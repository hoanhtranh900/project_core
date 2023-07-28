package com.osp.service.impl;

import com.osp.core.entity.ExtOtpSession;
import com.osp.core.repository.ExtOtpSessionRepository;
import com.osp.core.service.BaseServiceImpl;
import com.osp.core.utils.UtilsDate;
import com.osp.core.utils.UtilsString;
import com.osp.notification.TelegramBot;
import com.osp.service.ExtOtpSessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ExtOtpSessionServiceImpl extends BaseServiceImpl<ExtOtpSession, ExtOtpSessionRepository> implements ExtOtpSessionService<ExtOtpSession> {
    public ExtOtpSessionServiceImpl(ExtOtpSessionRepository repository) {
        super(repository);
    }

    private static final Integer EXPIRE_MINS = 5;
    @Autowired private ExtOtpSessionRepository otpSessionRepository;
    @Autowired private TelegramBot telegramBot;

    @Override
    public String generateOTP(String receiver, Long typeOtp, int length) {
        String otp = UtilsString.OTP(length);
        ExtOtpSession otpSession = new ExtOtpSession();
        otpSession.setReceiver(receiver);
        otpSession.setCode(otp);
        Date newDate = UtilsDate.minuteDate(new Date(), EXPIRE_MINS);
        otpSession.setTimeExpired(newDate);
        otpSessionRepository.save(otpSession);
        return otp;
    }

    @Override
    public void clearOTP(String receiver, Long typeOtp) {
        List<ExtOtpSession> otpSessionList = otpSessionRepository.findByReceiverAndTypeOtp(receiver, typeOtp);
        for (ExtOtpSession otpSession : otpSessionList) {
            otpSessionRepository.delete(otpSession);
        }
    }

}
