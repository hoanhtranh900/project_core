
package com.osp.core.entity.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.osp.core.contants.CategoryBuffer;
import com.osp.core.contants.ConstantString;
import com.osp.core.entity.Auditable;
import com.osp.core.entity.only.*;
import com.osp.core.utils.H;
import lombok.*;
import org.hibernate.annotations.Subselect;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/*
* Tạo view bằng @Subselect
* */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Subselect("select * from ADM_USERS")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ViewAdmUser extends Auditable implements Serializable {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "users")
    private List<ViewAdmUserType> typeUsers;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID", referencedColumnName="USER_ID", nullable = true)
    private V_AdmUserSession sessions;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "ADM_GROUP_USER",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "GROUP_ID"))
    private List<V_AdmGroup> groups;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "ADM_DEPT_USER",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "DEPT_ID"))
    private List<V_AdmDept> depts;

    @Column(name = "PROVINCE_ID")
    private Long provinceId;

    @Column(name = "DISTRICT_ID")
    private Long districtId;

    @Column(name = "COMMUNE_ID")
    private Long communeId;

    @Column(name = "USER_NAME")
    private String username;

    @JsonIgnore
    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "ADDRESS")
    private String address;

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

    @Column(name = "EMAIL")
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
