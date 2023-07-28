package com.osp.service.impl;

import com.osp.core.contants.ConstantString;
import com.osp.core.dto.request.SearchForm;
import com.osp.core.dto.response.NzTreeData;
import com.osp.core.entity.AdmAuthorities;
import com.osp.core.entity.AdmGroup;
import com.osp.core.entity.view.ViewAdmGroup;
import com.osp.core.exception.BadRequestException;
import com.osp.core.repository.AdmAuthoritiesRepository;
import com.osp.core.repository.AdmGroupRepository;
import com.osp.core.service.BaseServiceImpl;
import com.osp.core.utils.QueryBuilder;
import com.osp.core.utils.QueryUtils;
import com.osp.core.utils.UtilsCommon;
import com.osp.notification.TelegramBot;
import com.osp.service.AdmGroupService;
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
public class AdmGroupServiceImpl extends BaseServiceImpl<AdmGroup, AdmGroupRepository> implements AdmGroupService<AdmGroup> {
    public AdmGroupServiceImpl(AdmGroupRepository repository) {
        super(repository);
    }

    @PersistenceContext private EntityManager entityManager;
    @Autowired private AdmGroupRepository groupRepository;
    @Autowired private AdmAuthoritiesRepository authoritiesRepository;
    @Autowired private MessageSource messageSource;
    @Autowired private TelegramBot telegramBot;

    public Page<ViewAdmGroup> getPage(SearchForm searchObject, Pageable pageable) {
        Page<ViewAdmGroup> page = null;
        try {
            List<ViewAdmGroup> list = new ArrayList<>();
            String hql = " from ViewAdmGroup u where 1=1 ";
            QueryBuilder builder = new QueryBuilder(entityManager, "select count(u)", new StringBuffer(hql), false);

            List<QueryBuilder.ConditionObject> conditionObjects = new ArrayList<>();
            conditionObjects.add(new QueryBuilder.ConditionObject(QueryUtils.EQ,"u.status", ConstantString.STATUS.active));
            conditionObjects.add(new QueryBuilder.ConditionObject(QueryUtils.EQ,"u.status", ConstantString.STATUS.lock));
            builder.andOrListNative(conditionObjects);

            if (StringUtils.isNotBlank(searchObject.getGroupName())) {
                builder.and(QueryUtils.LIKE, "u.groupName", "%" + searchObject.getGroupName().trim() + "%");
            }
            if (StringUtils.isNotBlank(searchObject.getDescription())) {
                builder.and(QueryUtils.LIKE, "u.description", "%" + searchObject.getDescription().trim() + "%");
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

            builder.setSubFix("select u");
            query = builder.initQuery(ViewAdmGroup.class);
            if(pageable.getPageSize() > 0){
                query.setFirstResult(Integer.parseInt(String.valueOf(pageable.getOffset()))).setMaxResults(pageable.getPageSize());
            }
            list = query.getResultList();

            if (list != null) {
                page = new PageImpl<>(list, pageable, count);
            }

        } catch (Exception e) {
            e.printStackTrace();
            telegramBot.sendBotMessage("Lỗi tại AdmGroupServiceImpl.getPage " + e.getMessage());
        }
        return page;
    }

    @Override
    public List<AdmGroup> deleteByIds(List<Long> ids) {
        List<AdmGroup> users = groupRepository.loadByListIds(ids);
        for (AdmGroup user: users) {
            user.setIsDelete(ConstantString.IS_DELETE.delete);
            groupRepository.save(user);
        }
        return users;
    }

    @Override
    public List<AdmGroup> locks(List<Long> ids) {
        List<AdmGroup> users = groupRepository.loadByListIds(ids);
        for (AdmGroup user: users) {
            user.setStatus(ConstantString.STATUS.lock);
            groupRepository.save(user);
        }
        return users;
    }

    @Override
    public List<AdmGroup> unlocks(List<Long> ids) {
        List<AdmGroup> users = groupRepository.loadByListIds(ids);
        for (AdmGroup user: users) {
            user.setStatus(ConstantString.STATUS.active);
            groupRepository.save(user);
        }
        return users;
    }

    @Override
    public HashMap initAdd() {
        HashMap response = new HashMap();
        List<AdmAuthorities> listParent = authoritiesRepository.loadListParent();
        List<AdmAuthorities> listChild = authoritiesRepository.loadListChild();

        List<NzTreeData> nzTreeData = new ArrayList<>();

        for (AdmAuthorities au: listParent) {
            NzTreeData treeParent = new NzTreeData();
            treeParent.setTitle(au.getDescription());
            treeParent.setKey(au.getId().toString());
            treeParent.setIsLeaf(false);
            nzTreeData.add(treeParent);
        }

        for (AdmAuthorities authorities: listChild) {
            for (NzTreeData au: nzTreeData) {
                if (authorities.getParentId().compareTo(Long.parseLong(au.getKey())) == 0) {
                    NzTreeData treeChild = new NzTreeData();
                    treeChild.setTitle(authorities.getDescription());
                    treeChild.setKey(authorities.getId().toString());
                    treeChild.setIsLeaf(true);
                    treeChild.setExpanded(true);
                    au.getChildren().add(treeChild);
                }
            }
        }

        List<NzTreeData> listRoot = new ArrayList<>();
        NzTreeData root = new NzTreeData("Danh sách chức năng", "-1", false, true, false, nzTreeData);
        listRoot.add(root);

        response.put("treeData", listRoot);
        return response;
    }

    @Override
    public AdmGroup addGroup(AdmGroup group) {
        group = groupRepository.save(group);

        if (group.getKeys() != null) {
            List<AdmAuthorities> listAuthorities = new ArrayList<>();
            for (Long idAuthority: group.getKeys()) {
                if (idAuthority.compareTo(-1L) == 0) {
                    listAuthorities.addAll(authoritiesRepository.findAll());
                } else {
                    Optional<AdmAuthorities> authorities = authoritiesRepository.checkParent(idAuthority);
                    if (authorities.isPresent()) {
                        listAuthorities.addAll(authoritiesRepository.getAllByParentId(idAuthority));
                    } else {
                        listAuthorities.add(authoritiesRepository.getById(idAuthority));
                    }
                }
            }
            group.setGroupAuthorities(listAuthorities);
            group = groupRepository.save(group);
        }
        return group;
    }

    @Override
    public AdmGroup editGroup(AdmGroup dto) throws BadRequestException {
        Optional<AdmGroup> groupOpt = groupRepository.findById(dto.getId());
        if (!groupOpt.isPresent()) {
            throw new BadRequestException(messageSource.getMessage("error.ENTITY_NOT_FOUND", new Object[]{"Entity"}, UtilsCommon.getLocale()));
        }

        AdmGroup group = groupOpt.get();
        if (group.getGroupAuthorities().size() > 0) {
            group.setGroupAuthorities(new ArrayList<>());
        }

        group = group.formToBo(dto, group);
        groupRepository.save(group);

        if (group.getKeys() != null) {
            List<AdmAuthorities> listAuthorities = new ArrayList<>();
            for (Long idAuthority: group.getKeys()) {
                if (idAuthority.compareTo(-1L) == 0) {
                    listAuthorities.addAll(authoritiesRepository.findAll());
                } else {
                    Optional<AdmAuthorities> authorities = authoritiesRepository.checkParent(idAuthority);
                    if (authorities.isPresent()) {
                        listAuthorities.addAll(authoritiesRepository.getAllByParentId(idAuthority));
                    } else {
                        listAuthorities.add(authoritiesRepository.getById(idAuthority));
                    }
                }
            }
            group.setGroupAuthorities(listAuthorities);
            group = groupRepository.save(group);
        }
        return group;
    }

    @Override
    public HashMap initEdit(Long groupId) {
        HashMap response = new HashMap();
        AdmGroup group = groupRepository.findById(groupId).get();
        response.put("group", group);

        List<AdmAuthorities> listParentOfGroup = authoritiesRepository.loadListParentByGroupId(groupId);
        List<AdmAuthorities> listChildOfGroup = authoritiesRepository.loadListChildByGroupId(groupId);

        List<AdmAuthorities> listParent = authoritiesRepository.loadListParent();
        List<AdmAuthorities> listChild = authoritiesRepository.loadListChild();

        List<NzTreeData> nzTreeData = new ArrayList<>();
        List<String> keys = new ArrayList<>();

        for (AdmAuthorities au: listParent) {
            NzTreeData treeParent = new NzTreeData();
            treeParent.setTitle(au.getDescription());
            treeParent.setKey(au.getId().toString());
            treeParent.setIsLeaf(false);

            for (AdmAuthorities parentOfGroup: listParentOfGroup) {
                if (parentOfGroup.getId().compareTo(au.getId()) == 0) {
                    treeParent.setChecked(true);
                    keys.add(treeParent.getKey());
                    break;
                }
            }
            // add parent node
            nzTreeData.add(treeParent);
        }

        for (AdmAuthorities authorities: listChild) {
            for (NzTreeData au : nzTreeData) {
                if (authorities.getParentId().compareTo(Long.parseLong(au.getKey())) == 0) {
                    NzTreeData treeChild = new NzTreeData();
                    treeChild.setTitle(authorities.getDescription());
                    treeChild.setKey(authorities.getId().toString());
                    treeChild.setIsLeaf(true);
                    treeChild.setExpanded(true);

                    for (AdmAuthorities childOfGroup: listChildOfGroup) {
                        if (childOfGroup.getId().compareTo(authorities.getId()) == 0) {
                            treeChild.setChecked(true);
                            keys.add(treeChild.getKey());
                            break;
                        }
                    }
                    // add children node
                    au.getChildren().add(treeChild);
                }
            }
        }

        response.put("keys", keys);

        List<NzTreeData> listRoot = new ArrayList<>();
        NzTreeData root = new NzTreeData("Danh sách chức năng", "-1", false, true, false, nzTreeData);
        listRoot.add(root);
        response.put("treeData", listRoot);
        return response;
    }

}
