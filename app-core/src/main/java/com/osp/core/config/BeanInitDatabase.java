package com.osp.core.config;

import com.osp.core.contants.ConstantString;
import com.osp.core.entity.*;
import com.osp.core.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class BeanInitDatabase {

    @Autowired private Environment env;
    @Autowired private AdmAuthoritiesRepository authoritiesRepository;
    @Autowired private AdmGroupRepository groupRepository;
    @Autowired private AdmUserRepository userRepository;
    @Autowired private AdmRightRepository rightRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private AdmUserTypeRepository userTypeRepository;

    @Bean
    public void initDataDatabase() {

        if (!userRepository.findByUsername("admin").isPresent()) {

            Long orderId = 1L;
            Long parentId = 1L;
            authoritiesRepository.save(AdmAuthorities.builder().isDelete(0L).status(0L).authKey("ROLE_SYSTEM_USER").description("Người dùng").authoritieName("Người dùng").parentId(0L).orderId(orderId).build());
            authoritiesRepository.save(AdmAuthorities.builder().isDelete(0L).status(0L).authKey("ROLE_SYSTEM_USER_ADD").description("Thêm mới người dùng").authoritieName("Thêm mới người dùng").parentId(parentId).orderId(orderId).build());
            authoritiesRepository.save(AdmAuthorities.builder().isDelete(0L).status(0L).authKey("ROLE_SYSTEM_USER_EDIT").description("Sửa người dùng").authoritieName("Sửa người dùng").parentId(parentId).orderId(orderId).build());
            authoritiesRepository.save(AdmAuthorities.builder().isDelete(0L).status(0L).authKey("ROLE_SYSTEM_USER_DELETE").description("Xóa người dùng").authoritieName("Xóa người dùng").parentId(parentId).orderId(orderId).build());
            authoritiesRepository.save(AdmAuthorities.builder().isDelete(0L).status(0L).authKey("ROLE_SYSTEM_USER_VIEW").description("Xem người dùng").authoritieName("Xem người dùng").parentId(parentId).orderId(orderId).build());

            orderId = orderId + 1L;
            parentId = 6L;
            authoritiesRepository.save(AdmAuthorities.builder().isDelete(0L).status(0L).authKey("ROLE_SYSTEM_GROUP").description("Nhóm người dùng").authoritieName("Nhóm người dùng").parentId(0L).orderId(orderId).build());
            authoritiesRepository.save(AdmAuthorities.builder().isDelete(0L).status(0L).authKey("ROLE_SYSTEM_GROUP_ADD").description("Thêm mới nhóm người dùng").authoritieName("Thêm mới nhóm người dùng").parentId(parentId).orderId(orderId).build());
            authoritiesRepository.save(AdmAuthorities.builder().isDelete(0L).status(0L).authKey("ROLE_SYSTEM_GROUP_EDIT").description("Sửa nhóm người dùng").authoritieName("Sửa nhóm người dùng").parentId(parentId).orderId(orderId).build());
            authoritiesRepository.save(AdmAuthorities.builder().isDelete(0L).status(0L).authKey("ROLE_SYSTEM_GROUP_DELETE").description("Xóa nhóm người dùng").authoritieName("Xóa nhóm người dùng").parentId(parentId).orderId(orderId).build());
            authoritiesRepository.save(AdmAuthorities.builder().isDelete(0L).status(0L).authKey("ROLE_SYSTEM_GROUP_VIEW").description("Xem nhóm người dùng").authoritieName("Xem nhóm người dùng").parentId(parentId).orderId(orderId).build());

            orderId = orderId + 1L;
            parentId = 11L;
            authoritiesRepository.save(AdmAuthorities.builder().isDelete(0L).status(0L).authKey("ROLE_SYSTEM_AUTHORITY").description("Chức năng").authoritieName("Chức năng").parentId(0L).orderId(orderId).build());
            authoritiesRepository.save(AdmAuthorities.builder().isDelete(0L).status(0L).authKey("ROLE_SYSTEM_AUTHORITY_ADD").description("Thêm mới chức năng").authoritieName("Thêm mới chức năng").parentId(parentId).orderId(orderId).build());
            authoritiesRepository.save(AdmAuthorities.builder().isDelete(0L).status(0L).authKey("ROLE_SYSTEM_AUTHORITY_EDIT").description("Sửa chức năng").authoritieName("Sửa chức năng").parentId(parentId).orderId(orderId).build());
            authoritiesRepository.save(AdmAuthorities.builder().isDelete(0L).status(0L).authKey("ROLE_SYSTEM_AUTHORITY_DELETE").description("Xóa chức năng").authoritieName("Xóa chức năng").parentId(parentId).orderId(orderId).build());
            authoritiesRepository.save(AdmAuthorities.builder().isDelete(0L).status(0L).authKey("ROLE_SYSTEM_AUTHORITY_VIEW").description("Xem chức năng").authoritieName("Xem chức năng").parentId(parentId).orderId(orderId).build());

            orderId = orderId + 1L;
            parentId = 16L;
            authoritiesRepository.save(AdmAuthorities.builder().isDelete(0L).status(0L).authKey("ROLE_SYSTEM_PARAM").description("Tham số hệ thống").authoritieName("Tham số hệ thống").parentId(0L).orderId(orderId).build());
            authoritiesRepository.save(AdmAuthorities.builder().isDelete(0L).status(0L).authKey("ROLE_SYSTEM_PARAM_ADD").description("Thêm mới tham số hệ thống").authoritieName("Thêm mới tham số hệ thống").parentId(parentId).orderId(orderId).build());
            authoritiesRepository.save(AdmAuthorities.builder().isDelete(0L).status(0L).authKey("ROLE_SYSTEM_PARAM_EDIT").description("Sửa tham số hệ thống").authoritieName("Sửa tham số hệ thống").parentId(parentId).orderId(orderId).build());
            authoritiesRepository.save(AdmAuthorities.builder().isDelete(0L).status(0L).authKey("ROLE_SYSTEM_PARAM_DELETE").description("Xóa tham số hệ thống").authoritieName("Xóa tham số hệ thống").parentId(parentId).orderId(orderId).build());
            authoritiesRepository.save(AdmAuthorities.builder().isDelete(0L).status(0L).authKey("ROLE_SYSTEM_PARAM_VIEW").description("Xem tham số hệ thống").authoritieName("Xem tham số hệ thống").parentId(parentId).orderId(orderId).build());

            orderId = orderId + 1L;
            parentId = 21L;
            authoritiesRepository.save(AdmAuthorities.builder().isDelete(0L).status(0L).authKey("ROLE_SYSTEM_RIGHT").description("Menu").authoritieName("Menu").parentId(0L).orderId(orderId).build());
            authoritiesRepository.save(AdmAuthorities.builder().isDelete(0L).status(0L).authKey("ROLE_SYSTEM_RIGHT_ADD").description("Thêm mới menu").authoritieName("Thêm mới menu").parentId(parentId).orderId(orderId).build());
            authoritiesRepository.save(AdmAuthorities.builder().isDelete(0L).status(0L).authKey("ROLE_SYSTEM_RIGHT_EDIT").description("Sửa menu").authoritieName("Sửa menu").parentId(parentId).orderId(orderId).build());
            authoritiesRepository.save(AdmAuthorities.builder().isDelete(0L).status(0L).authKey("ROLE_SYSTEM_RIGHT_DELETE").description("Xóa menu").authoritieName("Xóa menu").parentId(parentId).orderId(orderId).build());
            authoritiesRepository.save(AdmAuthorities.builder().isDelete(0L).status(0L).authKey("ROLE_SYSTEM_RIGHT_VIEW").description("Xem menu").authoritieName("Xem menu").parentId(parentId).orderId(orderId).build());

            orderId = orderId + 1L;
            parentId = 26L;
            authoritiesRepository.save(AdmAuthorities.builder().isDelete(0L).status(0L).authKey("ROLE_SYSTEM_DEPT").description("Phòng ban").authoritieName("Phòng ban").parentId(0L).orderId(orderId).build());
            authoritiesRepository.save(AdmAuthorities.builder().isDelete(0L).status(0L).authKey("ROLE_SYSTEM_DEPT_ADD").description("Thêm mới phòng ban").authoritieName("Thêm mới phòng ban").parentId(parentId).orderId(orderId).build());
            authoritiesRepository.save(AdmAuthorities.builder().isDelete(0L).status(0L).authKey("ROLE_SYSTEM_DEPT_EDIT").description("Sửa phòng ban").authoritieName("Sửa phòng ban").parentId(parentId).orderId(orderId).build());
            authoritiesRepository.save(AdmAuthorities.builder().isDelete(0L).status(0L).authKey("ROLE_SYSTEM_DEPT_DELETE").description("Xóa phòng ban").authoritieName("Xóa phòng ban").parentId(parentId).orderId(orderId).build());
            authoritiesRepository.save(AdmAuthorities.builder().isDelete(0L).status(0L).authKey("ROLE_SYSTEM_DEPT_VIEW").description("Xem phòng ban").authoritieName("Xem phòng ban").parentId(parentId).orderId(orderId).build());

            orderId = orderId + 1L;
            parentId = 31L;
            authoritiesRepository.save(AdmAuthorities.builder().isDelete(0L).status(0L).authKey("ROLE_SYSTEM_PROVINCE").description("Tỉnh thành").authoritieName("Tỉnh thành").parentId(0L).orderId(orderId).build());
            authoritiesRepository.save(AdmAuthorities.builder().isDelete(0L).status(0L).authKey("ROLE_SYSTEM_PROVINCE_ADD").description("Thêm mới tỉnh thành").authoritieName("Thêm mới tỉnh thành").parentId(parentId).orderId(orderId).build());
            authoritiesRepository.save(AdmAuthorities.builder().isDelete(0L).status(0L).authKey("ROLE_SYSTEM_PROVINCE_EDIT").description("Sửa tỉnh thành").authoritieName("Sửa tỉnh thành").parentId(parentId).orderId(orderId).build());
            authoritiesRepository.save(AdmAuthorities.builder().isDelete(0L).status(0L).authKey("ROLE_SYSTEM_PROVINCE_DELETE").description("Xóa tỉnh thành").authoritieName("Xóa tỉnh thành").parentId(parentId).orderId(orderId).build());
            authoritiesRepository.save(AdmAuthorities.builder().isDelete(0L).status(0L).authKey("ROLE_SYSTEM_PROVINCE_VIEW").description("Xem tỉnh thành").authoritieName("Xem tỉnh thành").parentId(parentId).orderId(orderId).build());

            orderId = orderId + 1L;
            parentId = 36L;
            authoritiesRepository.save(AdmAuthorities.builder().isDelete(0L).status(0L).authKey("ROLE_SYSTEM_DISTRICT").description("Quận huyện").authoritieName("Quận huyện").parentId(0L).orderId(orderId).build());
            authoritiesRepository.save(AdmAuthorities.builder().isDelete(0L).status(0L).authKey("ROLE_SYSTEM_DISTRICT_ADD").description("Thêm mới quận huyện").authoritieName("Thêm mới quận huyện").parentId(parentId).orderId(orderId).build());
            authoritiesRepository.save(AdmAuthorities.builder().isDelete(0L).status(0L).authKey("ROLE_SYSTEM_DISTRICT_EDIT").description("Sửa quận huyện").authoritieName("Sửa quận huyện").parentId(parentId).orderId(orderId).build());
            authoritiesRepository.save(AdmAuthorities.builder().isDelete(0L).status(0L).authKey("ROLE_SYSTEM_DISTRICT_DELETE").description("Xóa quận huyện").authoritieName("Xóa quận huyện").parentId(parentId).orderId(orderId).build());
            authoritiesRepository.save(AdmAuthorities.builder().isDelete(0L).status(0L).authKey("ROLE_SYSTEM_DISTRICT_VIEW").description("Xem quận huyện").authoritieName("Xem quận huyện").parentId(parentId).orderId(orderId).build());

            orderId = orderId + 1L;
            parentId = 41L;
            authoritiesRepository.save(AdmAuthorities.builder().isDelete(0L).status(0L).authKey("ROLE_SYSTEM_COMMUNE").description("Phường xã").authoritieName("Phường xã").parentId(0L).orderId(orderId).build());
            authoritiesRepository.save(AdmAuthorities.builder().isDelete(0L).status(0L).authKey("ROLE_SYSTEM_COMMUNE_ADD").description("Thêm mới phường xã").authoritieName("Thêm mới phường xã").parentId(parentId).orderId(orderId).build());
            authoritiesRepository.save(AdmAuthorities.builder().isDelete(0L).status(0L).authKey("ROLE_SYSTEM_COMMUNE_EDIT").description("Sửa phường xã").authoritieName("Sửa phường xã").parentId(parentId).orderId(orderId).build());
            authoritiesRepository.save(AdmAuthorities.builder().isDelete(0L).status(0L).authKey("ROLE_SYSTEM_COMMUNE_DELETE").description("Xóa phường xã").authoritieName("Xóa phường xã").parentId(parentId).orderId(orderId).build());
            authoritiesRepository.save(AdmAuthorities.builder().isDelete(0L).status(0L).authKey("ROLE_SYSTEM_COMMUNE_VIEW").description("Xem phường xã").authoritieName("Xem phường xã").parentId(parentId).orderId(orderId).build());

            AdmUser us = userRepository.save(AdmUser.builder().isDelete(0L).status(0L).groups(Arrays.asList(groupRepository.getById(1L))).username("admin").password(passwordEncoder.encode("admin")).build());
            userTypeRepository.save(AdmUserType.builder().users(us).typeName("Quản trị viên").typeCode("SYSTEM").build());

            List<AdmAuthorities> groupAuthorities = new ArrayList<>();
            for (Long i = 1L; i <= 45L; i++) {
                groupAuthorities.add(authoritiesRepository.getById(i));
            }
            groupRepository.save(AdmGroup.builder().groupUsers(Arrays.asList(us)).isDelete(0L).status(0L).groupAuthorities(groupAuthorities).groupName("Quản trị hệ thống").description("Quản trị hệ thống").build());

            parentId = 1L;
            rightRepository.save(AdmRight.builder().isDelete(0L).status(0L).rightName("Quản trị hệ thống").description("Quản trị hệ thống").urlRewrite("/admin/system").iconUrl("icon-puzzle").parentId(0L).rightCode("admin").rightOrder(0L).authoritieId(null).build());
            rightRepository.save(AdmRight.builder().isDelete(0L).status(0L).rightName("Nhóm người dùng").description("Nhóm người dùng").urlRewrite("/admin/system/adm-group").iconUrl("icon-puzzle").parentId(parentId).rightCode("admin").rightOrder(1L).authoritieId(9L).build());
            rightRepository.save(AdmRight.builder().isDelete(0L).status(0L).rightName("Người dùng").description("Người dùng").urlRewrite("/admin/system/adm-user").iconUrl("icon-puzzle").parentId(parentId).rightCode("admin").rightOrder(2L).authoritieId(5L).build());
            rightRepository.save(AdmRight.builder().isDelete(0L).status(0L).rightName("Danh sách MENU").description("Danh sách MENU").urlRewrite("/admin/system/adm-right").iconUrl("icon-puzzle").parentId(parentId).rightCode("admin").rightOrder(3L).authoritieId(25L).build());
            rightRepository.save(AdmRight.builder().isDelete(0L).status(0L).rightName("Biến hệ thống").description("Biến hệ thống").urlRewrite("/admin/system/adm-param").iconUrl("icon-puzzle").parentId(parentId).rightCode("admin").rightOrder(4L).authoritieId(15L).build());
            rightRepository.save(AdmRight.builder().isDelete(0L).status(0L).rightName("Chức năng").description("Chức năng").urlRewrite("/admin/system/adm-authorities").iconUrl("icon-puzzle").parentId(parentId).rightCode("admin").rightOrder(5L).authoritieId(20L).build());
            rightRepository.save(AdmRight.builder().isDelete(0L).status(0L).rightName("Phòng ban").description("Phòng ban").urlRewrite("/admin/system/adm-dept").iconUrl("icon-puzzle").parentId(parentId).rightCode("admin").rightOrder(6L).authoritieId(30L).build());
        }
    }
}
