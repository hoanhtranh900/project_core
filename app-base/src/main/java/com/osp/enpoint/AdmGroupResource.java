package com.osp.enpoint;

import com.osp.core.contants.ConstantAuthor;
import com.osp.core.contants.ConstantString;
import com.osp.core.contants.Constants;
import com.osp.core.controller.BaseControllerImpl;
import com.osp.core.dto.response.ResponseData;
import com.osp.core.dto.request.SearchForm;
import com.osp.core.entity.view.ViewAdmGroup;
import com.osp.core.exception.BadRequestException;
import com.osp.core.exception.Result;
import com.osp.core.utils.JsonHelper;
import com.osp.core.entity.AdmGroup;
import com.osp.service.AdmGroupService;
import com.osp.service.impl.AdmGroupServiceImpl;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/system/group", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdmGroupResource extends BaseControllerImpl<AdmGroup, AdmGroupServiceImpl> {
    protected AdmGroupResource(AdmGroupServiceImpl service, MessageSource messageSource) {
        super(service, ConstantAuthor.Group.view, messageSource);
    }

    @Autowired private AdmGroupService<AdmGroup> groupService;
    @Autowired private MessageSource messageSource;

    @ApiOperation(response = AdmGroup.class, notes = Constants.NOTE_API + "empty_note", value = "Danh sách nhóm quyền", authorizations = {@Authorization(value = Constants.API_KEY)})
    @GetMapping(value = "/getPage")
    @Secured({ConstantAuthor.Group.view})
    public ResponseEntity<ResponseData> getPage(Pageable pageable, @ApiParam(value = Constants.NOTE_API_PAGEABLE) @RequestParam(value = "search") String search) {
        SearchForm searchObject = JsonHelper.jsonToObject(search, SearchForm.class);
        Page<ViewAdmGroup> pages = groupService.getPage(searchObject, pageable);
        return new ResponseEntity<>(new ResponseData<>(pages, Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = AdmGroup.class, notes = Constants.NOTE_API + "empty_note", value = "khởi tạo nhóm quyền", authorizations = {@Authorization(value = Constants.API_KEY)})
    @GetMapping(value = "/initAdd")
    @Secured({ConstantAuthor.Group.add})
    public ResponseEntity<ResponseData> initAdd() {
        return new ResponseEntity<>(new ResponseData<>(groupService.initAdd(), Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = AdmGroup.class, notes = Constants.NOTE_API + "empty_note", value = "Load trang thêm mới nhóm quyền", authorizations = {@Authorization(value = Constants.API_KEY)})
    @PostMapping
    @Secured({ConstantAuthor.Group.add})
    public ResponseEntity<ResponseData> add(@RequestBody AdmGroup group) {
        return new ResponseEntity<>(new ResponseData<>(groupService.addGroup(group), Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = AdmGroup.class, notes = Constants.NOTE_API + "empty_note", value = "Thêm mới nhóm quyền", authorizations = {@Authorization(value = Constants.API_KEY)})
    @GetMapping(value = "/initEdit/{groupId}")
    @Secured({ConstantAuthor.Group.edit})
    public ResponseEntity<ResponseData> initEdit(@PathVariable("groupId") Long groupId) {
        return new ResponseEntity<>(new ResponseData<>(groupService.initEdit(groupId), Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = AdmGroup.class, notes = Constants.NOTE_API + "empty_note", value = "Load trang sửa nhóm quyền theo ID", authorizations = {@Authorization(value = Constants.API_KEY)})
    @PutMapping
    @Secured({ConstantAuthor.Group.edit})
    public ResponseEntity<ResponseData> edit(@RequestBody AdmGroup dto) throws BadRequestException {
        return new ResponseEntity<>(new ResponseData<>(groupService.editGroup(dto), Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = AdmGroup[].class, notes = Constants.NOTE_API + "empty_note", value = "Xóa nhóm quyền", authorizations = {@Authorization(value = Constants.API_KEY)})
    @PostMapping(path = "/delete")
    @Secured({ConstantAuthor.Group.delete})
    public ResponseEntity<ResponseData> deleteByIds(@RequestBody List<Long> ids) {
        return new ResponseEntity<>(new ResponseData<>(groupService.deleteByIds(ids), Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = AdmGroup[].class, notes = Constants.NOTE_API + "empty_note", value = "Khóa nhóm quyền", authorizations = {@Authorization(value = Constants.API_KEY)})
    @PostMapping(path = "/lock")
    @Secured({ConstantAuthor.Group.edit})
    public ResponseEntity<ResponseData> locks(@RequestBody List<Long> ids) {
        return new ResponseEntity<>(new ResponseData<>(groupService.locks(ids), Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = AdmGroup[].class, notes = Constants.NOTE_API + "empty_note", value = "Mở khóa nhóm quyền", authorizations = {@Authorization(value = Constants.API_KEY)})
    @PostMapping(path = "/unlock")
    @Secured({ConstantAuthor.Group.edit})
    public ResponseEntity<ResponseData> unlocks(@RequestBody List<Long> ids) {
        return new ResponseEntity<>(new ResponseData<>(groupService.unlocks(ids), Result.SUCCESS), HttpStatus.OK);
    }
}
