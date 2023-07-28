package com.osp.service.impl;

import com.osp.core.contants.ConstantString;
import com.osp.core.dto.request.SearchForm;
import com.osp.core.entity.CmCommune;
import com.osp.core.entity.CmDistrict;
import com.osp.core.entity.CmProvince;
import com.osp.core.exception.BadRequestException;
import com.osp.core.exception.BaseException;
import com.osp.core.repository.CmDistrictRepository;
import com.osp.core.service.BaseServiceImpl;
import com.osp.core.utils.QueryBuilder;
import com.osp.core.utils.QueryUtils;
import com.osp.core.utils.UtilsCommon;
import com.osp.notification.TelegramBot;
import com.osp.service.CmDistrictService;
import com.osp.service.CmProvinceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * TODO: write you class description here
 *
 * @author
 */

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class CmDistrictServiceImpl extends BaseServiceImpl<CmDistrict, CmDistrictRepository> implements CmDistrictService<CmDistrict> {
    public CmDistrictServiceImpl(CmDistrictRepository repository) {
        super(repository);
    }

    @Autowired private CmDistrictRepository districtRepository;
    @PersistenceContext private EntityManager entityManager;
    @Autowired private CmProvinceService<CmProvince> provinceService;
    @Autowired private MessageSource messageSource;
    @Autowired private TelegramBot telegramBot;

    @Override
    public List<CmDistrict> districtByProvince(Long provinceId) {
        CmProvince province = provinceService.get(provinceId).orElseThrow(() -> new BaseException(
                messageSource.getMessage("error.ENTITY_NOT_FOUND", new Object[]{"Province"}, UtilsCommon.getLocale())
        ));
        return districtRepository.findByProvince(province);
    }

    @Override
    public Optional<CmDistrict> edit(CmDistrict dto) {
        CmDistrict bo = get(dto.getId()).orElseThrow(() -> new BaseException(
                messageSource.getMessage("error.ENTITY_NOT_FOUND", new Object[]{"District"}, UtilsCommon.getLocale())
        ));
        return update(dto.formToBo(dto, bo));
    }

    @Override
    public HashMap initAddOrEdit(Long id) throws BadRequestException {
        HashMap map = new HashMap();
        if (id == null) {
            map.put("district", new CmCommune());
        } else {
            Optional<CmDistrict> bo = get(id);
            if (!bo.isPresent()) {
                throw new BadRequestException( messageSource.getMessage("error.ENTITY_NOT_FOUND", new Object[]{"District"}, UtilsCommon.getLocale()));
            }
            map.put("district", bo);
        }
        map.put("provinceList", provinceService.getAll());
        return map;
    }

    public Page<CmDistrict> getPage(SearchForm search, Pageable pageable) {
        Page<CmDistrict> page = null;
        try {
            List<CmDistrict> list = new ArrayList<>();
            String hql = " from CmDistrict u where 1=1 ";
            QueryBuilder builder = new QueryBuilder(entityManager, "select count(u)", new StringBuffer(hql), false);

            List<QueryBuilder.ConditionObject> conditionObjects = new ArrayList<>();
            conditionObjects.add(new QueryBuilder.ConditionObject(QueryUtils.EQ,"u.status", ConstantString.STATUS.active));
            conditionObjects.add(new QueryBuilder.ConditionObject(QueryUtils.EQ,"u.status", ConstantString.STATUS.lock));
            builder.andOrListNative(conditionObjects);

            if (StringUtils.isNotBlank(search.getDistrictName())) {
                builder.and(QueryUtils.LIKE, "u.districtName", "%" + search.getDistrictName().trim() + "%");
            }
            if (StringUtils.isNotBlank(search.getDistrictCode())) {
                builder.and(QueryUtils.LIKE, "u.districtCode", "%" + search.getDistrictCode().trim() + "%");
            }
            if (StringUtils.isNotBlank(search.getStatus())) {
                builder.and(QueryUtils.EQ, "u.status", Long.parseLong(search.getStatus().trim()));
            }
            if (StringUtils.isNotBlank(search.getIsDelete())) {
                builder.and(QueryUtils.EQ, "u.isDelete", Long.parseLong(search.getIsDelete().trim()));
            }

            Query query = builder.initQuery(false);
            int count = Integer.parseInt(query.getSingleResult().toString());

            pageable.getSort().iterator().forEachRemaining(order -> {
                builder.addOrder(order.getProperty(), order.getDirection().isAscending() ? "ASC" : "DESC");
            });
            builder.addOrder("u.createdDate", QueryUtils.DESC);

            builder.setSubFix("select u");
            query = builder.initQuery(CmDistrict.class);
            if(pageable.getPageSize() > 0){
                query.setFirstResult(Integer.parseInt(String.valueOf(pageable.getOffset()))).setMaxResults(pageable.getPageSize());
            }
            list = query.getResultList();

            if (list != null) {
                page = new PageImpl<>(list, pageable, count);
            }

        } catch (Exception e) {
            e.printStackTrace();
            telegramBot.sendBotMessage("Lỗi tại CmDistrictServiceImpl.getPage " + e.getMessage());
        }
        return page;
    }

    @Override
    public List<CmDistrict> deleteByIds(List<Long> ids) {
        List<CmDistrict> users = districtRepository.loadByListIds(ids);
        for (CmDistrict user: users) {
            user.setIsDelete(ConstantString.IS_DELETE.delete);
            districtRepository.save(user);
        }
        return users;
    }

    @Override
    public List<CmDistrict> locks(List<Long> ids) {
        List<CmDistrict> users = districtRepository.loadByListIds(ids);
        for (CmDistrict user: users) {
            user.setStatus(ConstantString.STATUS.lock);
            districtRepository.save(user);
        }
        return users;
    }

    @Override
    public List<CmDistrict> unlocks(List<Long> ids) {
        List<CmDistrict> users = districtRepository.loadByListIds(ids);
        for (CmDistrict user: users) {
            user.setStatus(ConstantString.STATUS.active);
            districtRepository.save(user);
        }
        return users;
    }
}
