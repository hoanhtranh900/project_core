
package com.osp.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.osp.core.contants.ConstantString;
import com.osp.core.utils.H;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "T_ADM_USERS", indexes = {
        @Index(name = "USER_NAME", columnList = "USER_NAME", unique = true),
        @Index(name = "STATUS", columnList = "STATUS", unique = false)
})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AdmUser extends Auditable implements Serializable, UserDetails {

    public AdmUser(String username, String password, List<GrantedAuthority> authorities) {
        this.username=username;
        this.password=password;
        this.grantedAuths=authorities;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "users")
    private List<AdmUserType> typeUsers;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
    private AdmUserSession sessions;

    @ManyToMany(mappedBy = "groupUsers", fetch = FetchType.LAZY)
    private List<AdmGroup> groups;

    @ManyToMany(mappedBy = "deptUsers", fetch = FetchType.LAZY)
    private List<AdmDept> depts;

    @ManyToOne
    @JoinColumn(name = "PROVINCE_ID")
    private CmProvince province;

    @ManyToOne
    @JoinColumn(name = "DISTRICT_ID")
    private CmDistrict district;

    @ManyToOne
    @JoinColumn(name = "COMMUNE_ID")
    private CmCommune commune;

    @Comment("Tên đăng nhập")
    @Column(name = "USER_NAME", length = 100, unique = true)
    private String username;

    @Comment("Địa chỉ chi tiết")
    @Column(name = "ADDRESS", length = 200)
    private String address;

    @Comment("Mật khẩu")
    @JsonIgnore
    @Column(name = "PASSWORD", length = 300)
    private String password;

    @Comment("Số điện thoại")
    @Column(name = "PHONE_NUMBER", length = 100)
    private String phoneNumber;

    @Comment("Họ")
    @Column(name = "SURNAME", length = 100)
    private String surname;

    @Comment("Tên")
    @Column(name = "GIVEN_NAME", length = 100)
    private String givenName;

    @Comment("Tên đầy đủ")
    @Column(name = "FULL_NAME", length = 300)
    private String fullName;

    @Comment("Trạng thái hoạt động")
    @Column(name = "STATUS", columnDefinition = "bigint default 0")
    private Long status = ConstantString.STATUS.active;

    @Comment("Trạng thái xóa")
    @Column(name = "IS_DELETE", columnDefinition = "bigint default 0")
    private Long isDelete = ConstantString.IS_DELETE.active;

    @Comment("EMAIL")
    @Column(name = "EMAIL", unique = true, length = 50)
    private String email;

    @Transient
    private String ipAddress;
    @Transient
    private String rePassWord;
    @Transient
    private String oldPassWord;
    @Transient
    private List<GrantedAuthority> grantedAuths;
    @Transient
    private String statusStr;
    @Transient
    private String isDeleteStr;
    @Transient
    private String addressFull;

    public String getStatusStr() {
        return ConstantString.STATUS.getStatusStr(this.status);
    }

    public String getIsDeleteStr() {
        return ConstantString.IS_DELETE.getStatusStr(this.isDelete);
    }

    public String getAddressFull() {
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

    //update
    public AdmUser formToBo(AdmUser form, AdmUser bo) {
        bo.setPhoneNumber(form.getPhoneNumber());
        bo.setSurname(form.getSurname());
        bo.setGivenName(form.getGivenName());
        bo.setFullName(form.getFullName());
        bo.setStatus(form.getStatus());
        bo.setEmail(form.getEmail());
        return bo;
    }

    public AdmUser updateProfile(AdmUser form, AdmUser bo) {
        bo.setPhoneNumber(form.getPhoneNumber());
        bo.setSurname(form.getSurname());
        bo.setGivenName(form.getGivenName());
        bo.setFullName(form.getFullName());
        bo.setStatus(form.getStatus());
        bo.setEmail(form.getEmail());
        return bo;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuths;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.isDelete != null && this.isDelete.compareTo(ConstantString.IS_DELETE.active) == 0;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.status != null && this.status.compareTo(ConstantString.STATUS.active) == 0;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.status != null && this.status.compareTo(ConstantString.STATUS.active) == 0;
    }
}
