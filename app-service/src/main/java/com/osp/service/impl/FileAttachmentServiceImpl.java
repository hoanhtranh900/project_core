package com.osp.service.impl;

import com.osp.core.entity.FileAttachment;
import com.osp.core.repository.FileAttachmentRepository;
import com.osp.core.service.BaseServiceImpl;
import com.osp.notification.TelegramBot;
import com.osp.service.FileAttachmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class FileAttachmentServiceImpl extends BaseServiceImpl<FileAttachment, FileAttachmentRepository> implements FileAttachmentService<FileAttachment> {
    public FileAttachmentServiceImpl(FileAttachmentRepository repository) {
        super(repository);
    }

    @Autowired private TelegramBot telegramBot;
}
