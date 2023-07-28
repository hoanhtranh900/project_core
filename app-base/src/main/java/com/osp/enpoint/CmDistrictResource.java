package com.osp.enpoint;

import com.osp.core.contants.ConstantAuthor;
import com.osp.core.contants.Constants;
import com.osp.core.controller.BaseControllerImpl;
import com.osp.core.dto.request.SearchForm;
import com.osp.core.dto.response.ResponseData;
import com.osp.core.entity.CmDistrict;
import com.osp.core.exception.BadRequestException;
import com.osp.core.exception.Result;
import com.osp.core.utils.JsonHelper;
import com.osp.service.CmDistrictService;
import com.osp.service.impl.CmDistrictServiceImpl;
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

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/system/district", produces = MediaType.APPLICATION_JSON_VALUE)
public class CmDistrictResource extends BaseControllerImpl<CmDistrict, CmDistrictServiceImpl> {
    protected CmDistrictResource(CmDistrictServiceImpl service, MessageSource messageSource) {
        super(service, ConstantAuthor.District.view, messageSource);
    }

    @Autowired private CmDistrictService<CmDistrict> districtService;
    @Autowired private MessageSource messageSource;

    @ApiOperation(response = CmDistrict.class, notes = Constants.NOTE_API + "empty_note", value = "Danh sách quận huyện", authorizations = {@Authorization(value = Constants.API_KEY)})
    @GetMapping(value = "/getPage")
    @Secured({ConstantAuthor.District.view})
    public ResponseEntity<ResponseData> getPage(Pageable pageable, @ApiParam(value = Constants.NOTE_API_PAGEABLE) @RequestParam(value = "search") String search) {
        SearchForm searchObject = JsonHelper.jsonToObject(search, SearchForm.class);
        Page<CmDistrict> pages = districtService.getPage(searchObject, pageable);
        ResponseData<Page<CmDistrict>> response = new ResponseData<>(pages, Result.SUCCESS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(response = CmDistrict.class, notes = Constants.NOTE_API + "empty_note", value = "khởi tạo thêm mới", authorizations = {@Authorization(value = Constants.API_KEY)})
    @GetMapping(value = "/initAdd")
    @Secured({ConstantAuthor.District.add})
    public ResponseEntity<ResponseData> initAdd() throws BadRequestException {
        return new ResponseEntity<>(new ResponseData<>(districtService.initAddOrEdit(null), Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = CmDistrict.class, notes = Constants.NOTE_API + "empty_note", value = "Thêm mới tỉnh thành", authorizations = {@Authorization(value = Constants.API_KEY)})
    @PostMapping
    @Secured({ConstantAuthor.District.add})
    public ResponseEntity<ResponseData> add(@RequestBody CmDistrict dto) {
        return new ResponseEntity<>(new ResponseData<>(districtService.save(dto), Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = CmDistrict.class, notes = Constants.NOTE_API + "empty_note", value = "Chi tiết theo ID", authorizations = {@Authorization(value = Constants.API_KEY)})
    @GetMapping(value = "/initEdit/{id}")
    @Secured({ConstantAuthor.District.edit})
    public ResponseEntity<ResponseData> initEdit(@PathVariable("id") Long id) throws BadRequestException {
        return new ResponseEntity<>(new ResponseData<>(districtService.initAddOrEdit(id), Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = CmDistrict.class, notes = Constants.NOTE_API + "empty_note", value = "Chỉnh sửa", authorizations = {@Authorization(value = Constants.API_KEY)})
    @PutMapping
    @Secured({ConstantAuthor.District.edit})
    public ResponseEntity<ResponseData> edit(@RequestBody CmDistrict dto) throws Throwable {
        return new ResponseEntity<>(new ResponseData<>(districtService.edit(dto), Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = CmDistrict[].class, notes = Constants.NOTE_API + "empty_note", value = "Xóa", authorizations = {@Authorization(value = Constants.API_KEY)})
    @PostMapping(path = "/delete")
    @Secured({ConstantAuthor.District.delete})
    public ResponseEntity<ResponseData> deleteByIds(@RequestBody List<Long> ids) {
        return new ResponseEntity<>(new ResponseData<>(districtService.deleteByIds(ids), Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = CmDistrict[].class, notes = Constants.NOTE_API + "empty_note", value = "Khóa", authorizations = {@Authorization(value = Constants.API_KEY)})
    @PostMapping(path = "/lock")
    @Secured({ConstantAuthor.District.edit})
    public ResponseEntity<ResponseData> locks(@RequestBody List<Long> ids) {
        return new ResponseEntity<>(new ResponseData<>(districtService.locks(ids), Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = CmDistrict[].class, notes = Constants.NOTE_API + "empty_note", value = "Mở khóa", authorizations = {@Authorization(value = Constants.API_KEY)})
    @PostMapping(path = "/unlock")
    @Secured({ConstantAuthor.District.edit})
    public ResponseEntity<ResponseData> unlocks(@RequestBody List<Long> ids) {
        return new ResponseEntity<>(new ResponseData<>(districtService.unlocks(ids), Result.SUCCESS), HttpStatus.OK);
    }
}
