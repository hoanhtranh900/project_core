package com.osp.enpoint;

import com.osp.core.contants.ConstantAuthor;
import com.osp.core.contants.ConstantString;
import com.osp.core.contants.Constants;
import com.osp.core.controller.BaseControllerImpl;
import com.osp.core.dto.request.SearchForm;
import com.osp.core.dto.response.ResponseData;
import com.osp.core.entity.AdmAuthorities;
import com.osp.core.entity.CmCommune;
import com.osp.core.entity.view.ViewAdmAuthorities;
import com.osp.core.exception.BadRequestException;
import com.osp.core.exception.Result;
import com.osp.core.utils.JsonHelper;
import com.osp.service.AdmAuthoritiesService;
import com.osp.service.impl.AdmAuthoritiesServiceImpl;
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/system/authorities", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdmAuthoritiesResource extends BaseControllerImpl<AdmAuthorities, AdmAuthoritiesServiceImpl> {
    protected AdmAuthoritiesResource(AdmAuthoritiesServiceImpl service, MessageSource messageSource) {
        super(service, ConstantAuthor.Authority.view, messageSource);
    }

    @Autowired private AdmAuthoritiesService<AdmAuthorities> authoritiesService;
    @Autowired private MessageSource messageSource;

    @ApiOperation(response = AdmAuthorities[].class, notes = Constants.NOTE_API + "empty_note", value = "Danh sách chức năng cấp 1", authorizations = {@Authorization(value = Constants.API_KEY)})
    @GetMapping(value = "/loadListParent")
    @Secured({ConstantAuthor.Authority.view, ConstantAuthor.Authority.add, ConstantAuthor.Authority.edit})
    public ResponseEntity<ResponseData> getListParent() {
        return new ResponseEntity(new ResponseData(authoritiesService.loadListParent(), Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = AdmAuthorities[].class, notes = Constants.NOTE_API + "empty_note", value = "Danh sách chức năng cấp 2 theo id cấp 1", authorizations = {@Authorization(value = Constants.API_KEY)})
    @GetMapping(value = "/loadChildrenByParentId")
    @Secured({ConstantAuthor.Dept.view, ConstantAuthor.Dept.add, ConstantAuthor.Dept.edit})
    public ResponseEntity<ResponseData> loadChildrenByParentId(@RequestParam(value = "parentId") Long parentId) {
        return new ResponseEntity(new ResponseData(authoritiesService.findByParentIdAndListStatus(parentId,  Arrays.asList(ConstantString.STATUS.active, ConstantString.STATUS.lock) ), Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = AdmAuthorities.class, notes = Constants.NOTE_API + "empty_note", value = "Danh sách chức năng", authorizations = {@Authorization(value = Constants.API_KEY)})
    @GetMapping(value = "/getPage")
    @Secured({ConstantAuthor.Authority.view})
    public ResponseEntity<ResponseData> getPage(Pageable pageable, @ApiParam(value = Constants.NOTE_API_PAGEABLE) @RequestParam(value = "search") String search) {
        SearchForm searchObject = JsonHelper.jsonToObject(search, SearchForm.class);
        Map map = new HashMap();
        Page<ViewAdmAuthorities> pages = authoritiesService.getPage(searchObject, pageable);
        map.put("page", pages);
        map.put("listParent", authoritiesService.loadListParent());
        return new ResponseEntity<>(new ResponseData<>(map, Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = CmCommune.class, notes = Constants.NOTE_API + "empty_note", value = "khởi tạo thêm mới", authorizations = {@Authorization(value = Constants.API_KEY)})
    @GetMapping(value = "/initAdd")
    @Secured({ConstantAuthor.Commune.add})
    public ResponseEntity<ResponseData> initAdd() throws BadRequestException {
        return new ResponseEntity<>(new ResponseData<>(authoritiesService.initAddOrEdit(null), Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = AdmAuthorities.class, notes = Constants.NOTE_API + "empty_note", value = "Thêm mới chức năng", authorizations = {@Authorization(value = Constants.API_KEY)})
    @PostMapping
    @Secured({ConstantAuthor.Authority.add})
    public ResponseEntity<ResponseData> add(@RequestBody AdmAuthorities dto) {
        return new ResponseEntity<>(new ResponseData<>(authoritiesService.save(dto), Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = AdmAuthorities.class, notes = Constants.NOTE_API + "empty_note", value = "Lấy chức năng theo id", authorizations = {@Authorization(value = Constants.API_KEY)})
    @GetMapping(value = "/initEdit/{id}")
    @Secured({ConstantAuthor.Authority.edit})
    public ResponseEntity<ResponseData> initEdit(@PathVariable("id") Long id) throws BadRequestException {
        return new ResponseEntity<>(new ResponseData<>(authoritiesService.initAddOrEdit(id), Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = AdmAuthorities.class, notes = Constants.NOTE_API + "empty_note", value = "Sửa chức năng", authorizations = {@Authorization(value = Constants.API_KEY)})
    @PutMapping
    @Secured({ConstantAuthor.Authority.edit})
    public ResponseEntity<ResponseData> edit(@RequestBody AdmAuthorities dto) {
        return new ResponseEntity<>(new ResponseData<>(authoritiesService.edit(dto), Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = AdmAuthorities[].class, notes = Constants.NOTE_API + "empty_note", value = "Xóa chức năng", authorizations = {@Authorization(value = Constants.API_KEY)})
    @PostMapping(path = "/delete")
    @Secured({ConstantAuthor.Authority.delete})
    public ResponseEntity<ResponseData> deleteByIds(@RequestBody List<Long> ids) {
        return new ResponseEntity<>(new ResponseData<>(authoritiesService.deleteByIds(ids), Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = AdmAuthorities[].class, notes = Constants.NOTE_API + "empty_note", value = "Khóa chức năng", authorizations = {@Authorization(value = Constants.API_KEY)})
    @PostMapping(path = "/lock")
    @Secured({ConstantAuthor.Authority.edit})
    public ResponseEntity<ResponseData> locks(@RequestBody List<Long> ids) {
        return new ResponseEntity<>(new ResponseData<>(authoritiesService.locks(ids), Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = AdmAuthorities[].class, notes = Constants.NOTE_API + "empty_note", value = "Mở khóa chức năng", authorizations = {@Authorization(value = Constants.API_KEY)})
    @PostMapping(path = "/unlock")
    @Secured({ConstantAuthor.Authority.edit})
    public ResponseEntity<ResponseData> unlocks(@RequestBody List<Long> ids) {
        return new ResponseEntity<>(new ResponseData<>(authoritiesService.unlocks(ids), Result.SUCCESS), HttpStatus.OK);
    }

}
