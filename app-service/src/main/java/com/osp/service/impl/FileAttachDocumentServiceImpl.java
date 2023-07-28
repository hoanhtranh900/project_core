package com.osp.service.impl;

import com.itextpdf.text.pdf.PdfReader;
import com.osp.core.contants.ConstantString;
import com.osp.core.contants.Constants;
import com.osp.core.dto.request.DownloadOption;
import com.osp.core.dto.request.ExtractFileOption;
import com.osp.core.dto.request.ExtractFileResult;
import com.osp.core.dto.request.UploadOption;
import com.osp.core.dto.response.GetNumberPagesOfPdfResponse;
import com.osp.core.entity.AdmUser;
import com.osp.core.entity.FileAttachDocument;
import com.osp.core.entity.FileAttachmentMetadata;
import com.osp.core.entity.FileImageExtract;
import com.osp.core.exception.BaseException;
import com.osp.core.exception.StorageException;
import com.osp.core.repository.FileAttachDocumentRepository;
import com.osp.core.service.BaseServiceImpl;
import com.osp.core.utils.H;
import com.osp.core.utils.UtilsCommon;
import com.osp.notification.TelegramBot;
import com.osp.service.FileAttachDocumentService;
import com.osp.service.FileDownloadService;
import com.osp.service.FileImageExtractService;
import com.osp.service.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

/**
 * TODO: write you class description here
 *
 * @author
 */

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class FileAttachDocumentServiceImpl extends BaseServiceImpl<FileAttachDocument, FileAttachDocumentRepository> implements FileAttachDocumentService<FileAttachDocument> {
    public FileAttachDocumentServiceImpl(FileAttachDocumentRepository repository) {
        super(repository);
    }
    @Autowired private FileStorageService storageService;
    @Autowired private FileDownloadService downloadService;
    @Autowired private FileAttachDocumentRepository attachDocumentRepository;
    @Autowired private FileImageExtractService<FileImageExtract> fileImageExtractService;
    @Autowired private MessageSource messageSource;
    @Autowired private TelegramBot telegramBot;
    @Value("${upload.tempSubFolder}")
    private String tempSubFolder;

    @Override
    public void getPdfFile(FileAttachDocument attachDocument, DownloadOption downloadOption, IProcessFile processFile) {
        List<File> tempFiles = new ArrayList<>();
        try {
            File finalFile;
            InputStream finalInputStream = storageService.getInputStream(attachDocument);
            /**
             * include includeAttachDocument to attachDocument
             */
            Long includeAttachDocumentId = downloadOption.getIncludeAttachDocumentId();
            FileAttachDocument includeAttachDocument = H.isTrue(includeAttachDocumentId) ? attachDocumentRepository.getById(includeAttachDocumentId) : null;
            if (H.isTrue(includeAttachDocument)) {
                finalFile = storageService.mergePdfFiles(storageService.getLocalTempFile(), storageService.getInputStream(includeAttachDocument), finalInputStream);
                finalInputStream = new FileInputStream(finalFile);
                tempFiles.add(finalFile);
            }

            /**
             * them water-mark text;
             */
            String waterMarkText = downloadOption.getWaterMarkText();
            if (H.isTrue(waterMarkText)) {
                storageService.addWaterMark(finalInputStream, waterMarkText, new FileOutputStream(finalFile = storageService.getLocalTempFile()));
                finalInputStream = new FileInputStream(finalFile);
                tempFiles.add(finalFile);
            }

            processFile.do_(finalInputStream);
        } catch (FileNotFoundException e) {
            throw new StorageException("getPdfFile error", e);
        } finally {
            try {
                if (H.isTrue(tempFiles)) {
                    storageService.deleteFile(tempFiles);
                }
            } catch (Exception ex) {
                log.warn("delete tempFiles error", ex);
            }
        }
    }

    @Override
    public FileAttachDocument populateOriginalAttachDocument(File finalOriginalFile, String fileId, Boolean isEncrypted, String contentType, AdmUser actor) {
        FileAttachDocument originalAttachDocument = new FileAttachDocument();
        originalAttachDocument.setFileName(finalOriginalFile.getName());
        originalAttachDocument.setFileSize(finalOriginalFile.length());
        originalAttachDocument.setIsOriginal(ConstantString.ATTACH_DOCUMENT.ORIGINAL);
        originalAttachDocument.setFilePath(finalOriginalFile.getAbsolutePath());
        originalAttachDocument.setIsEncrypt(isEncrypted ? ConstantString.ATTACH_DOCUMENT.ENCRYPTED : null);
        originalAttachDocument.setContentType(contentType);
        originalAttachDocument.setCreateBy(actor.getUsername());
        originalAttachDocument.setCreatedDate(new Date());
        originalAttachDocument.setModifiedBy(actor.getUsername());
        originalAttachDocument.setModifiedDate(new Date());
        return originalAttachDocument;
    }

    @Override
    public GetNumberPagesOfPdfResponse getNumberPagesOfPdf(Long id) {
        try {
            FileAttachDocument attachDocument = attachDocumentRepository.getById(id);
            InputStream finalInputStream = storageService.getInputStream(attachDocument);
            PdfReader pdfReader = new PdfReader(finalInputStream);
            int numberOfPages = pdfReader.getNumberOfPages();
            GetNumberPagesOfPdfResponse result = new GetNumberPagesOfPdfResponse();
            result.setId(id);
            result.setNumberOfPages((long) numberOfPages);
            return result;
        } catch (IOException e) {
            throw new StorageException("getNumberPagesOfPdf.error", e);
        }
    }

    @Override
    public void downloadFile(HttpServletResponse response, Long id, DownloadOption downloadOption) throws Exception {
        FileAttachDocument attachDocument = attachDocumentRepository.getById(id);
        try {
            this.getPdfFile(
                    attachDocument, downloadOption,
                    inputStream -> {
                        try {
                            MediaType mediaType = H.isTrue(downloadOption.getWaterMarkText())
                                    || H.isTrue(downloadOption.getIncludeAttachDocumentId())
                                    ? MediaType.APPLICATION_PDF : MediaType.valueOf(attachDocument.getContentType());
                            String fileName = attachDocument.getFileName();
                            downloadService.downloadFile(response, fileName, mediaType.toString(), os -> {
                                try {
                                    storageService.writeFile(inputStream, os);
                                } catch (IOException e) {
                                    throw new StorageException("downloadFile.error", e);
                                }
                            });
                        } catch (Exception ex) {
                            throw new StorageException("downloadFile.error", ex);
                        }
                    }
            );
        } catch (Exception ex) {
            throw new StorageException("downloadFile.error", ex);
        }
    }

    @Override
    public String downloadFileBase64(Long id) throws IOException {
        FileAttachDocument attachDocument = attachDocumentRepository.findById(id).orElseThrow(() -> new BaseException(
                messageSource.getMessage("error.ENTITY_NOT_FOUND", new Object[]{"File"}, UtilsCommon.getLocale())
        ));
        InputStream inputStream = storageService.getInputStream(attachDocument);
        String base64 = Base64.getEncoder().encodeToString(IOUtils.toByteArray(inputStream));
        return base64;
    }

    @Override
    public FileAttachDocument uploadFile(UploadOption uploadOption, MultipartFile uploadFile, AdmUser actor) throws IOException {
        String contentType = uploadOption.getContentType();
        String pdfFileId = UUID.randomUUID().toString();
        String subFolder = storageService.getSubFolder();
        File finalOriginalFile = storageService.saveFile(pdfFileId, subFolder, uploadFile.getInputStream(), uploadOption.getNeedEncrypt());

        FileAttachDocument originalAttachDocument = populateOriginalAttachDocument(finalOriginalFile, pdfFileId, Boolean.TRUE.equals(uploadOption.getNeedEncrypt()), contentType, actor);
        return attachDocumentRepository.save(originalAttachDocument);
    }

    @Override
    public FileAttachDocument uploadImageFileBase64(String imageBase64, UploadOption uploadOption, AdmUser actor) throws Exception {
        String fileId = UUID.randomUUID().toString();
        String subFolder = storageService.getSubFolder();
        Base64InputStream inputStream = new Base64InputStream(IOUtils.toInputStream(imageBase64), true);
        File finalOriginalFile = storageService.saveFile(fileId, subFolder, inputStream, uploadOption.getNeedEncrypt());
        FileAttachDocument originalAttachDocument = populateOriginalAttachDocument(finalOriginalFile, fileId, Boolean.TRUE.equals(uploadOption.getNeedEncrypt()), uploadOption.getContentType(), actor);
        return attachDocumentRepository.save(originalAttachDocument);
    }

    @Override
    public ExtractFileResult convertPdfToImage(ExtractFileOption extractFileOption, AdmUser actor) throws FileNotFoundException {
        FileAttachDocument attachDocument = attachDocumentRepository.getById(extractFileOption.getAttachmentId());
        if (!H.isTrue(attachDocument) || !attachDocument.getContentType().equalsIgnoreCase(Constants.CONTENT_TYPE.PDF)) {
            return null;
        }
        return extractPdfToImage(attachDocument, extractFileOption, actor);
    }

    private ExtractFileResult extractPdfToImage(FileAttachDocument pdfAttachDocument, ExtractFileOption extractFileOption, AdmUser actor) throws FileNotFoundException {
        List<File> tempFiles = new ArrayList<>();
        ExtractFileResult extractFileResult = new ExtractFileResult();
        InputStream pdfFileInputStream = storageService.getInputStream(new FileInputStream(pdfAttachDocument.getFilePath()), H.isTrue(pdfAttachDocument.getIsEncrypt()));

        List<FileImageExtract> imageExtractList = new ArrayList<>();
        PDDocument document = null;
        try {
            document = PDDocument.load(pdfFileInputStream);
            PDFRenderer pdfRenderer = new PDFRenderer(document);

            FileAttachmentMetadata attachmentMetadata = populateAttachmentMetadata((long) document.getNumberOfPages(), pdfAttachDocument.getFileSize(), extractFileOption);
            extractFileResult.setAttachmentMetadata(attachmentMetadata);

            for (int page = 0; page < document.getNumberOfPages(); ++page) {

                BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 150, ImageType.RGB);
                File imageFile = storageService.getLocalFile(UUID.randomUUID().toString() + Constants.FILE_EXTENSION.IMAGE_PNG, tempSubFolder);

                ImageIO.write(bim, "PNG", new FileOutputStream(imageFile));
                tempFiles.add(imageFile);

                String finalImageFileID = UUID.randomUUID().toString();
                File finalImageFile = storageService.saveFile(
                        finalImageFileID, storageService.getSubFolder(), new FileInputStream(imageFile),
                        H.isTrue(pdfAttachDocument.getIsEncrypt())
                );

                imageExtractList.add(populateImageExtract(
                        pdfAttachDocument, attachmentMetadata.getId(), finalImageFile, page,
                        extractFileOption.getVbAttachmentId(), actor
                ));
            }
            document.close();

            if (H.isTrue(tempFiles)) {
                storageService.deleteFile(tempFiles);
            }

        } catch (IOException e) {
            log.warn("extractPdfToImage: error", e);
        } finally {
            try {
                if (document != null) {
                    document.close();
                }
            } catch (Exception ex) {
                log.warn("document close error", ex);
            }
        }
        extractFileResult.setImageExtractList(imageExtractList);
        return extractFileResult;
    }

    private FileImageExtract populateImageExtract(FileAttachDocument pdfAttachDocument, Long id, File imageConvert, Integer pageNumber, Long vbAttachmentId, AdmUser actor) {
        FileImageExtract imageExtract = new FileImageExtract();
        if (actor != null) {
            imageExtract.setCreateBy(actor.getFullName());
            imageExtract.setModifiedBy(actor.getFullName());
        }
        imageExtract.setVbAttachmentId(vbAttachmentId);
        imageExtract.setAttachmentMetadataId(id);
        imageExtract.setFileSize(imageConvert.getTotalSpace());
        imageExtract.setPageNumber(pageNumber.longValue());
        imageExtract.setFileName(imageConvert.getName());
        imageExtract.setFileExtention(Constants.FILE_EXTENSION.IMAGE_PNG);
        imageExtract.setFilePath(imageConvert.getPath());
        imageExtract.setNote("");
        imageExtract.setIsEncrypt(pdfAttachDocument.getIsEncrypt());
        imageExtract.setStatus(ConstantString.STATUS.active);
        imageExtract.setIsDelete(ConstantString.IS_DELETE.active);
        fileImageExtractService.save(imageExtract);
        return imageExtract;
    }

    /**
     * @param numberOfPages
     * @return
     */
    private FileAttachmentMetadata populateAttachmentMetadata(Long numberOfPages, Long fileSize, ExtractFileOption extractFileOption) {
        FileAttachmentMetadata attachmentMetadata = new FileAttachmentMetadata();
        attachmentMetadata.setVbAttachmentId(extractFileOption.getVbAttachmentId());
        attachmentMetadata.setObjectType(extractFileOption.getObjectType());
        attachmentMetadata.setSizeTotal(fileSize);
        attachmentMetadata.setStatus(Constants.IMAGE_EXTRACT_STATUS.ORIGINAL);
        attachmentMetadata.setPageSize(numberOfPages);
        return attachmentMetadata;
    }
}
