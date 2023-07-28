package com.osp.service.impl;

import com.osp.core.entity.FileImageExtract;
import com.osp.core.repository.FileImageExtractRepository;
import com.osp.core.service.BaseServiceImpl;
import com.osp.notification.TelegramBot;
import com.osp.service.FileImageExtractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class FileImageExtractServiceServiceImpl extends BaseServiceImpl<FileImageExtract, FileImageExtractRepository> implements FileImageExtractService<FileImageExtract> {
    public FileImageExtractServiceServiceImpl(FileImageExtractRepository repository) {
        super(repository);
    }

    @Autowired private TelegramBot telegramBot;
}
