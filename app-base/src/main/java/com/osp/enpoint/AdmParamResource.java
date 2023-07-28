package com.osp.enpoint;

import com.osp.core.contants.ConstantAuthor;
import com.osp.core.contants.Constants;
import com.osp.core.controller.BaseControllerImpl;
import com.osp.core.dto.request.SearchForm;
import com.osp.core.dto.response.ResponseData;
import com.osp.core.entity.AdmParaSystem;
import com.osp.core.exception.BaseException;
import com.osp.core.exception.Result;
import com.osp.core.utils.JsonHelper;
import com.osp.core.utils.UtilsCommon;
import com.osp.service.AdmParaSystemService;
import com.osp.service.impl.AdmParaSystemServiceImpl;
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
@RequestMapping(value = "/api/v1/system/param-system", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdmParamResource extends BaseControllerImpl<AdmParaSystem, AdmParaSystemServiceImpl> {
    protected AdmParamResource(AdmParaSystemServiceImpl service, MessageSource messageSource) {
        super(service, ConstantAuthor.ParamSystem.view, messageSource);
    }

    @Autowired private AdmParaSystemService<AdmParaSystem> paraSystemService;
    @Autowired private MessageSource messageSource;

    @ApiOperation(response = AdmParaSystem[].class, notes = Constants.NOTE_API + "empty_note", value = "Danh sách", authorizations = {@Authorization(value = Constants.API_KEY)})
    @GetMapping(value = "/getPage")
    @Secured({ConstantAuthor.ParamSystem.view})
    public ResponseEntity<ResponseData> getPage(Pageable pageable, @ApiParam(value = Constants.NOTE_API_PAGEABLE) @RequestParam(value = "search") String search) {
        SearchForm searchObject = JsonHelper.jsonToObject(search, SearchForm.class);
        Page<AdmParaSystem> pages = paraSystemService.getPage(searchObject, pageable);
        return new ResponseEntity<>(new ResponseData<>(pages, Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = AdmParaSystem.class, notes = Constants.NOTE_API + "empty_note", value = "Thêm mới", authorizations = {@Authorization(value = Constants.API_KEY)})
    @PostMapping
    @Secured({ConstantAuthor.ParamSystem.add})
    public ResponseEntity<ResponseData> add(@RequestBody AdmParaSystem dto) {
        return new ResponseEntity<>(new ResponseData<>(paraSystemService.save(dto), Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = AdmParaSystem.class, notes = Constants.NOTE_API + "empty_note", value = "Chi tiết chỉnh sửa theo ID", authorizations = {@Authorization(value = Constants.API_KEY)})
    @GetMapping(value = "/initEdit/{id}")
    @Secured({ConstantAuthor.ParamSystem.edit})
    public ResponseEntity<ResponseData> initEdit(@PathVariable("id") Long id) {
        return new ResponseEntity<>(new ResponseData<>(paraSystemService.get(id), Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = AdmParaSystem.class, notes = Constants.NOTE_API + "empty_note", value = "Chỉnh sửa", authorizations = {@Authorization(value = Constants.API_KEY)})
    @PutMapping
    @Secured({ConstantAuthor.ParamSystem.edit})
    public ResponseEntity<ResponseData> edit(@RequestBody AdmParaSystem dto) {
        AdmParaSystem paraSystem = paraSystemService.get(dto.getId()).orElseThrow(() -> new BaseException(
                messageSource.getMessage("error.ENTITY_NOT_FOUND", new Object[]{"ParaSystem"}, UtilsCommon.getLocale())
        ));
        return new ResponseEntity<>(new ResponseData<>(paraSystemService.save(dto.formToBo(dto, paraSystem)), Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = AdmParaSystem.class, notes = Constants.NOTE_API + "empty_note", value = "Xóa", authorizations = {@Authorization(value = Constants.API_KEY)})
    @PostMapping(path = "/delete")
    @Secured({ConstantAuthor.ParamSystem.delete})
    public ResponseEntity<ResponseData> deleteByIds(@RequestBody List<Long> ids) {
        return new ResponseEntity<>(new ResponseData<>(paraSystemService.deleteByIds(ids), Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = AdmParaSystem.class, notes = Constants.NOTE_API + "empty_note", value = "Khóa", authorizations = {@Authorization(value = Constants.API_KEY)})
    @PostMapping(path = "/lock")
    @Secured({ConstantAuthor.ParamSystem.edit})
    public ResponseEntity<ResponseData> locks(@RequestBody List<Long> ids) {
        return new ResponseEntity<>(new ResponseData<>(paraSystemService.locks(ids), Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = AdmParaSystem.class, notes = Constants.NOTE_API + "empty_note", value = "Mở khóa", authorizations = {@Authorization(value = Constants.API_KEY)})
    @PostMapping(path = "/unlock")
    @Secured({ConstantAuthor.ParamSystem.edit})
    public ResponseEntity<ResponseData> unlocks(@RequestBody List<Long> ids) {
        return new ResponseEntity<>(new ResponseData<>(paraSystemService.unlocks(ids), Result.SUCCESS), HttpStatus.OK);
    }

}
