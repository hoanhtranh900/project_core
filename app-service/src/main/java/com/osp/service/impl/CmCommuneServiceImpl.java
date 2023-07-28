package com.osp.service.impl;

import com.osp.core.contants.ConstantString;
import com.osp.core.dto.request.SearchForm;
import com.osp.core.entity.CmCommune;
import com.osp.core.entity.CmDistrict;
import com.osp.core.entity.CmProvince;
import com.osp.core.exception.BadRequestException;
import com.osp.core.exception.BaseException;
import com.osp.core.repository.CmCommuneRepository;
import com.osp.core.service.BaseServiceImpl;
import com.osp.core.utils.QueryBuilder;
import com.osp.core.utils.QueryUtils;
import com.osp.core.utils.UtilsCommon;
import com.osp.notification.TelegramBot;
import com.osp.service.CmCommuneService;
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
public class CmCommuneServiceImpl extends BaseServiceImpl<CmCommune, CmCommuneRepository> implements CmCommuneService<CmCommune> {
    public CmCommuneServiceImpl(CmCommuneRepository repository) {
        super(repository);
    }

    @Autowired private CmProvinceService<CmProvince> provinceService;
    @Autowired private CmDistrictService<CmDistrict> districtService;
    @Autowired private CmCommuneRepository communeRepository;
    @PersistenceContext private EntityManager entityManager;
    @Autowired private MessageSource messageSource;
    @Autowired private TelegramBot telegramBot;

    @Override
    public List<CmCommune> communeByDistrict(Long districtId) {
        CmDistrict district = districtService.get(districtId).orElseThrow(() -> new BaseException(
                messageSource.getMessage("error.ENTITY_NOT_FOUND", new Object[]{"District"}, UtilsCommon.getLocale())
        ));
        return communeRepository.findByDistrict(district);
    }

    @Override
    public Optional<CmCommune> edit(CmCommune dto) {
        CmCommune bo = get(dto.getId()).orElseThrow(() -> new BaseException(
                messageSource.getMessage("error.ENTITY_NOT_FOUND", new Object[]{"Commune"}, UtilsCommon.getLocale())
        ));
        return update(dto.formToBo(dto, bo));
    }

    @Override
    public HashMap initAddOrEdit(Long id) throws BadRequestException {
        HashMap map = new HashMap();
        if (id == null) {
            map.put("commune", new CmCommune());
        } else {
            Optional<CmCommune> bo = get(id);
            if (!bo.isPresent()) {
                throw new BadRequestException( messageSource.getMessage("error.ENTITY_NOT_FOUND", new Object[]{"Commune"}, UtilsCommon.getLocale()));
            }
            map.put("commune", bo);
        }
        map.put("provinceList", provinceService.getAll());
        return map;
    }

    public Page<CmCommune> getPage(SearchForm search, Pageable pageable) {
        Page<CmCommune> page = null;
        try {
            List<CmCommune> list = new ArrayList<>();
            String hql = " from CmCommune u where 1=1 ";
            QueryBuilder builder = new QueryBuilder(entityManager, "select count(u)", new StringBuffer(hql), false);

            List<QueryBuilder.ConditionObject> conditionObjects = new ArrayList<>();
            conditionObjects.add(new QueryBuilder.ConditionObject(QueryUtils.EQ,"u.status", ConstantString.STATUS.active));
            conditionObjects.add(new QueryBuilder.ConditionObject(QueryUtils.EQ,"u.status", ConstantString.STATUS.lock));
            builder.andOrListNative(conditionObjects);

            if (StringUtils.isNotBlank(search.getCommuneName())) {
                builder.and(QueryUtils.LIKE, "u.communeName", "%" + search.getCommuneName().trim() + "%");
            }
            if (StringUtils.isNotBlank(search.getCommuneCode())) {
                builder.and(QueryUtils.LIKE, "u.communeCode", "%" + search.getCommuneCode().trim() + "%");
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
            query = builder.initQuery(CmCommune.class);
            if(pageable.getPageSize() > 0){
                query.setFirstResult(Integer.parseInt(String.valueOf(pageable.getOffset()))).setMaxResults(pageable.getPageSize());
            }
            list = query.getResultList();

            if (list != null) {
                page = new PageImpl<>(list, pageable, count);
            }

        } catch (Exception e) {
            e.printStackTrace();
            telegramBot.sendBotMessage("Lỗi tại CmCommuneServiceImpl.getPage " + e.getMessage());
        }
        return page;
    }

    @Override
    public List<CmCommune> deleteByIds(List<Long> ids) {
        List<CmCommune> users = communeRepository.loadByListIds(ids);
        for (CmCommune user: users) {
            user.setIsDelete(ConstantString.IS_DELETE.delete);
            communeRepository.save(user);
        }
        return users;
    }

    @Override
    public List<CmCommune> locks(List<Long> ids) {
        List<CmCommune> users = communeRepository.loadByListIds(ids);
        for (CmCommune user: users) {
            user.setStatus(ConstantString.STATUS.lock);
            communeRepository.save(user);
        }
        return users;
    }

    @Override
    public List<CmCommune> unlocks(List<Long> ids) {
        List<CmCommune> users = communeRepository.loadByListIds(ids);
        for (CmCommune user: users) {
            user.setStatus(ConstantString.STATUS.active);
            communeRepository.save(user);
        }
        return users;
    }
}
