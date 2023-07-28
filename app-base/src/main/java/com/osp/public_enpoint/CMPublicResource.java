package com.osp.public_enpoint;

import com.osp.core.contants.ConstantString;
import com.osp.core.contants.Constants;
import com.osp.core.dto.response.ResponseData;
import com.osp.core.entity.CmCommune;
import com.osp.core.entity.CmDistrict;
import com.osp.core.exception.BadRequestException;
import com.osp.core.exception.Result;
import com.osp.service.CmCommuneService;
import com.osp.service.CmDistrictService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/public/v1/common", produces = MediaType.APPLICATION_JSON_VALUE)
@ApiOperation(value = "Các api chung không bắt buộc login hệ thống")
public class CMPublicResource {
    @Autowired private MessageSource messageSource;
    @Autowired private CmDistrictService<CmDistrict> districtService;
    @Autowired private CmCommuneService<CmCommune> communeService;

    @ApiOperation(response = CmDistrict.class, notes = Constants.NOTE_API + "empty_note", value = "Lấy các hằng số API", authorizations = {@Authorization(value = Constants.API_KEY)})
    @GetMapping(value = "/constants")
    public ResponseEntity<ResponseData> constant() throws BadRequestException {
        return new ResponseEntity<>(new ResponseData<>(ConstantString.map, Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = CmDistrict.class, notes = Constants.NOTE_API + "empty_note", value = "Lấy danh sách Quận huyện theo tỉnh thành", authorizations = {@Authorization(value = Constants.API_KEY)})
    @GetMapping(value = "/district-by-province/{provinceId}")
    public ResponseEntity<ResponseData> districtByProvince(@PathVariable Long provinceId) throws BadRequestException {
        return new ResponseEntity<>(new ResponseData<>(districtService.districtByProvince(provinceId), Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = CmDistrict.class, notes = Constants.NOTE_API + "empty_note", value = "Lấy danh sách phường xã theo quận huyện", authorizations = {@Authorization(value = Constants.API_KEY)})
    @GetMapping(value = "/commune-by-district/{districtId}")
    public ResponseEntity<ResponseData> communeByDistrict(@PathVariable Long districtId) throws BadRequestException {
        return new ResponseEntity<>(new ResponseData<>(communeService.communeByDistrict(districtId), Result.SUCCESS), HttpStatus.OK);
    }
}
