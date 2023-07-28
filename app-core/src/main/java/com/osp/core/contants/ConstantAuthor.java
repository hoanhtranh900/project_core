package com.osp.core.contants;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ConstantAuthor {

    public static boolean contain(String role, Authentication auth) {
        System.out.println("ConstantAuthor|contain(" + role + ")");
        List<String> user_root = new ArrayList<>(Arrays.asList(Constants.supper_admin.split(",")));
        if (user_root.contains(auth.getName())) return true;
        Collection<? extends GrantedAuthority> checkRight = auth.getAuthorities();
        boolean authorized = checkRight.contains(new SimpleGrantedAuthority(role));
        if (!authorized) {
            return false;
        }
        return true;
    }

    public interface Ckfinder { // quản lý file
        String author = "ROLE_SYSTEM_CKFINDER";
        String view = "ROLE_SYSTEM_CKFINDER_VIEW";
        String add = "ROLE_SYSTEM_CKFINDER_ADD";
        String edit = "ROLE_SYSTEM_CKFINDER_EDIT";
        String delete = "ROLE_SYSTEM_CKFINDER_DELETE";
    }

    public interface Dept { // phòng ban
        String author = "ROLE_SYSTEM_DEPT";
        String view = "ROLE_SYSTEM_DEPT_VIEW";
        String add = "ROLE_SYSTEM_DEPT_ADD";
        String edit = "ROLE_SYSTEM_DEPT_EDIT";
        String delete = "ROLE_SYSTEM_DEPT_DELETE";
    }

    public interface User { // người dùng
        String author = "ROLE_SYSTEM_USER";
        String view = "ROLE_SYSTEM_USER_VIEW";
        String add = "ROLE_SYSTEM_USER_ADD";
        String edit = "ROLE_SYSTEM_USER_EDIT";
        String delete = "ROLE_SYSTEM_USER_DELETE";
    }

    public interface Group { // nhóm quyền
        String author = "ROLE_SYSTEM_GROUP";
        String view = "ROLE_SYSTEM_GROUP_VIEW";
        String add = "ROLE_SYSTEM_GROUP_ADD";
        String edit = "ROLE_SYSTEM_GROUP_EDIT";
        String delete = "ROLE_SYSTEM_GROUP_DELETE";
    }

    public interface Authority { //Quyền
        String author = "ROLE_SYSTEM_AUTHORITY";
        String view = "ROLE_SYSTEM_AUTHORITY_VIEW";
        String edit = "ROLE_SYSTEM_AUTHORITY_EDIT";
        String add = "ROLE_SYSTEM_AUTHORITY_ADD";
        String delete = "ROLE_SYSTEM_AUTHORITY_DELETE";
    }

    public interface Right { //menu
        String author = "ROLE_SYSTEM_RIGHT";
        String view = "ROLE_SYSTEM_RIGHT_VIEW";
        String edit = "ROLE_SYSTEM_RIGHT_EDIT";
        String add = "ROLE_SYSTEM_RIGHT_ADD";
        String delete = "ROLE_SYSTEM_RIGHT_DELETE";
    }

    public interface ParamSystem { // Tham số hệ thống
        String author = "ROLE_SYSTEM_PARAM";
        String view = "ROLE_SYSTEM_PARAM_VIEW";
        String edit = "ROLE_SYSTEM_PARAM_EDIT";
        String add = "ROLE_SYSTEM_PARAM_ADD";
        String delete = "ROLE_SYSTEM_PARAM_DELETE";
    }

    public interface Province { // tỉnh thành phố
        String author = "ROLE_SYSTEM_PROVINCE";
        String view = "ROLE_SYSTEM_PROVINCE_VIEW";
        String edit = "ROLE_SYSTEM_PROVINCE_EDIT";
        String add = "ROLE_SYSTEM_PROVINCE_ADD";
        String delete = "ROLE_SYSTEM_PROVINCE_DELETE";
    }

    public interface District { // quận huyện
        String author = "ROLE_SYSTEM_DISTRICT";
        String view = "ROLE_SYSTEM_DISTRICT_VIEW";
        String edit = "ROLE_SYSTEM_DISTRICT_EDIT";
        String add = "ROLE_SYSTEM_DISTRICT_ADD";
        String delete = "ROLE_SYSTEM_DISTRICT_DELETE";
    }

    public interface Commune { // phường xã
        String author = "ROLE_SYSTEM_COMMUNE";
        String view = "ROLE_SYSTEM_COMMUNE_VIEW";
        String edit = "ROLE_SYSTEM_COMMUNE_EDIT";
        String add = "ROLE_SYSTEM_COMMUNE_ADD";
        String delete = "ROLE_SYSTEM_COMMUNE_DELETE";
    }
}
