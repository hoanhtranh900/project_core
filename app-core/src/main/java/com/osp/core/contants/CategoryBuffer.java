package com.osp.core.contants;

import com.osp.core.entity.only.V_CmCommune;
import com.osp.core.entity.only.V_CmDistrict;
import com.osp.core.entity.only.V_CmProvince;
import com.osp.core.repository.CmCommuneRepository;
import com.osp.core.repository.CmDistrictRepository;
import com.osp.core.repository.CmProvinceRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class CategoryBuffer {
    private static CmCommuneRepository communeRepository;
    private static CmDistrictRepository districtRepository;
    private static CmProvinceRepository provinceRepository;
    private static ModelMapper modelMapper;

    public static HashMap<Long, V_CmCommune> communeMap = new HashMap<>();
    public static HashMap<Long, V_CmDistrict> districtMap = new HashMap<>();
    public static HashMap<Long, V_CmProvince> provinceMap = new HashMap<>();

    public CategoryBuffer(final ModelMapper modelMapper, final CmCommuneRepository communeRepository, final CmDistrictRepository districtRepository, final CmProvinceRepository provinceRepository) {
        this.communeRepository = communeRepository;
        this.districtRepository = districtRepository;
        this.provinceRepository = provinceRepository;
        this.modelMapper = modelMapper;
    }

    public static V_CmCommune getCommuneById(Long id) {
        V_CmCommune commune = communeMap.get(id);
        if (commune == null) {
            commune = modelMapper.map(communeRepository.getById(id), V_CmCommune.class);
        }
        return commune;
    }

    public static V_CmDistrict getDistrictById(Long id) {
        V_CmDistrict district = districtMap.get(id);
        if (district == null) {
            district = modelMapper.map(districtRepository.getById(id), V_CmDistrict.class);
        }
        return district;
    }

    public static V_CmProvince getProvinceById(Long id) {
        V_CmProvince province = provinceMap.get(id);
        if (province == null) {
            province = modelMapper.map(provinceRepository.getById(id), V_CmProvince.class);
        }
        return province;
    }
}
