package com.osp.service.impl;

import com.osp.core.contants.ConstantString;
import com.osp.core.dto.request.SearchForm;
import com.osp.core.entity.AdmDept;
import com.osp.core.entity.CmCommune;
import com.osp.core.entity.view.ViewAdmDept;
import com.osp.core.exception.BadRequestException;
import com.osp.core.exception.BaseException;
import com.osp.core.repository.AdmDeptRepository;
import com.osp.core.service.BaseServiceImpl;
import com.osp.core.utils.QueryBuilder;
import com.osp.core.utils.QueryUtils;
import com.osp.core.utils.UtilsCommon;
import com.osp.notification.TelegramBot;
import com.osp.service.AdmDeptService;
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
public class AdmDeptServiceImpl extends BaseServiceImpl<AdmDept, AdmDeptRepository> implements AdmDeptService<AdmDept> {
    public AdmDeptServiceImpl(AdmDeptRepository repository) {
        super(repository);
    }

    @Autowired private AdmDeptRepository deptRepository;
    @PersistenceContext private EntityManager entityManager;
    @Autowired private MessageSource messageSource;
    @Autowired private TelegramBot telegramBot;

    @Override
    public Optional<AdmDept> edit(AdmDept dto) {
        AdmDept dept = get(dto.getId()).orElseThrow(() -> new BaseException(
                messageSource.getMessage("error.ENTITY_NOT_FOUND", new Object[]{"Dept"}, UtilsCommon.getLocale())
        ));
        return update(dto.formToBo(dto, dept));
    }

    @Override
    public HashMap initAddOrEdit(Long id) throws BadRequestException {
        HashMap map = new HashMap();
        if (id == null) {
            map.put("dept", new CmCommune());
        } else {
            Optional<AdmDept> bo = get(id);
            if (!bo.isPresent()) {
                throw new BadRequestException(messageSource.getMessage("error.ENTITY_NOT_FOUND", new Object[]{"Dept"}, UtilsCommon.getLocale()));
            }
            map.put("dept", bo);
        }
        return map;
    }

    public Page<ViewAdmDept> getPage(SearchForm searchObject, Pageable pageable) {
        Page<ViewAdmDept> page = null;
        try {
            List<ViewAdmDept> list = new ArrayList<>();
            String hql = " from ViewAdmDept u where 1=1 ";
            QueryBuilder builder = new QueryBuilder(entityManager, "select count(u)", new StringBuffer(hql), false);

            List<QueryBuilder.ConditionObject> conditionObjects = new ArrayList<>();
            conditionObjects.add(new QueryBuilder.ConditionObject(QueryUtils.EQ,"u.status", ConstantString.STATUS.active));
            conditionObjects.add(new QueryBuilder.ConditionObject(QueryUtils.EQ,"u.status", ConstantString.STATUS.lock));
            builder.andOrListNative(conditionObjects);

            if (StringUtils.isNotBlank(searchObject.getDeptName())) {
                builder.and(QueryUtils.LIKE, "u.deptName", "%" + searchObject.getDeptName().trim() + "%");
            }
            if (StringUtils.isNotBlank(searchObject.getDeptDesc())) {
                builder.and(QueryUtils.LIKE, "u.deptDesc", "%" + searchObject.getDeptDesc().trim() + "%");
            }
            if (StringUtils.isNotBlank(searchObject.getParentId())) {
                List<QueryBuilder.ConditionObject> objects = new ArrayList<>();
                objects.add(new QueryBuilder.ConditionObject(QueryUtils.EQ,"u.id", Long.parseLong(searchObject.getParentId().trim()) ));
                objects.add(new QueryBuilder.ConditionObject(QueryUtils.EQ,"u.parentId", Long.parseLong(searchObject.getParentId().trim()) ));
                builder.andOrListNative(objects);
            }
            builder.and(QueryUtils.EQ,"u.parentId", 0L);
            Query query = builder.initQuery(false);
            int count = Integer.parseInt(query.getSingleResult().toString());

            pageable.getSort().iterator().forEachRemaining(order -> {
                builder.addOrder(order.getProperty(), order.getDirection().isAscending() ? "ASC" : "DESC");
            });
            builder.addOrder("u.createdDate", QueryUtils.DESC);

            builder.setSubFix("select u");
            query = builder.initQuery(ViewAdmDept.class);
            if(pageable.getPageSize() > 0){
                query.setFirstResult(Integer.parseInt(String.valueOf(pageable.getOffset()))).setMaxResults(pageable.getPageSize());
            }
            list = query.getResultList();

            if (list != null) {
                page = new PageImpl<>(list, pageable, count);
            }

        } catch (Exception e) {
            e.printStackTrace();
            telegramBot.sendBotMessage("Lỗi tại AdmDeptServiceImpl.getPage " + e.getMessage());
        }
        return page;
    }

    @Override
    public List<AdmDept> deleteByIds(List<Long> ids) {
        List<AdmDept> users = deptRepository.loadByListIds(ids);
        for (AdmDept user: users) {
            user.setIsDelete(ConstantString.IS_DELETE.delete);
            deptRepository.save(user);
        }
        return users;
    }

    @Override
    public List<AdmDept> locks(List<Long> ids) {
        List<AdmDept> users = deptRepository.loadByListIds(ids);
        for (AdmDept user: users) {
            user.setStatus(ConstantString.STATUS.lock);
            deptRepository.save(user);
        }
        return users;
    }

    @Override
    public List<AdmDept> unlocks(List<Long> ids) {
        List<AdmDept> users = deptRepository.loadByListIds(ids);
        for (AdmDept user: users) {
            user.setStatus(ConstantString.STATUS.active);
            deptRepository.save(user);
        }
        return users;
    }

    @Override
    public List<AdmDept> findByParentIdAndStatusAndIsDelete(Long parentId, Long status, Long isDelete) {
        return deptRepository.findByParentIdAndStatusAndIsDelete(parentId, status, isDelete);
    }

    @Override
    public List<AdmDept> findByParentIdAndStatusInAndIsDelete(Long parentId, List<Long> status, Long isDelete) {
        return deptRepository.findByParentIdAndStatusInAndIsDelete(parentId, status, isDelete);
    }

}
