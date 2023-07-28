package com.osp.service.impl;

import com.osp.core.contants.ConstantString;
import com.osp.core.dto.request.DownloadOption;
import com.osp.core.dto.request.UploadFileDTO;
import com.osp.core.dto.request.UploadOption;
import com.osp.core.entity.AdmUser;
import com.osp.core.entity.FileAttachDocument;
import com.osp.core.entity.FileAttachment;
import com.osp.core.exception.BadRequestException;
import com.osp.core.exception.BaseException;
import com.osp.core.repository.FileAttachmentRepository;
import com.osp.core.utils.H;
import com.osp.core.utils.UtilsCommon;
import com.osp.core.utils.UtilsString;
import com.osp.service.FileAttachDocumentService;
import com.osp.service.FileAttachmentService;
import com.osp.service.FileIOService;
import com.osp.service.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class FileIOServiceImpl implements FileIOService {
    @Autowired private MessageSource messageSource;
    @Autowired private FileAttachmentRepository attachmentRepository;
    @Autowired private FileAttachmentService<FileAttachment> attachmentService;
    @Autowired private FileAttachDocumentService<FileAttachDocument> attachmentDocumentService;
    @Autowired private FileStorageService storageService;

    @Override
    public Optional<FileAttachment> uploadFile(MultipartFile multipartFile, Long objectType, String storageType, String note, AdmUser actor) throws IOException {
        String contentType = multipartFile.getContentType();

        UploadOption uploadOption = new UploadOption();
        uploadOption.setNeedConvertPdf(false);
        uploadOption.setNeedEncrypt(true);

        FileAttachment vbAttachment = new FileAttachment();
        vbAttachment.setObjectType(objectType);
        vbAttachment.setStorageType(storageType);
        String fileName = multipartFile.getOriginalFilename();
        String fileType = fileName.contains(".") ? fileName.substring(fileName.lastIndexOf(".") + 1).trim().toLowerCase() : "";
        vbAttachment.setFileName(multipartFile.getOriginalFilename());
        vbAttachment.setFileExtention(fileType);

        vbAttachment.setNote(note);
        vbAttachment.setIsDelete(ConstantString.IS_DELETE.active);

        vbAttachment.setContentType(contentType);
        vbAttachment.setUpdatorId(actor.getId());
        vbAttachment.setCreatorId(actor.getId());
        FileAttachDocument attachDocument = attachmentDocumentService.uploadFile(uploadOption, multipartFile,actor);
        vbAttachment.setIsEncrypt(attachDocument.getIsEncrypt());
        vbAttachment.setFileServiceId(attachDocument.getId());
        vbAttachment.setPdfAlready(H.isTrue(attachDocument.getPdfAttachmentDocumentId()) ? 1L : 0);
        return attachmentService.save(vbAttachment);
    }

    @Override
    public Optional<FileAttachment> findByAttachmentId(Long attachmentId) {
        return attachmentRepository.findByIdAndIsDelete(attachmentId, ConstantString.IS_DELETE.active);
    }

    @Override
    public List<FileAttachment> findByAttachmentIds(List<Long> attachmentIds, Long objectType) {
        return attachmentRepository.findByIdInAndObjectType(attachmentIds, ConstantString.IS_DELETE.active, objectType);
    }

    @Override
    public ResponseEntity<InputStreamResource> downloadFile(HttpServletResponse response, Long attachmentId, DownloadOption downloadOption) throws Exception {
        Optional<FileAttachment> attachmentOpt = attachmentRepository.findByIdAndIsDelete(attachmentId, ConstantString.IS_DELETE.active);
        if (!attachmentOpt.isPresent()) {
            throw new BadRequestException(
                    messageSource.getMessage("error.ENTITY_NOT_FOUND", new Object[]{"File"}, UtilsCommon.getLocale())
            );
        }
        FileAttachment attachment = attachmentOpt.get();
        InputStream inputStream = storageService.getInputStream(attachmentDocumentService.get(attachment.getFileServiceId()).get());
        String contentType = H.isTrue(downloadOption.getType()) ? downloadOption.getType() : attachment.getContentType();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=" + UtilsString.encodeStringToUrlParam(attachment.getFileName()))
                .contentType(MediaType.valueOf(contentType))
                .body(new InputStreamResource(inputStream));

    }

    @Override
    public List<String> getListFile(String path) {
        File file = new File(path);
        List<String> listFileNames = new ArrayList<>();
        for (String string : Arrays.asList(file.list())) {
            listFileNames.add(string);
        }
        return listFileNames;
    }

    @Override
    public void savaAttachment(FileAttachment attachment) {
        attachmentService.save(attachment);
    }

    @Override
    public List<FileAttachment> savaAttachments(List<FileAttachment> attachmentList) {
        List<FileAttachment> lst = new ArrayList<>();
        for (FileAttachment attachment : attachmentList) {
            attachment = attachmentService.save(attachment).get();
            lst.add(attachment);
        }
        return lst;
    }

    @Override
    public List<FileAttachment> findByObjectIdAndObjectType(Long objectId, Long objectType, String storageType) {
        return attachmentRepository.findByObjectIdAndObjectTypeAndIsDelete(objectId, objectType, storageType, ConstantString.IS_DELETE.active);
    }

    @Override
    public Boolean updateFileForObject(UploadFileDTO uploadFileDTO) throws BadRequestException {
        if(H.isTrue(uploadFileDTO.getListFileIds())){
            for ( Long attachId : uploadFileDTO.getListFileIds()) {
                Optional<FileAttachment> vbAttachmentOpt = attachmentRepository.findByIdAndIsDelete(attachId, ConstantString.IS_DELETE.active);
                if(vbAttachmentOpt.isPresent()){
                    FileAttachment vbAttachment = vbAttachmentOpt.get();
                    vbAttachment.setObjectId(uploadFileDTO.getObjectId());
                    attachmentRepository.save(vbAttachment);
                }
                else throw new BadRequestException("File not found or deleted");
            }
            return true;
        }
        return false;
    }

    @Override
    public FileAttachment deleteFile(Long attachmentId) {
        FileAttachment vbAttachment = attachmentRepository.findByIdAndIsDelete(attachmentId, ConstantString.IS_DELETE.active).orElseThrow(() -> new BaseException(
                messageSource.getMessage("error.ENTITY_NOT_FOUND", new Object[]{"File"}, UtilsCommon.getLocale())
        ));
        vbAttachment.setIsDelete(ConstantString.IS_DELETE.delete);
        attachmentRepository.save(vbAttachment);
        return vbAttachment;
    }

    @Override
    public String downloadFileBase64(Long attachmentId) throws IOException {
        FileAttachment vbAttachment = attachmentRepository.findByIdAndIsDelete(attachmentId, ConstantString.IS_DELETE.active).orElseThrow(() -> new BaseException(
                messageSource.getMessage("error.ENTITY_NOT_FOUND", new Object[]{"File"}, UtilsCommon.getLocale())
        ));
        InputStream inputStream = storageService.getInputStream(attachmentDocumentService.get(vbAttachment.getFileServiceId()).get());
        String base64 = Base64.getEncoder().encodeToString(IOUtils.toByteArray(inputStream));
        return base64;
    }
}
