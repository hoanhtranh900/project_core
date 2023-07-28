package com.osp.quartz.jobs;

import com.osp.core.contants.CategoryBuffer;
import com.osp.core.entity.only.V_CmCommune;
import com.osp.core.entity.only.V_CmDistrict;
import com.osp.core.entity.only.V_CmProvince;
import com.osp.core.repository.CmCommuneRepository;
import com.osp.core.repository.CmDistrictRepository;
import com.osp.core.repository.CmProvinceRepository;
import org.modelmapper.ModelMapper;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;

@Service
public class CategoryBufferJob extends QuartzJobBean {
    @Autowired private CmCommuneRepository communeRepository;
    @Autowired private CmDistrictRepository districtRepository;
    @Autowired private CmProvinceRepository provinceRepository;
    @Autowired private ModelMapper modelMapper;

    @Override
    protected void executeInternal(JobExecutionContext context) {
        communeRepository.findAll().stream().forEach(commune -> {
            CategoryBuffer.communeMap.put(commune.getId(), modelMapper.map(commune, V_CmCommune.class));
        });
        districtRepository.findAll().stream().forEach(district -> {
            CategoryBuffer.districtMap.put(district.getId(), modelMapper.map(district, V_CmDistrict.class));
        });
        provinceRepository.findAll().stream().forEach(province -> {
            CategoryBuffer.provinceMap.put(province.getId(), modelMapper.map(province, V_CmProvince.class));
        });
    }

}
