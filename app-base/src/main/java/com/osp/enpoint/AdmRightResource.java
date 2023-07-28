package com.osp.enpoint;

import com.osp.core.contants.Constants;
import com.osp.core.entity.AdmUser;
import com.osp.core.exception.BadRequestException;
import com.osp.core.exception.BaseException;
import com.osp.core.contants.ConstantAuthor;
import com.osp.core.contants.ConstantString;
import com.osp.core.controller.BaseControllerImpl;
import com.osp.core.dto.response.ResponseData;
import com.osp.core.dto.request.SearchForm;
import com.osp.core.exception.Result;
import com.osp.core.utils.JsonHelper;
import com.osp.core.utils.UtilsCommon;
import com.osp.core.entity.AdmRight;
import com.osp.service.AdmRightService;
import com.osp.service.impl.AdmRightServiceImpl;
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

import java.util.*;

@RestController
@RequestMapping(value = "/api/v1/system/right", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdmRightResource extends BaseControllerImpl<AdmRight, AdmRightServiceImpl> {
    protected AdmRightResource(AdmRightServiceImpl service, MessageSource messageSource) {
        super(service, ConstantAuthor.Right.view, messageSource);
    }

    @Autowired private AdmRightService<AdmRight> admRightService;
    @Autowired private MessageSource messageSource;

    @ApiOperation(response = AdmRight.class, notes = Constants.NOTE_API + "empty_note", value = "Danh sách menu", authorizations = {@Authorization(value = Constants.API_KEY)})
    @GetMapping(value = "/getPage")
    @Secured({ConstantAuthor.Right.view})
    public ResponseEntity<ResponseData> getPage(Pageable pageable, @ApiParam(value = Constants.NOTE_API_PAGEABLE) @RequestParam(value = "search") String search) {
        SearchForm searchObject = JsonHelper.jsonToObject(search, SearchForm.class);
        Page<AdmRight> pages = admRightService.getPage(searchObject, pageable);
        ResponseData<Page<AdmRight>> response = new ResponseData<>(pages, Result.SUCCESS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(response = AdmRight.class, notes = Constants.NOTE_API + "empty_note", value = "Danh sách menu cấp 1", authorizations = {@Authorization(value = Constants.API_KEY)})
    @GetMapping(value = "/loadListParent")
    @Secured({ConstantAuthor.Right.view, ConstantAuthor.Right.add, ConstantAuthor.Right.edit})
    public ResponseEntity<ResponseData> getListParent() {
        return new ResponseEntity<>(new ResponseData<>(admRightService.loadListParent(), Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = AdmRight.class, notes = Constants.NOTE_API + "empty_note", value = "Danh sách cấp 2 theo parentId", authorizations = {@Authorization(value = Constants.API_KEY)})
    @GetMapping(value = "/loadChildrenByParentId")
    @Secured({ConstantAuthor.Dept.view, ConstantAuthor.Dept.add, ConstantAuthor.Dept.edit})
    public ResponseEntity<ResponseData> loadChildrenByParentId(@RequestParam(value = "parentId") Long parentId) {
        return new ResponseEntity<>(new ResponseData<>(admRightService.findByParentIdAndStatusInAndIsDelete(parentId,  Arrays.asList(ConstantString.STATUS.active, ConstantString.STATUS.lock), ConstantString.IS_DELETE.active), Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = AdmRight.class, notes = Constants.NOTE_API + "empty_note", value = "khởi tạo thêm mới", authorizations = {@Authorization(value = Constants.API_KEY)})
    @GetMapping(value = "/initAdd")
    @Secured({ConstantAuthor.Right.add})
    public ResponseEntity<ResponseData> initAdd() {
        return new ResponseEntity<>(new ResponseData<>(admRightService.loadListParent(), Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = AdmRight.class, notes = Constants.NOTE_API + "empty_note", value = "Thêm mới menu", authorizations = {@Authorization(value = Constants.API_KEY)})
    @PostMapping
    @Secured({ConstantAuthor.Right.add})
    public ResponseEntity<ResponseData> add(@RequestBody AdmRight dto) {
        return new ResponseEntity<>(new ResponseData<>(admRightService.save(dto), Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = AdmRight.class, notes = Constants.NOTE_API + "empty_note", value = "Chi tiết menu theo ID", authorizations = {@Authorization(value = Constants.API_KEY)})
    @GetMapping(value = "/initEdit/{id}")
    @Secured({ConstantAuthor.Right.edit})
    public ResponseEntity<ResponseData> initEdit(@PathVariable("id") Long id) throws BadRequestException {
        Optional<AdmRight> right = admRightService.get(id);
        if (!right.isPresent()) {
            throw new BadRequestException( messageSource.getMessage("error.ENTITY_NOT_FOUND", new Object[]{"Menu"}, UtilsCommon.getLocale()));
        }
        List<AdmRight> listParent = admRightService.loadListParent();
        Map response = new HashMap();
        response.put("listParent", listParent);
        response.put("right", right);
        return new ResponseEntity<>(new ResponseData<>(response, Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = AdmRight.class, notes = Constants.NOTE_API + "empty_note", value = "Chỉnh sửa menu", authorizations = {@Authorization(value = Constants.API_KEY)})
    @PutMapping
    @Secured({ConstantAuthor.Right.edit})
    public ResponseEntity<ResponseData> edit(@RequestBody AdmRight dto) throws Throwable {
        AdmRight right = admRightService.get(dto.getId()).orElseThrow(() -> new BaseException(
                messageSource.getMessage("error.ENTITY_NOT_FOUND", new Object[]{"Menu"}, UtilsCommon.getLocale())
        ));
        return new ResponseEntity<>(new ResponseData<>(admRightService.save(dto.formToBo(dto, right)), Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = AdmRight[].class, notes = Constants.NOTE_API + "empty_note", value = "Xóa menu", authorizations = {@Authorization(value = Constants.API_KEY)})
    @PostMapping(path = "/delete")
    @Secured({ConstantAuthor.Right.delete})
    public ResponseEntity<ResponseData> deleteByIds(@RequestBody List<Long> ids) {
        return new ResponseEntity<>(new ResponseData<>(admRightService.deleteByIds(ids), Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = AdmRight[].class, notes = Constants.NOTE_API + "empty_note", value = "Khóa menu", authorizations = {@Authorization(value = Constants.API_KEY)})
    @PostMapping(path = "/lock")
    @Secured({ConstantAuthor.Right.edit})
    public ResponseEntity<ResponseData> locks(@RequestBody List<Long> ids) {
        return new ResponseEntity<>(new ResponseData<>(admRightService.locks(ids), Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = AdmRight[].class, notes = Constants.NOTE_API + "empty_note", value = "Mở khóa menu", authorizations = {@Authorization(value = Constants.API_KEY)})
    @PostMapping(path = "/unlock")
    @Secured({ConstantAuthor.Right.edit})
    public ResponseEntity<ResponseData> unlocks(@RequestBody List<Long> ids) {
        return new ResponseEntity<>(new ResponseData<>(admRightService.unlocks(ids), Result.SUCCESS), HttpStatus.OK);
    }
}
