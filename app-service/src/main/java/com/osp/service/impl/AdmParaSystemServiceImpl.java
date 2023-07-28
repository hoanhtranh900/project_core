package com.osp.service.impl;

import com.osp.core.contants.ConstantString;
import com.osp.core.dto.request.SearchForm;
import com.osp.core.entity.AdmParaSystem;
import com.osp.core.repository.AdmParaSystemRepository;
import com.osp.core.service.BaseServiceImpl;
import com.osp.core.utils.QueryBuilder;
import com.osp.core.utils.QueryUtils;
import com.osp.notification.TelegramBot;
import com.osp.service.AdmParaSystemService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO: write you class description here
 *
 * @author
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AdmParaSystemServiceImpl extends BaseServiceImpl<AdmParaSystem, AdmParaSystemRepository> implements AdmParaSystemService<AdmParaSystem> {
    public AdmParaSystemServiceImpl(AdmParaSystemRepository repository) {
        super(repository);
    }

    @PersistenceContext private EntityManager entityManager;
    @Autowired private AdmParaSystemRepository paraSystemRepository;
    @Autowired private TelegramBot telegramBot;

    public Page<AdmParaSystem> getPage(SearchForm searchObject, Pageable pageable) {
        Page<AdmParaSystem> page = null;
        try {
            List<AdmParaSystem> list = new ArrayList<>();
            String hql = " from AdmParaSystem u where 1=1 ";
            QueryBuilder builder = new QueryBuilder(entityManager, "select count(u) ", new StringBuffer(hql), false);

            List<QueryBuilder.ConditionObject> conditionObjects = new ArrayList<>();
            conditionObjects.add(new QueryBuilder.ConditionObject(QueryUtils.EQ,"u.status", ConstantString.STATUS.active));
            conditionObjects.add(new QueryBuilder.ConditionObject(QueryUtils.EQ,"u.status", ConstantString.STATUS.lock));
            builder.andOrListNative(conditionObjects);

            if (StringUtils.isNotBlank(searchObject.getName())) {
                builder.and(QueryUtils.LIKE, "u.name", "%" + searchObject.getName().trim() + "%");
            }
            if (StringUtils.isNotBlank(searchObject.getValue())) {
                builder.and(QueryUtils.LIKE, "u.value", "%" + searchObject.getValue().trim() + "%");
            }
            if (StringUtils.isNotBlank(searchObject.getStatus())) {
                builder.and(QueryUtils.EQ, "u.status", Long.parseLong(searchObject.getStatus().trim()));
            }

            Query query = builder.initQuery(false);
            int count = Integer.parseInt(query.getSingleResult().toString());

            pageable.getSort().iterator().forEachRemaining(order -> {
                builder.addOrder(order.getProperty(), order.getDirection().isAscending() ? "ASC" : "DESC");
            });
            builder.addOrder("u.createdDate", QueryUtils.DESC);

            builder.setSubFix("select u ");
            query = builder.initQuery(AdmParaSystem.class);
            if(pageable.getPageSize() > 0){
                query.setFirstResult(Integer.parseInt(String.valueOf(pageable.getOffset()))).setMaxResults(pageable.getPageSize());
            }
            list = query.getResultList();

            if (list != null) {
                page = new PageImpl<>(list, pageable, count);
            }

        } catch (Exception e) {
            e.printStackTrace();
            telegramBot.sendBotMessage("Lỗi tại AdmParaSystemServiceImpl.getPage " + e.getMessage());
        }
        return page;
    }

    @Override
    public List<AdmParaSystem> deleteByIds(List<Long> ids) {
        List<AdmParaSystem> users = paraSystemRepository.loadByListIds(ids);
        for (AdmParaSystem user: users) {
            user.setIsDelete(ConstantString.IS_DELETE.delete);
            paraSystemRepository.save(user);
        }
        return users;
    }

    @Override
    public List<AdmParaSystem> locks(List<Long> ids) {
        List<AdmParaSystem> users = paraSystemRepository.loadByListIds(ids);
        for (AdmParaSystem user: users) {
            user.setStatus(ConstantString.STATUS.lock);
            paraSystemRepository.save(user);
        }
        return users;
    }

    @Override
    public List<AdmParaSystem> unlocks(List<Long> ids) {
        List<AdmParaSystem> users = paraSystemRepository.loadByListIds(ids);
        for (AdmParaSystem user: users) {
            user.setStatus(ConstantString.STATUS.active);
            paraSystemRepository.save(user);
        }
        return users;
    }

}
