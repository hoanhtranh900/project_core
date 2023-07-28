package com.osp.enpoint;

import com.osp.core.contants.ConstantAuthor;
import com.osp.core.contants.ConstantString;
import com.osp.core.contants.Constants;
import com.osp.core.controller.BaseControllerImpl;
import com.osp.core.dto.response.ResponseData;
import com.osp.core.dto.request.SearchForm;
import com.osp.core.entity.view.ViewAdmUser;
import com.osp.core.exception.BaseException;
import com.osp.core.exception.Result;
import com.osp.core.utils.JsonHelper;
import com.osp.core.entity.AdmUser;
import com.osp.core.utils.UtilsCommon;
import com.osp.service.AdmUserService;
import com.osp.service.impl.AdmUserServiceImpl;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/system/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdmUserResource extends BaseControllerImpl<AdmUser, AdmUserServiceImpl> {

    //PreAuthorize -> hasAuthority -> dành cho những role đã bắt đầu ROLE_
    //PreAuthorize -> hasRole -> dành cho những role chưa bắt đầu ROLE_            recommended  hasRole('ROLE_SYSTEM_USER')
    //Secured -> là thuộc tính or giữa nhiều ROLE_

    protected AdmUserResource(AdmUserServiceImpl service, MessageSource messageSource) {
        super(service, ConstantAuthor.User.view, messageSource);
    }

    @Autowired private AdmUserService<AdmUser> admUserService;
    @Autowired private MessageSource messageSource;

    @ApiOperation(response = AdmUser.class, notes = Constants.NOTE_API + "empty_note", value = "Danh sách người dùng", authorizations = {@Authorization(value = Constants.API_KEY)})
    @GetMapping(value = "/getPage")
    @Secured({ConstantAuthor.User.view})
    public ResponseEntity<ResponseData> getPage(Pageable pageable, @ApiParam(value = Constants.NOTE_API_PAGEABLE) @RequestParam @Valid String search) {
        SearchForm searchObject = JsonHelper.jsonToObject(search, SearchForm.class);
        Page<ViewAdmUser> pages = admUserService.getPage(searchObject, pageable);
        return new ResponseEntity<>(new ResponseData<>(pages, Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = AdmUser.class, notes = Constants.NOTE_API + "empty_note", value = "Thêm mới người dùng", authorizations = {@Authorization(value = Constants.API_KEY)})
    @PostMapping
    @Secured({ConstantAuthor.User.add})
    public ResponseEntity<ResponseData> add(@RequestBody @Valid AdmUser user) {
        return new ResponseEntity<>(new ResponseData<>(admUserService.save(user), Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = AdmUser.class, notes = Constants.NOTE_API + "empty_note", value = "Lấy người dùng theo ID", authorizations = {@Authorization(value = Constants.API_KEY)})
    @GetMapping(value = "/initEdit/{userId}")
    @Secured({ConstantAuthor.User.edit})
    public ResponseEntity<ResponseData> initEdit(@PathVariable("userId") @Valid Long userId) throws Throwable {
        return new ResponseEntity<>(new ResponseData<>(admUserService.get(userId).orElseThrow(() -> new BaseException(messageSource.getMessage("error.ENTITY_NOT_FOUND", new Object[]{"User"}, UtilsCommon.getLocale()))), Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = AdmUser.class, notes = Constants.NOTE_API + "empty_note", value = "Chỉnh sửa người dùng", authorizations = {@Authorization(value = Constants.API_KEY)})
    @PutMapping
    @Secured({ConstantAuthor.User.edit})
    public ResponseEntity<ResponseData> edit(@RequestBody AdmUser dto) throws Throwable {
        AdmUser user = admUserService.get(dto.getId()).orElseThrow(() -> new BaseException(messageSource.getMessage("error.ENTITY_NOT_FOUND", new Object[]{"User"}, UtilsCommon.getLocale())));
        return new ResponseEntity<>(new ResponseData<>(admUserService.update(dto.formToBo(dto, user)), Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = AdmUser[].class, notes = Constants.NOTE_API + "empty_note", value = "Xóa người dùng", authorizations = {@Authorization(value = Constants.API_KEY)})
    @PostMapping(path = "/delete")
    @Secured({ConstantAuthor.User.delete})
    public ResponseEntity<ResponseData> deleteByIds(@RequestBody @Valid List<Long> ids) {
        return new ResponseEntity<>(new ResponseData<>(admUserService.deleteByIds(ids), Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = AdmUser[].class, notes = Constants.NOTE_API + "empty_note", value = "Khóa người dùng", authorizations = {@Authorization(value = Constants.API_KEY)})
    @PostMapping(path = "/lock")
    @Secured({ConstantAuthor.User.edit})
    public ResponseEntity<ResponseData> locks(@RequestBody @Valid List<Long> ids) {
        return new ResponseEntity<>(new ResponseData<>(admUserService.locks(ids), Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = AdmUser[].class, notes = Constants.NOTE_API + "empty_note", value = "Mở khóa người dùng", authorizations = {@Authorization(value = Constants.API_KEY)})
    @PostMapping(path = "/unlock")
    @Secured({ConstantAuthor.User.edit})
    public ResponseEntity<ResponseData> unlocks(@RequestBody @Valid List<Long> ids) {
        return new ResponseEntity<>(new ResponseData<>(admUserService.unlocks(ids), Result.SUCCESS), HttpStatus.OK);
    }

}
