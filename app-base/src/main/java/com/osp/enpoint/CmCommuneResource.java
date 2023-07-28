package com.osp.enpoint;

import com.osp.core.contants.ConstantAuthor;
import com.osp.core.contants.Constants;
import com.osp.core.controller.BaseControllerImpl;
import com.osp.core.dto.request.SearchForm;
import com.osp.core.dto.response.ResponseData;
import com.osp.core.entity.CmCommune;
import com.osp.core.exception.BadRequestException;
import com.osp.core.exception.Result;
import com.osp.core.utils.JsonHelper;
import com.osp.service.CmCommuneService;
import com.osp.service.impl.CmCommuneServiceImpl;
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
@RequestMapping(value = "/api/v1/system/commune", produces = MediaType.APPLICATION_JSON_VALUE)
public class CmCommuneResource extends BaseControllerImpl<CmCommune, CmCommuneServiceImpl> {
    protected CmCommuneResource(CmCommuneServiceImpl service, MessageSource messageSource) {
        super(service, ConstantAuthor.Commune.view, messageSource);
    }

    @Autowired private CmCommuneService<CmCommune> communeService;
    @Autowired private MessageSource messageSource;

    @ApiOperation(response = CmCommune.class, notes = Constants.NOTE_API + "empty_note", value = "Danh sách phường xã", authorizations = {@Authorization(value = Constants.API_KEY)})
    @GetMapping(value = "/getPage")
    @Secured({ConstantAuthor.Commune.view})
    public ResponseEntity<ResponseData> getPage(Pageable pageable, @ApiParam(value = Constants.NOTE_API_PAGEABLE) @RequestParam(value = "search") String search) {
        SearchForm searchObject = JsonHelper.jsonToObject(search, SearchForm.class);
        Page<CmCommune> pages = communeService.getPage(searchObject, pageable);
        ResponseData<Page<CmCommune>> response = new ResponseData<>(pages, Result.SUCCESS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(response = CmCommune.class, notes = Constants.NOTE_API + "empty_note", value = "khởi tạo thêm mới", authorizations = {@Authorization(value = Constants.API_KEY)})
    @GetMapping(value = "/initAdd")
    @Secured({ConstantAuthor.Commune.add})
    public ResponseEntity<ResponseData> initAdd() throws BadRequestException {
        return new ResponseEntity<>(new ResponseData<>(communeService.initAddOrEdit(null), Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = CmCommune.class, notes = Constants.NOTE_API + "empty_note", value = "Thêm mới phường xã", authorizations = {@Authorization(value = Constants.API_KEY)})
    @PostMapping
    @Secured({ConstantAuthor.Commune.add})
    public ResponseEntity<ResponseData> add(@RequestBody CmCommune dto) {
        return new ResponseEntity<>(new ResponseData<>(communeService.save(dto), Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = CmCommune.class, notes = Constants.NOTE_API + "empty_note", value = "Chi tiết theo ID", authorizations = {@Authorization(value = Constants.API_KEY)})
    @GetMapping(value = "/initEdit/{id}")
    @Secured({ConstantAuthor.Commune.edit})
    public ResponseEntity<ResponseData> initEdit(@PathVariable("id") Long id) throws BadRequestException {
        return new ResponseEntity<>(new ResponseData<>(communeService.initAddOrEdit(id), Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = CmCommune.class, notes = Constants.NOTE_API + "empty_note", value = "Chỉnh sửa", authorizations = {@Authorization(value = Constants.API_KEY)})
    @PutMapping
    @Secured({ConstantAuthor.Commune.edit})
    public ResponseEntity<ResponseData> edit(@RequestBody CmCommune dto) throws Throwable {
        return new ResponseEntity<>(new ResponseData<>(communeService.edit(dto), Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = CmCommune[].class, notes = Constants.NOTE_API + "empty_note", value = "Xóa", authorizations = {@Authorization(value = Constants.API_KEY)})
    @PostMapping(path = "/delete")
    @Secured({ConstantAuthor.Commune.delete})
    public ResponseEntity<ResponseData> deleteByIds(@RequestBody List<Long> ids) {
        return new ResponseEntity<>(new ResponseData<>(communeService.deleteByIds(ids), Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = CmCommune[].class, notes = Constants.NOTE_API + "empty_note", value = "Khóa", authorizations = {@Authorization(value = Constants.API_KEY)})
    @PostMapping(path = "/lock")
    @Secured({ConstantAuthor.Commune.edit})
    public ResponseEntity<ResponseData> locks(@RequestBody List<Long> ids) {
        return new ResponseEntity<>(new ResponseData<>(communeService.locks(ids), Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = CmCommune[].class, notes = Constants.NOTE_API + "empty_note", value = "Mở khóa", authorizations = {@Authorization(value = Constants.API_KEY)})
    @PostMapping(path = "/unlock")
    @Secured({ConstantAuthor.Commune.edit})
    public ResponseEntity<ResponseData> unlocks(@RequestBody List<Long> ids) {
        return new ResponseEntity<>(new ResponseData<>(communeService.unlocks(ids), Result.SUCCESS), HttpStatus.OK);
    }
}
