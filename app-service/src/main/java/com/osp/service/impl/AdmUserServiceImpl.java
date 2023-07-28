package com.osp.service.impl;

import com.osp.core.contants.ConstantString;
import com.osp.core.dto.request.SearchForm;
import com.osp.core.entity.AdmUser;
import com.osp.core.entity.view.ViewAdmUser;
import com.osp.core.exception.BadRequestException;
import com.osp.core.exception.BaseException;
import com.osp.core.repository.AdmUserRepository;
import com.osp.core.service.BaseServiceImpl;
import com.osp.core.utils.QueryBuilder;
import com.osp.core.utils.QueryUtils;
import com.osp.core.utils.UtilsCommon;
import com.osp.notification.TelegramBot;
import com.osp.service.AdmUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
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
public class AdmUserServiceImpl extends BaseServiceImpl<AdmUser, AdmUserRepository> implements AdmUserService<AdmUser> {
    public AdmUserServiceImpl(AdmUserRepository repository) {
        super(repository);
    }

    @Autowired private MessageSource messageSource;
    @Autowired private AdmUserRepository admUserRepository;
    @PersistenceContext private EntityManager entityManager;
    @Autowired private TelegramBot telegramBot;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public Page<ViewAdmUser> getPage(SearchForm searchObject, Pageable pageable) {
        Page<ViewAdmUser> page = null;
        try {
            List<ViewAdmUser> list = new ArrayList<>();
            String hql = " from ViewAdmUser u where 1=1 ";
            QueryBuilder builder = new QueryBuilder(entityManager, "select count(u)", new StringBuffer(hql), false);

            List<QueryBuilder.ConditionObject> conditionObjects = new ArrayList<>();
            conditionObjects.add(new QueryBuilder.ConditionObject(QueryUtils.EQ,"u.status", ConstantString.STATUS.active));
            conditionObjects.add(new QueryBuilder.ConditionObject(QueryUtils.EQ,"u.status", ConstantString.STATUS.lock));
            builder.andOrListNative(conditionObjects);

            if (StringUtils.isNotBlank(searchObject.getUsername())) {
                builder.and(QueryUtils.LIKE, "u.username", "%" + searchObject.getUsername().trim() + "%");
            }
            if (StringUtils.isNotBlank(searchObject.getPhoneNumber())) {
                builder.and(QueryUtils.LIKE, "u.phoneNumber", "%" + searchObject.getPhoneNumber().trim() + "%");
            }
            if (StringUtils.isNotBlank(searchObject.getStatus())) {
                builder.and(QueryUtils.EQ, "u.status", Long.parseLong(searchObject.getStatus().trim()));
            }
            if (StringUtils.isNotBlank(searchObject.getEmail())) {
                builder.and(QueryUtils.LIKE, "u.email", "%" + searchObject.getEmail().trim() + "%");
            }

            Query query = builder.initQuery(false);
            int count = Integer.parseInt(query.getSingleResult().toString());

            pageable.getSort().iterator().forEachRemaining(order -> {
                builder.addOrder(order.getProperty(), order.getDirection().isAscending() ? "ASC" : "DESC");
            });
            builder.addOrder("u.createdDate", QueryUtils.DESC);

            builder.setSubFix("select u");
            query = builder.initQuery(ViewAdmUser.class);
            if(pageable.getPageSize() > 0){
                query.setFirstResult(Integer.parseInt(String.valueOf(pageable.getOffset()))).setMaxResults(pageable.getPageSize());
            }
            list = query.getResultList();

            if (list != null) {
                page = new PageImpl<>(list, pageable, count);
            }

        } catch (Exception e) {
            e.printStackTrace();
            telegramBot.sendBotMessage("Lỗi tại AdmUserServiceImpl.getPage " + e.getMessage());
        }
        return page;
    }

    @Override
    public List<AdmUser> deleteByIds(List<Long> ids) {
        List<AdmUser> users = admUserRepository.loadByListIds(ids);
        for (AdmUser user: users) {
            user.setIsDelete(ConstantString.IS_DELETE.delete);
            admUserRepository.save(user);
        }
        return users;
    }

    @Override
    public List<AdmUser> locks(List<Long> ids) {
        List<AdmUser> users = admUserRepository.loadByListIds(ids);
        for (AdmUser user: users) {
            user.setStatus(ConstantString.STATUS.lock);
            admUserRepository.save(user);
        }
        return users;
    }

    @Override
    public List<AdmUser> unlocks(List<Long> ids) {
        List<AdmUser> users = admUserRepository.loadByListIds(ids);
        for (AdmUser user: users) {
            user.setStatus(ConstantString.STATUS.active);
            admUserRepository.save(user);
        }
        return users;
    }

    @Override
    public AdmUser profile(Long id) {
        AdmUser user = get(id).orElseThrow(() -> new BaseException(messageSource.getMessage("error.ENTITY_NOT_FOUND", new Object[]{"User"}, UtilsCommon.getLocale())));
        return user;
    }

    @Override
    public Optional<AdmUser> updateProfile(AdmUser form) {
        // lấy user DB
        AdmUser user = get(form.getId()).orElseThrow(() -> new BaseException(messageSource.getMessage("error.ENTITY_NOT_FOUND", new Object[]{"User"}, UtilsCommon.getLocale())));
        // sét lại data cần sửa
        user = user.updateProfile(form, user);
        // save lại thông tin
        return save(user);
    }

    @Override
    public Optional<AdmUser> changePass(AdmUser form) throws BadRequestException {
        AdmUser user = get(form.getId()).orElseThrow(() -> new BaseException(messageSource.getMessage("error.ENTITY_NOT_FOUND", new Object[]{"User"}, UtilsCommon.getLocale())));
        if (form.getPassword() == null || form.getRePassWord() == null || !form.getPassword().equals(form.getRePassWord())) {
            throw new BadRequestException(messageSource.getMessage("error.ENTITY_NOT_FOUND", new Object[]{"Authorities"}, UtilsCommon.getLocale()));
        }
        if (passwordEncoder.matches(form.getOldPassWord(), user.getPassword())) {
            throw new BadRequestException(messageSource.getMessage("MSG_PW_OLD_INVALID", null, UtilsCommon.getLocale()));
        }
        user.setPassword(passwordEncoder.encode(form.getPassword()));
        return update(user);
    }
}
