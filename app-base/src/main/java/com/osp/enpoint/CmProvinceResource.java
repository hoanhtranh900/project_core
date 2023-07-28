package com.osp.enpoint;

import com.osp.core.contants.ConstantAuthor;
import com.osp.core.contants.Constants;
import com.osp.core.controller.BaseControllerImpl;
import com.osp.core.dto.request.SearchForm;
import com.osp.core.dto.response.ResponseData;
import com.osp.core.entity.CmProvince;
import com.osp.core.exception.BadRequestException;
import com.osp.core.exception.Result;
import com.osp.core.utils.JsonHelper;
import com.osp.service.CmProvinceService;
import com.osp.service.impl.CmProvinceServiceImpl;
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
@RequestMapping(value = "/api/v1/system/province", produces = MediaType.APPLICATION_JSON_VALUE)
public class CmProvinceResource extends BaseControllerImpl<CmProvince, CmProvinceServiceImpl> {
    protected CmProvinceResource(CmProvinceServiceImpl service, MessageSource messageSource) {
        super(service, ConstantAuthor.Province.view, messageSource);
    }

    @Autowired private CmProvinceService<CmProvince> provinceService;
    @Autowired private MessageSource messageSource;

    @ApiOperation(response = CmProvince.class, notes = Constants.NOTE_API + "empty_note", value = "Danh sách tỉnh thành", authorizations = {@Authorization(value = Constants.API_KEY)})
    @GetMapping(value = "/getPage")
    @Secured({ConstantAuthor.Province.view})
    public ResponseEntity<ResponseData> getPage(Pageable pageable, @ApiParam(value = Constants.NOTE_API_PAGEABLE) @RequestParam(value = "search") String search) {
        SearchForm searchObject = JsonHelper.jsonToObject(search, SearchForm.class);
        Page<CmProvince> pages = provinceService.getPage(searchObject, pageable);
        ResponseData<Page<CmProvince>> response = new ResponseData<>(pages, Result.SUCCESS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(response = CmProvince.class, notes = Constants.NOTE_API + "empty_note", value = "khởi tạo thêm mới", authorizations = {@Authorization(value = Constants.API_KEY)})
    @GetMapping(value = "/initAdd")
    @Secured({ConstantAuthor.Province.add})
    public ResponseEntity<ResponseData> initAdd() throws BadRequestException {
        return new ResponseEntity<>(new ResponseData<>(provinceService.initAddOrEdit(null), Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = CmProvince.class, notes = Constants.NOTE_API + "empty_note", value = "Thêm mới tỉnh thành", authorizations = {@Authorization(value = Constants.API_KEY)})
    @PostMapping
    @Secured({ConstantAuthor.Province.add})
    public ResponseEntity<ResponseData> add(@RequestBody CmProvince dto) {
        return new ResponseEntity<>(new ResponseData<>(provinceService.save(dto), Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = CmProvince.class, notes = Constants.NOTE_API + "empty_note", value = "Chi tiết theo ID", authorizations = {@Authorization(value = Constants.API_KEY)})
    @GetMapping(value = "/initEdit/{id}")
    @Secured({ConstantAuthor.Province.edit})
    public ResponseEntity<ResponseData> initEdit(@PathVariable("id") Long id) throws BadRequestException {
        return new ResponseEntity<>(new ResponseData<>(provinceService.initAddOrEdit(id), Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = CmProvince.class, notes = Constants.NOTE_API + "empty_note", value = "Chỉnh sửa", authorizations = {@Authorization(value = Constants.API_KEY)})
    @PutMapping
    @Secured({ConstantAuthor.Province.edit})
    public ResponseEntity<ResponseData> edit(@RequestBody CmProvince dto) throws Throwable {
        return new ResponseEntity<>(new ResponseData<>(provinceService.edit(dto), Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = CmProvince[].class, notes = Constants.NOTE_API + "empty_note", value = "Xóa", authorizations = {@Authorization(value = Constants.API_KEY)})
    @PostMapping(path = "/delete")
    @Secured({ConstantAuthor.Province.delete})
    public ResponseEntity<ResponseData> deleteByIds(@RequestBody List<Long> ids) {
        return new ResponseEntity<>(new ResponseData<>(provinceService.deleteByIds(ids), Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = CmProvince[].class, notes = Constants.NOTE_API + "empty_note", value = "Khóa", authorizations = {@Authorization(value = Constants.API_KEY)})
    @PostMapping(path = "/lock")
    @Secured({ConstantAuthor.Province.edit})
    public ResponseEntity<ResponseData> locks(@RequestBody List<Long> ids) {
        return new ResponseEntity<>(new ResponseData<>(provinceService.locks(ids), Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = CmProvince[].class, notes = Constants.NOTE_API + "empty_note", value = "Mở khóa", authorizations = {@Authorization(value = Constants.API_KEY)})
    @PostMapping(path = "/unlock")
    @Secured({ConstantAuthor.Province.edit})
    public ResponseEntity<ResponseData> unlocks(@RequestBody List<Long> ids) {
        return new ResponseEntity<>(new ResponseData<>(provinceService.unlocks(ids), Result.SUCCESS), HttpStatus.OK);
    }
}
