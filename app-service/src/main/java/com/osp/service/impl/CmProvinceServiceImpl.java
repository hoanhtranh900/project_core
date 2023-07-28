package com.osp.service.impl;

import com.osp.core.contants.ConstantString;
import com.osp.core.dto.request.SearchForm;
import com.osp.core.entity.CmCommune;
import com.osp.core.entity.CmProvince;
import com.osp.core.exception.BadRequestException;
import com.osp.core.exception.BaseException;
import com.osp.core.repository.CmProvinceRepository;
import com.osp.core.service.BaseServiceImpl;
import com.osp.core.utils.QueryBuilder;
import com.osp.core.utils.QueryUtils;
import com.osp.core.utils.UtilsCommon;
import com.osp.notification.TelegramBot;
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
public class CmProvinceServiceImpl extends BaseServiceImpl<CmProvince, CmProvinceRepository> implements CmProvinceService<CmProvince> {
    public CmProvinceServiceImpl(CmProvinceRepository repository) {
        super(repository);
    }

    @Autowired private CmProvinceRepository provinceRepository;
    @PersistenceContext private EntityManager entityManager;
    @Autowired private MessageSource messageSource;
    @Autowired private TelegramBot telegramBot;

    @Override
    public Optional<CmProvince> edit(CmProvince dto) {
        CmProvince bo = get(dto.getId()).orElseThrow(() -> new BaseException(
                messageSource.getMessage("error.ENTITY_NOT_FOUND", new Object[]{"Province"}, UtilsCommon.getLocale())
        ));
        return update(dto.formToBo(dto, bo));
    }

    @Override
    public HashMap initAddOrEdit(Long id) throws BadRequestException {
        HashMap map = new HashMap();
        if (id == null) {
            map.put("province", new CmCommune());
        } else {
            Optional<CmProvince> bo = get(id);
            if (!bo.isPresent()) {
                throw new BadRequestException( messageSource.getMessage("error.ENTITY_NOT_FOUND", new Object[]{"Province"}, UtilsCommon.getLocale()));
            }
            map.put("province", bo);
        }
        return map;
    }

    public Page<CmProvince> getPage(SearchForm searchObject, Pageable pageable) {
        Page<CmProvince> page = null;
        try {
            List<CmProvince> list = new ArrayList<>();
            String hql = " from CmProvince u where 1=1 ";
            QueryBuilder builder = new QueryBuilder(entityManager, "select count(u)", new StringBuffer(hql), false);

            List<QueryBuilder.ConditionObject> conditionObjects = new ArrayList<>();
            conditionObjects.add(new QueryBuilder.ConditionObject(QueryUtils.EQ,"u.status", ConstantString.STATUS.active));
            conditionObjects.add(new QueryBuilder.ConditionObject(QueryUtils.EQ,"u.status", ConstantString.STATUS.lock));
            builder.andOrListNative(conditionObjects);

            if (StringUtils.isNotBlank(searchObject.getProvinceCode())) {
                builder.and(QueryUtils.LIKE, "u.provinceCode", "%" + searchObject.getProvinceCode().trim() + "%");
            }
            if (StringUtils.isNotBlank(searchObject.getProvinceName())) {
                builder.and(QueryUtils.LIKE, "u.provinceName", "%" + searchObject.getProvinceName().trim() + "%");
            }
            if (StringUtils.isNotBlank(searchObject.getStatus())) {
                builder.and(QueryUtils.EQ, "u.status", Long.parseLong(searchObject.getStatus().trim()));
            }
            if (StringUtils.isNotBlank(searchObject.getIsDelete())) {
                builder.and(QueryUtils.EQ, "u.isDelete", Long.parseLong(searchObject.getIsDelete().trim()));
            }

            Query query = builder.initQuery(false);
            int count = Integer.parseInt(query.getSingleResult().toString());

            pageable.getSort().iterator().forEachRemaining(order -> {
                builder.addOrder(order.getProperty(), order.getDirection().isAscending() ? "ASC" : "DESC");
            });
            builder.addOrder("u.createdDate", QueryUtils.DESC);

            builder.setSubFix("select u");
            query = builder.initQuery(CmProvince.class);
            if(pageable.getPageSize() > 0){
                query.setFirstResult(Integer.parseInt(String.valueOf(pageable.getOffset()))).setMaxResults(pageable.getPageSize());
            }
            list = query.getResultList();

            if (list != null) {
                page = new PageImpl<>(list, pageable, count);
            }

        } catch (Exception e) {
            e.printStackTrace();
            telegramBot.sendBotMessage("Lỗi tại CmProvinceServiceImpl.getPage " + e.getMessage());
        }
        return page;
    }

    @Override
    public List<CmProvince> deleteByIds(List<Long> ids) {
        List<CmProvince> users = provinceRepository.loadByListIds(ids);
        for (CmProvince user: users) {
            user.setIsDelete(ConstantString.IS_DELETE.delete);
            provinceRepository.save(user);
        }
        return users;
    }

    @Override
    public List<CmProvince> locks(List<Long> ids) {
        List<CmProvince> users = provinceRepository.loadByListIds(ids);
        for (CmProvince user: users) {
            user.setStatus(ConstantString.STATUS.lock);
            provinceRepository.save(user);
        }
        return users;
    }

    @Override
    public List<CmProvince> unlocks(List<Long> ids) {
        List<CmProvince> users = provinceRepository.loadByListIds(ids);
        for (CmProvince user: users) {
            user.setStatus(ConstantString.STATUS.active);
            provinceRepository.save(user);
        }
        return users;
    }
}
