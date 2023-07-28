package com.osp.service.impl;

import com.osp.core.entity.ActionHistory;
import com.osp.core.repository.ActionHistoryRepository;
import com.osp.core.service.BaseServiceImpl;
import com.osp.notification.TelegramBot;
import com.osp.service.ActionHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ActionHistoryServiceImpl extends BaseServiceImpl<ActionHistory, ActionHistoryRepository> implements ActionHistoryService<ActionHistory> {
    public ActionHistoryServiceImpl(ActionHistoryRepository repository) {
        super(repository);
    }

    @Autowired private TelegramBot telegramBot;
}
