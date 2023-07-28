package com.osp.service.impl;

import com.osp.notification.TelegramBot;
import com.osp.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class EmailServiceImpl implements EmailService {
    @Autowired private TelegramBot telegramBot;
}
