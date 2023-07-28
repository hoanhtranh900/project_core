package com.osp.service;

import com.osp.core.dto.request.DownloadOption;
import com.osp.core.dto.request.SearchForm;
import com.osp.core.dto.request.UploadFileDTO;
import com.osp.core.entity.AdmUser;
import com.osp.core.entity.FileAttachment;
import com.osp.core.exception.BadRequestException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

public interface FileIOService {

    Optional<FileAttachment> uploadFile(MultipartFile fileUpoad, Long objectType, String storageType, String note, AdmUser actor) throws IOException;

    Optional<FileAttachment> findByAttachmentId(Long attachmentId);

    List<FileAttachment> findByAttachmentIds(List<Long> attachmentIds, Long objectType);

    ResponseEntity<InputStreamResource> downloadFile(HttpServletResponse response, Long attachmentId, DownloadOption downloadOption) throws Exception;

    List<String> getListFile(String path);

    void savaAttachment(FileAttachment attachment);

    List<FileAttachment> savaAttachments(List<FileAttachment> attachmentList);

    List<FileAttachment> findByObjectIdAndObjectType(Long objectId, Long objectType, String storageType);

    Boolean updateFileForObject(UploadFileDTO uploadFileDTO) throws BadRequestException;

    FileAttachment deleteFile(Long attachmentId);

    String downloadFileBase64(Long attachmentId) throws IOException;
}
