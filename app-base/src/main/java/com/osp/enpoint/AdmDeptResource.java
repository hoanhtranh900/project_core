package com.osp.enpoint;

import com.osp.core.contants.ConstantAuthor;
import com.osp.core.contants.ConstantString;
import com.osp.core.contants.Constants;
import com.osp.core.controller.BaseControllerImpl;
import com.osp.core.dto.request.SearchForm;
import com.osp.core.dto.response.ResponseData;
import com.osp.core.entity.AdmDept;
import com.osp.core.entity.CmCommune;
import com.osp.core.entity.view.ViewAdmDept;
import com.osp.core.exception.BadRequestException;
import com.osp.core.exception.Result;
import com.osp.core.utils.JsonHelper;
import com.osp.service.AdmDeptService;
import com.osp.service.impl.AdmDeptServiceImpl;
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
@RequestMapping(value = "/api/v1/system/dept", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdmDeptResource extends BaseControllerImpl<AdmDept, AdmDeptServiceImpl> {
    protected AdmDeptResource(AdmDeptServiceImpl service, MessageSource messageSource) {
        super(service, ConstantAuthor.Dept.view, messageSource);
    }

    @Autowired private AdmDeptService<AdmDept> deptService;
    @Autowired private MessageSource messageSource;

    @ApiOperation(response = AdmDept.class, notes = Constants.NOTE_API + "empty_note", value = "Danh sách phòng ban cấp 1", authorizations = {@Authorization(value = Constants.API_KEY)})
    @GetMapping(value = "/loadListParent")
    @Secured({ConstantAuthor.Dept.view, ConstantAuthor.Dept.add, ConstantAuthor.Dept.edit})
    public ResponseEntity<ResponseData> getListParent() {
        return new ResponseEntity(new ResponseData(deptService.findByParentIdAndStatusAndIsDelete(0L, ConstantString.STATUS.active, ConstantString.IS_DELETE.active), Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = AdmDept.class, notes = Constants.NOTE_API + "empty_note", value = "Danh sách phòng ban cấp 2 theo id cấp 1", authorizations = {@Authorization(value = Constants.API_KEY)})
    @GetMapping(value = "/loadChildrenByParentId")
    @Secured({ConstantAuthor.Dept.view, ConstantAuthor.Dept.add, ConstantAuthor.Dept.edit})
    public ResponseEntity<ResponseData> loadChildrenByParentId(@RequestParam(value = "parentId") Long parentId) {
        return new ResponseEntity(new ResponseData(deptService.findByParentIdAndStatusInAndIsDelete(parentId,  Arrays.asList(ConstantString.STATUS.active, ConstantString.STATUS.lock), ConstantString.IS_DELETE.active), Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = AdmDept.class, notes = Constants.NOTE_API + "empty_note", value = "Danh sách phòng ban", authorizations = {@Authorization(value = Constants.API_KEY)})
    @GetMapping(value = "/getPage")
    @Secured({ConstantAuthor.Dept.view})
    public ResponseEntity<ResponseData> getPage(Pageable pageable, @ApiParam(value = Constants.NOTE_API_PAGEABLE) @RequestParam(value = "search") String search) {
        SearchForm searchObject = JsonHelper.jsonToObject(search, SearchForm.class);
        Map admDeptView = new HashMap();
        Page<ViewAdmDept> pages = deptService.getPage(searchObject, pageable);
        admDeptView.put("page", pages);
        admDeptView.put("listParent", deptService.findByParentIdAndStatusAndIsDelete(0L, ConstantString.STATUS.active, ConstantString.IS_DELETE.active));
        return new ResponseEntity<>(new ResponseData<>(admDeptView, Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = CmCommune.class, notes = Constants.NOTE_API + "empty_note", value = "khởi tạo thêm mới", authorizations = {@Authorization(value = Constants.API_KEY)})
    @GetMapping(value = "/initAdd")
    @Secured({ConstantAuthor.Commune.add})
    public ResponseEntity<ResponseData> initAdd() throws BadRequestException {
        return new ResponseEntity<>(new ResponseData<>(deptService.initAddOrEdit(null), Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = AdmDept.class, notes = Constants.NOTE_API + "empty_note", value = "Thêm mới phòng ban", authorizations = {@Authorization(value = Constants.API_KEY)})
    @PostMapping
    @Secured({ConstantAuthor.Dept.add})
    public ResponseEntity<ResponseData> add(@RequestBody AdmDept dto) {
        return new ResponseEntity<>(new ResponseData<>(deptService.save(dto), Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = AdmDept.class, notes = Constants.NOTE_API + "empty_note", value = "Chi tiết phòng ban theo ID", authorizations = {@Authorization(value = Constants.API_KEY)})
    @GetMapping(value = "/initEdit/{id}")
    @Secured({ConstantAuthor.Dept.edit})
    public ResponseEntity<ResponseData> initEdit(@PathVariable("id") Long id) throws BadRequestException {
        return new ResponseEntity<>(new ResponseData<>(deptService.initAddOrEdit(id), Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = AdmDept.class, notes = Constants.NOTE_API + "empty_note", value = "Sửa phòng ban", authorizations = {@Authorization(value = Constants.API_KEY)})
    @PutMapping
    @Secured({ConstantAuthor.Dept.edit})
    public ResponseEntity<ResponseData> edit(@RequestBody AdmDept dto) throws Throwable {
        return new ResponseEntity<>(new ResponseData<>(deptService.edit(dto), Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = AdmDept[].class, notes = Constants.NOTE_API + "empty_note", value = "Xóa phòng ban", authorizations = {@Authorization(value = Constants.API_KEY)})
    @PostMapping(path = "/delete")
    @Secured({ConstantAuthor.Dept.delete})
    public ResponseEntity<ResponseData> deleteByIds(@RequestBody List<Long> ids) {
        return new ResponseEntity<>(new ResponseData<>(deptService.deleteByIds(ids), Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = AdmDept[].class, notes = Constants.NOTE_API + "empty_note", value = "Khóa phòng ban", authorizations = {@Authorization(value = Constants.API_KEY)})
    @PostMapping(path = "/lock")
    @Secured({ConstantAuthor.Dept.edit})
    public ResponseEntity<ResponseData> locks(@RequestBody List<Long> ids) {
        return new ResponseEntity<>(new ResponseData<>(deptService.locks(ids), Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = AdmDept[].class, notes = Constants.NOTE_API + "empty_note", value = "Mở khóa phòng ban", authorizations = {@Authorization(value = Constants.API_KEY)})
    @PostMapping(path = "/unlock")
    @Secured({ConstantAuthor.Dept.edit})
    public ResponseEntity<ResponseData> unlocks(@RequestBody List<Long> ids) {
        return new ResponseEntity<>(new ResponseData<>(deptService.unlocks(ids), Result.SUCCESS), HttpStatus.OK);
    }

}
