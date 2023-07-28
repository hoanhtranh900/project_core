package com.osp.service;

import com.osp.core.dto.request.DownloadOption;
import com.osp.core.dto.request.ExtractFileOption;
import com.osp.core.dto.request.ExtractFileResult;
import com.osp.core.dto.request.UploadOption;
import com.osp.core.dto.response.GetNumberPagesOfPdfResponse;
import com.osp.core.entity.AdmUser;
import com.osp.core.entity.FileAttachDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

/**
 * TODO: write you class description here
 *
 * @author
 */

public interface FileAttachDocumentService<E> {

    interface IProcessFile {
        void do_(InputStream inputStream);
    }

    Optional<E> save(E entity);
    Optional<E> update(E entity);
    Optional<E> get(Long id);
    Page<E> getPaging(Pageable pageable);
    List<E> getAll();
    Boolean deleteById(Long id);
    Boolean deleteAll();

    void getPdfFile(FileAttachDocument attachDocument, DownloadOption downloadOption, IProcessFile processFile);

    FileAttachDocument populateOriginalAttachDocument(File finalOriginalFile, String fileId, Boolean isEncrypted, String contentType, AdmUser actor);

    GetNumberPagesOfPdfResponse getNumberPagesOfPdf(Long id);

    void downloadFile(HttpServletResponse response, Long id, DownloadOption downloadOption) throws Exception;
    String downloadFileBase64(Long id) throws IOException;

    FileAttachDocument uploadFile(UploadOption uploadOption, MultipartFile uploadFile, AdmUser actor) throws IOException;
    FileAttachDocument uploadImageFileBase64(String imageBase64, UploadOption uploadOption, AdmUser actor) throws Exception;
    ExtractFileResult convertPdfToImage(ExtractFileOption extractFileOption, AdmUser actor) throws FileNotFoundException;

}
