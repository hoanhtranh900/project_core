package com.osp.core.entity.only;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.osp.core.contants.CategoryBuffer;
import com.osp.core.contants.ConstantString;
import com.osp.core.entity.Auditable;
import com.osp.core.utils.H;
import lombok.*;
import org.hibernate.annotations.Subselect;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.io.Serializable;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Subselect("select * from T_ADM_USERS")
public class V_AdmUser extends Auditable implements Serializable {

    @Column(name = "PROVINCE_ID")
    private Long provinceId;

    @Column(name = "DISTRICT_ID")
    private Long districtId;

    @Column(name = "COMMUNE_ID")
    private Long communeId;

    @Column(name = "USER_NAME")
    private String username;

    @Column(name = "ADDRESS")
    private String address;

    @JsonIgnore
    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Column(name = "SURNAME")
    private String surname;

    @Column(name = "GIVEN_NAME")
    private String givenName;

    @Column(name = "FULL_NAME")
    private String fullName;

    @Column(name = "STATUS")
    private Long status;

    @Column(name = "IS_DELETE")
    private Long isDelete;

    @Column(name = "EMAIL", unique = true, length = 50)
    private String email;

    @Transient
    private String statusStr;
    @Transient
    private String isDeleteStr;
    @Transient
    private V_CmProvince province;
    @Transient
    private V_CmDistrict district;
    @Transient
    private V_CmCommune commune;
    @Transient
    private String addressFull;

    public String getStatusStr() {
        return ConstantString.STATUS.getStatusStr(this.status);
    }

    public String getIsDeleteStr() {
        return ConstantString.IS_DELETE.getStatusStr(this.isDelete);
    }

    public String getAddressFull() {
        commune = getCommune();
        district = getDistrict();
        province = getProvince();

        String str = address == null ? "" : address;
        if (commune != null) {
            if (H.isTrue(str))
                str += ", ";
            str += commune == null ? "" : commune.getCommuneName();
        }
        if (district != null) {
            if (H.isTrue(str))
                str += ", ";
            str += district == null ? "" : district.getDistrictName();
        }
        if (province != null) {
            if (H.isTrue(str))
                str += ", ";
            str += province == null ? "" : province.getProvinceName();
        }
        return str;
    }

    public V_CmProvince getProvince() {
        return CategoryBuffer.getProvinceById(provinceId);
    }

    public V_CmDistrict getDistrict() {
        return CategoryBuffer.getDistrictById(districtId);
    }

    public V_CmCommune getCommune() {
        return CategoryBuffer.getCommuneById(communeId);
    }
}
