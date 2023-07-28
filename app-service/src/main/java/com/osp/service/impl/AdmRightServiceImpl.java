package com.osp.service.impl;

import com.osp.core.contants.ConstantString;
import com.osp.core.dto.request.SearchForm;
import com.osp.core.entity.AdmRight;
import com.osp.core.repository.AdmRightRepository;
import com.osp.core.service.BaseServiceImpl;
import com.osp.core.utils.QueryBuilder;
import com.osp.core.utils.QueryUtils;
import com.osp.notification.TelegramBot;
import com.osp.service.AdmRightService;
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
public class AdmRightServiceImpl extends BaseServiceImpl<AdmRight, AdmRightRepository> implements AdmRightService<AdmRight> {
    public AdmRightServiceImpl(AdmRightRepository repository) {
        super(repository);
    }

    @Autowired private AdmRightRepository rightRepository;
    @PersistenceContext private EntityManager entityManager;
    @Autowired private TelegramBot telegramBot;

    public Page<AdmRight> getPage(SearchForm searchObject, Pageable pageable) {
        Page<AdmRight> page = null;
        try {
            List<AdmRight> list = new ArrayList<>();
            String hql = " from AdmRight u where 1=1 ";
            QueryBuilder builder = new QueryBuilder(entityManager, "select count(u)", new StringBuffer(hql), false);

            List<QueryBuilder.ConditionObject> conditionObjects = new ArrayList<>();
            conditionObjects.add(new QueryBuilder.ConditionObject(QueryUtils.EQ,"u.status", ConstantString.STATUS.active));
            conditionObjects.add(new QueryBuilder.ConditionObject(QueryUtils.EQ,"u.status", ConstantString.STATUS.lock));
            builder.andOrListNative(conditionObjects);

            if (StringUtils.isNotBlank(searchObject.getRightName())) {
                builder.and(QueryUtils.LIKE, "u.rightName", "%" + searchObject.getRightName().trim() + "%");
            }
            if (StringUtils.isNotBlank(searchObject.getDescription())) {
                builder.and(QueryUtils.LIKE, "u.description", "%" + searchObject.getDescription().trim() + "%");
            }
            if (StringUtils.isNotBlank(searchObject.getStatus())) {
                builder.and(QueryUtils.EQ, "u.status", Long.parseLong(searchObject.getStatus().trim()));
            }
            builder.and(QueryUtils.EQ, "u.parentId", 0L);

            Query query = builder.initQuery(false);
            int count = Integer.parseInt(query.getSingleResult().toString());

            pageable.getSort().iterator().forEachRemaining(order -> {
                builder.addOrder(order.getProperty(), order.getDirection().isAscending() ? "ASC" : "DESC");
            });
            builder.addOrder("u.createdDate", QueryUtils.DESC);

            builder.setSubFix("select u");
            query = builder.initQuery(AdmRight.class);
            if(pageable.getPageSize() > 0){
                query.setFirstResult(Integer.parseInt(String.valueOf(pageable.getOffset()))).setMaxResults(pageable.getPageSize());
            }
            list = query.getResultList();

            if (list != null) {
                page = new PageImpl<>(list, pageable, count);
            }

        } catch (Exception e) {
            e.printStackTrace();
            telegramBot.sendBotMessage("Lỗi tại AdmRightServiceImpl.getPage " + e.getMessage());
        }
        return page;
    }

    @Override
    public List<AdmRight> deleteByIds(List<Long> ids) {
        List<AdmRight> users = rightRepository.loadByListIds(ids);
        for (AdmRight user: users) {
            user.setIsDelete(ConstantString.IS_DELETE.delete);
            rightRepository.save(user);
        }
        return users;
    }

    @Override
    public List<AdmRight> locks(List<Long> ids) {
        List<AdmRight> users = rightRepository.loadByListIds(ids);
        for (AdmRight user: users) {
            user.setStatus(ConstantString.STATUS.lock);
            rightRepository.save(user);
        }
        return users;
    }

    @Override
    public List<AdmRight> unlocks(List<Long> ids) {
        List<AdmRight> users = rightRepository.loadByListIds(ids);
        for (AdmRight user: users) {
            user.setStatus(ConstantString.STATUS.active);
            rightRepository.save(user);
        }
        return users;
    }

    @Override
    public List<AdmRight> loadListParent() {
        return rightRepository.loadListParent();
    }

    @Override
    public List<AdmRight> findByParentIdAndStatusInAndIsDelete(Long parentId, List<Long> status, Long isDelete) {
        return rightRepository.findByParentIdAndStatusInAndIsDelete(parentId, status, isDelete);
    }

}
