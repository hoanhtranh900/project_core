package com.osp.enpoint;

import com.osp.core.contants.Constants;
import com.osp.core.dto.request.DownloadOption;
import com.osp.core.dto.request.ExtractFileOption;
import com.osp.core.dto.request.ExtractFileResult;
import com.osp.core.dto.request.UploadOption;
import com.osp.core.dto.response.GetNumberPagesOfPdfResponse;
import com.osp.core.dto.response.ResponseData;
import com.osp.core.entity.AdmUser;
import com.osp.core.entity.FileAttachDocument;
import com.osp.core.exception.Result;
import com.osp.core.utils.UtilsCommon;
import com.osp.service.FileAttachDocumentService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping(value = "/file", produces = MediaType.APPLICATION_JSON_VALUE)
public class FileResource {
    @Autowired private MessageSource messageSource;
    @Autowired private FileAttachDocumentService<FileAttachDocument> attachDocumentService;

    @GetMapping("/{id}/getNumberPagesOfPdf")
    public GetNumberPagesOfPdfResponse getNumberPagesOfPdf(@PathVariable Long id) {
        return attachDocumentService.getNumberPagesOfPdf(id);
    }

    @GetMapping("/downloadFile/{id}")
    public void downloadFile(HttpServletResponse response, @PathVariable Long id, DownloadOption downloadOption) throws Exception {
        attachDocumentService.downloadFile(response, id, downloadOption);
    }

    @RequestMapping(path = "/downloadFileBase64/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "API sẽ được gọi trong trường hợp download file tu server", notes = Constants.NOTE_API + "empty_note", authorizations = {@Authorization(value = Constants.API_KEY)})
    public ResponseEntity<ResponseData> downloadFileBase64(@PathVariable Long id) throws IOException {
        return new ResponseEntity<>(new ResponseData<>(attachDocumentService.downloadFileBase64(id), Result.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/uploadFile")
    public FileAttachDocument uploadFile(@RequestParam(name = "file") MultipartFile uploadFile, UploadOption uploadOption) throws Exception {
        Optional<AdmUser> user = UtilsCommon.getUserLogin();
        return attachDocumentService.uploadFile(uploadOption, uploadFile, user.isPresent() ? user.get() : new AdmUser());
    }

    @PostMapping("/uploadImageFileBase64")
    public FileAttachDocument uploadImageFileBase64(UploadOption uploadOption) throws Exception {
        Optional<AdmUser> user = UtilsCommon.getUserLogin();
        return attachDocumentService.uploadImageFileBase64(uploadOption.getFileBase64(), uploadOption, user.isPresent() ? user.get() : new AdmUser());
    }

    @PostMapping("/convertPdfToImage")
    public ExtractFileResult convertPdfToImage(ExtractFileOption extractFileOption) throws FileNotFoundException {
        Optional<AdmUser> user = UtilsCommon.getUserLogin();
        return attachDocumentService.convertPdfToImage(extractFileOption, user.isPresent() ? user.get() : new AdmUser());
    }

}
