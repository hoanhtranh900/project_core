package com.osp.core.contants;

import com.osp.core.dto.response.ConfigSystem;

import java.util.*;

/*
* ConstantString
* */
public class ConstantString {
    public static final Map<String, Object> map;

    static {
        Map<String, Object> all = new HashMap<>();
        all.put("STATUS", STATUS.lstAll);
        all.put("IS_DELETE", IS_DELETE.lstAll);
        all.put("ADM_USER_TYPE_USER", ADM_USER_TYPE_USER.lstAll);
        map = Collections.unmodifiableMap(all);
    }

    /*
    * Readme
    * cách tạo interface: tên_bảng_tên_cột
    * */

    public interface STATUS {
        Long active = 0L;
        Long lock = 1L;
        Long expire = 2L;

        String active_str = "Đang hoạt động";
        String lock_str = "Đã khóa";
        String expire_str = "Đã hết hạn";

        List<ConfigSystem> lstAll = Arrays.asList(ConfigSystem.builder().id(active).value(active_str).build(), ConfigSystem.builder().id(lock).value(lock_str).build(), ConfigSystem.builder().id(expire).value(expire_str).build());
        static String getStatusStr(Long status) {
            if (status == null) {
                return null;
            }
            switch(status.intValue()) {
                case 0:
                    return STATUS.active_str;
                case 1:
                    return STATUS.lock_str;
                case 2:
                    return STATUS.expire_str;
            }
            return null;
        }
    }

    public interface IS_DELETE {
        Long active = 0L;
        Long delete = 1L;

        String active_str = "Đang sử dụng";
        String delete_str = "Đã xóa";

        List<ConfigSystem> lstAll = Arrays.asList(ConfigSystem.builder().id(active).value(active_str).build(), ConfigSystem.builder().id(delete).value(delete_str).build());
        static String getStatusStr(Long status) {
            if (status == null) {
                return null;
            }
            switch(status.intValue()) {
                case 0:
                    return IS_DELETE.active_str;
                case 1:
                    return IS_DELETE.delete_str;
            }
            return null;
        }
    }

    public interface ATTACH_DOCUMENT {
        Long ENCRYPTED = 1L;
        Long PDF = 1L;
        Long ORIGINAL = 1L;
    }

    public interface ADM_USER_TYPE_USER {
        Long system = 1L;
        String system_str = "Người dùng quản trị hệ thống";

        List<ConfigSystem> lstAll = Arrays.asList(ConfigSystem.builder().id(system).value(system_str).build());
        static String getStr(Long type) {
            if (type == null) {
                return null;
            }
            switch(type.intValue()) {
                case 1:
                    return system_str;
            }
            return null;
        }
    }
}
