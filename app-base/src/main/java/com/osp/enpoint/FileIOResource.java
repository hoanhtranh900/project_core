package com.osp.enpoint;

import com.osp.core.contants.Constants;
import com.osp.core.dto.request.DownloadOption;
import com.osp.core.dto.request.UploadFileDTO;
import com.osp.core.dto.response.ResponseData;
import com.osp.core.entity.AdmUser;
import com.osp.core.exception.BadRequestException;
import com.osp.core.exception.PermissionException;
import com.osp.core.exception.Result;
import com.osp.core.utils.UtilsCommon;
import com.osp.service.FileIOService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * @author
 */
@Slf4j
@RestController
@RequestMapping(value = "/files", produces = MediaType.APPLICATION_JSON_VALUE)
public class FileIOResource {

    @Autowired private FileIOService fileIOService;

    @PostMapping("/uploadFile")
    public ResponseEntity<?> uploadFile(
            @RequestParam(name = "file") MultipartFile uploadFile,
            @RequestParam(name = "objectType") Long objectType,
            @RequestParam(name = "note", required = false) String note,
            @RequestParam(name = "storageType", required = false) String storageType
    ) throws IOException {
        Optional<AdmUser> user = UtilsCommon.getUserLogin();
        return new ResponseEntity<>(new ResponseData<>(fileIOService.uploadFile(uploadFile, objectType, storageType, note, user.isPresent() ? user.get() : new AdmUser()), Result.SUCCESS), HttpStatus.OK);
    }

    @RequestMapping(path = "/downloadFile/{attachmentId}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "API sẽ được gọi trong trường hợp download file tu server ", notes = Constants.NOTE_API + "empty_note", authorizations = {@Authorization(value = Constants.API_KEY)})
    public ResponseEntity<InputStreamResource> downloadFile(HttpServletResponse response, @PathVariable Long attachmentId, DownloadOption downloadOption) throws Exception {
        return fileIOService.downloadFile(response, attachmentId, downloadOption);
    }

    @RequestMapping(path = "/downloadFileBase64/{attachmentId}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "API sẽ được gọi trong trường hợp download file tu server", notes = Constants.NOTE_API + "empty_note", authorizations = {@Authorization(value = Constants.API_KEY)})
    public ResponseEntity<ResponseData> downloadFileBase64(@PathVariable Long attachmentId) throws IOException {
        return new ResponseEntity<>(new ResponseData<>(fileIOService.downloadFileBase64(attachmentId), Result.SUCCESS), HttpStatus.OK);
    }

    @DeleteMapping("/deleteFile/{attachmentId}")
    @ApiOperation(value = "API sẽ được gọi trong trường hợp xóa file", notes = Constants.NOTE_API + "empty_note", authorizations = {@Authorization(value = Constants.API_KEY)})
    public ResponseEntity<ResponseData> deleteFile(@PathVariable Long attachmentId) {
        return new ResponseEntity<>(new ResponseData<>(fileIOService.deleteFile(attachmentId), Result.SUCCESS), HttpStatus.OK);
    }

    @PutMapping("/upload")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "API sẽ được gọi trong trường hợp thêm file ", notes = Constants.NOTE_API + "empty_note", authorizations = {@Authorization(value = Constants.API_KEY)})
    public ResponseEntity<ResponseData> updateImage(@RequestBody UploadFileDTO uploadFileDTO) throws BadRequestException, PermissionException {
        return new ResponseEntity<>(new ResponseData<>(fileIOService.updateFileForObject(uploadFileDTO), Result.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/getFileByObjectIdAndObjectType")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(notes = Constants.NOTE_API + "empty_note", value = "API get file by objectId and objectType", authorizations = {@Authorization(value = Constants.API_KEY)})
    public ResponseEntity<?> getFileByObjectIdAndType(
            @ApiParam(value = "objectId") @RequestParam(name = "objectId") Long objectId,
            @ApiParam(value = "objectType") @RequestParam(name = "objectType") Long objectType,
            @ApiParam(value = "storageType") @RequestParam(name = "storageType", required = false) String storageType)
            throws BadRequestException, PermissionException {
        return new ResponseEntity<>(new ResponseData<>(fileIOService.findByObjectIdAndObjectType(objectId, objectType, storageType), Result.SUCCESS), HttpStatus.OK);
    }

    public static void main(String[] args) {
        String pathStr = "upload/FileUploadAdfilex/Screenshot from 2021-03-02 08-30-32.png";
        String fileName = pathStr.substring(pathStr.lastIndexOf("/") + 1);
        System.out.println(fileName);
    }
}
