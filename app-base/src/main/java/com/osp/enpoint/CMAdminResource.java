package com.osp.enpoint;

import com.osp.core.contants.ConstantString;
import com.osp.core.contants.Constants;
import com.osp.core.dto.response.ResponseData;
import com.osp.core.entity.AdmUser;
import com.osp.core.entity.CmCommune;
import com.osp.core.entity.CmDistrict;
import com.osp.core.exception.BadRequestException;
import com.osp.core.exception.Result;
import com.osp.service.AdmUserService;
import com.osp.service.CmCommuneService;
import com.osp.service.CmDistrictService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/common", produces = MediaType.APPLICATION_JSON_VALUE)
@ApiOperation(value = "Các api chung bắt buộc đã login hệ thống")
public class CMAdminResource {
    @Autowired private MessageSource messageSource;
    @Autowired private AdmUserService<AdmUser> admUserService;
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

    @ApiOperation(response = AdmUser.class, notes = Constants.NOTE_API + "empty_note", value = "Lấy profile", authorizations = {@Authorization(value = Constants.API_KEY)})
    @GetMapping(value = "/profile/{userId}")
    public ResponseEntity<ResponseData> profileById(@PathVariable Long userId) throws BadRequestException {
        return new ResponseEntity<>(new ResponseData(admUserService.profile(userId), Result.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(response = AdmUser.class, notes = Constants.NOTE_API + "empty_note", value = "Update Profile user", authorizations = {@Authorization(value = Constants.API_KEY)})
    @PostMapping(value = "/update-profile")
    public Optional<AdmUser> updateUserAvatar(@RequestBody AdmUser form) {
        return admUserService.updateProfile(form);
    }

    @ApiOperation(response = AdmUser.class, notes = Constants.NOTE_API + "empty_note", value = "Đổi mật khẩu user", authorizations = {@Authorization(value = Constants.API_KEY)})
    @PostMapping(value = "/change-pass")
    public Optional<AdmUser> changePass(@RequestBody AdmUser form) throws BadRequestException {
        return admUserService.changePass(form);
    }
}
