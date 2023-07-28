package com.osp.service.impl;

import com.osp.core.contants.ConstantString;
import com.osp.core.dto.request.SearchForm;
import com.osp.core.entity.AdmAuthorities;
import com.osp.core.entity.CmCommune;
import com.osp.core.entity.view.ViewAdmAuthorities;
import com.osp.core.exception.BadRequestException;
import com.osp.core.exception.BaseException;
import com.osp.core.repository.AdmAuthoritiesRepository;
import com.osp.core.service.BaseServiceImpl;
import com.osp.core.utils.QueryBuilder;
import com.osp.core.utils.QueryUtils;
import com.osp.core.utils.UtilsCommon;
import com.osp.notification.TelegramBot;
import com.osp.service.AdmAuthoritiesService;
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
public class AdmAuthoritiesServiceImpl extends BaseServiceImpl<AdmAuthorities, AdmAuthoritiesRepository> implements AdmAuthoritiesService<AdmAuthorities> {
    public AdmAuthoritiesServiceImpl(AdmAuthoritiesRepository repository) {
        super(repository);
    }

    @Autowired private AdmAuthoritiesRepository authoritiesRepository;
    @PersistenceContext private EntityManager entityManager;
    @Autowired private MessageSource messageSource;
    @Autowired private TelegramBot telegramBot;

    @Override
    public Optional<AdmAuthorities> edit(AdmAuthorities dto) {
        AdmAuthorities authorities = get(dto.getId()).orElseThrow(() -> new BaseException(messageSource.getMessage("error.ENTITY_NOT_FOUND", new Object[]{"Authorities"}, UtilsCommon.getLocale())));
        return update(dto.formToBo(dto, authorities));
    }

    @Override
    public HashMap initAddOrEdit(Long id) throws BadRequestException {
        HashMap map = new HashMap();
        if (id == null) {
            map.put("authoritie", new CmCommune());
        } else {
            Optional<AdmAuthorities> bo = get(id);
            if (!bo.isPresent()) {
                throw new BadRequestException(messageSource.getMessage("error.ENTITY_NOT_FOUND", new Object[]{"Authorities"}, UtilsCommon.getLocale()));
            }
            map.put("authoritie", bo);
        }
        return map;
    }

    public Page<ViewAdmAuthorities> getPage(SearchForm searchObject, Pageable pageable) {
        Page<ViewAdmAuthorities> page = null;
        try {
            List<ViewAdmAuthorities> list = new ArrayList<>();
            String hql = " from ViewAdmAuthorities u where 1=1 ";
            QueryBuilder builder = new QueryBuilder(entityManager, "select count(u)", new StringBuffer(hql), false);

            List<QueryBuilder.ConditionObject> conditionObjects = new ArrayList<>();
            conditionObjects.add(new QueryBuilder.ConditionObject(QueryUtils.EQ,"u.status", ConstantString.STATUS.active));
            conditionObjects.add(new QueryBuilder.ConditionObject(QueryUtils.EQ,"u.status", ConstantString.STATUS.lock));
            builder.andOrListNative(conditionObjects);

            if (StringUtils.isNotBlank(searchObject.getAuthKey())) {
                builder.and(QueryUtils.LIKE, "u.authKey", "%" + searchObject.getAuthKey().trim() + "%");
            }
            if (StringUtils.isNotBlank(searchObject.getAuthoritieName())) {
                builder.and(QueryUtils.LIKE, "u.authoritieName", "%" + searchObject.getAuthoritieName().trim() + "%");
            }
            if (StringUtils.isNotBlank(searchObject.getDescription())) {
                builder.and(QueryUtils.LIKE, "u.description", "%" + searchObject.getDescription().trim() + "%");
            }
            if (StringUtils.isNotBlank(searchObject.getParentId())) {
                List<QueryBuilder.ConditionObject> objects = new ArrayList<>();
                objects.add(new QueryBuilder.ConditionObject(QueryUtils.EQ,"u.id", Long.parseLong(searchObject.getParentId().trim()) ));
                objects.add(new QueryBuilder.ConditionObject(QueryUtils.EQ,"u.parentId", Long.parseLong(searchObject.getParentId().trim()) ));
                builder.andOrListNative(objects);
            }
            builder.and(QueryUtils.EQ, "u.parentId", 0L);
            Query query = builder.initQuery(false);
            int count = Integer.parseInt(query.getSingleResult().toString());

            pageable.getSort().iterator().forEachRemaining(order -> {
                builder.addOrder(order.getProperty(), order.getDirection().isAscending() ? "ASC" : "DESC");
            });
            builder.addOrder("u.createdDate", QueryUtils.DESC);

            builder.setSubFix("select u");
            query = builder.initQuery(ViewAdmAuthorities.class);
            if(pageable.getPageSize() > 0){
                query.setFirstResult(Integer.parseInt(String.valueOf(pageable.getOffset()))).setMaxResults(pageable.getPageSize());
            }
            list = query.getResultList();

            if (list != null) {
                page = new PageImpl<>(list, pageable, count);
            }

        } catch (Exception e) {
            e.printStackTrace();
            telegramBot.sendBotMessage("Lỗi tại AdmAuthoritiesServiceImpl.getPage " + e.getMessage());
        }
        return page;
    }

    @Override
    public List<AdmAuthorities> deleteByIds(List<Long> ids) {
        List<AdmAuthorities> users = authoritiesRepository.loadByListIds(ids);
        for (AdmAuthorities user: users) {
            user.setIsDelete(ConstantString.IS_DELETE.delete);
            authoritiesRepository.save(user);
        }
        return users;
    }

    @Override
    public List<AdmAuthorities> locks(List<Long> ids) {
        List<AdmAuthorities> users = authoritiesRepository.loadByListIds(ids);
        for (AdmAuthorities user: users) {
            user.setStatus(ConstantString.STATUS.lock);
            authoritiesRepository.save(user);
        }
        return users;
    }

    @Override
    public List<AdmAuthorities> unlocks(List<Long> ids) {
        List<AdmAuthorities> users = authoritiesRepository.loadByListIds(ids);
        for (AdmAuthorities user: users) {
            user.setStatus(ConstantString.STATUS.active);
            authoritiesRepository.save(user);
        }
        return users;
    }

    @Override
    public List<AdmAuthorities> loadListParent() {
        return authoritiesRepository.loadListParent();
    }

    @Override
    public List<AdmAuthorities> findByParentIdAndListStatus(Long parentId, List<Long> status) {
        return authoritiesRepository.findByParentIdAndListStatus(parentId, status);
    }

}
