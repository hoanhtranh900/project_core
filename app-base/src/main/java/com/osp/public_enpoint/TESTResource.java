package com.osp.public_enpoint;

import com.osp.core.contants.Constants;
import com.osp.core.dto.request.*;
import com.osp.core.dto.response.GetNumberPagesOfPdfResponse;
import com.osp.core.entity.*;
import com.osp.core.utils.UtilsCommon;
import com.osp.core.utils.UtilsMail;
import com.osp.notification.TelegramBot;
import com.osp.service.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping(value = "/public", produces = MediaType.APPLICATION_JSON_VALUE)
public class TESTResource {
    @Autowired private MessageSource messageSource;
    @Autowired private ActionHistoryService<ActionHistory> actionHistoryService;
    @Autowired private AdmAuthoritiesService<AdmAuthorities> admAuthoritiesService;
    @Autowired private AdmDeptService<AdmDept> admDeptService;
    @Autowired private AdmGroupService<AdmGroup> groupService;
    @Autowired private AdmParaSystemService<AdmParaSystem> paraSystemService;
    @Autowired private AdmRightService<AdmRight> admRightService;
    @Autowired private AdmUserService<AdmUser> admUserService;
    @Autowired private FileAttachDocumentService<FileAttachDocument> attachDocumentService;
    @Autowired private FileAttachmentService<FileAttachment> attachmentService;
    @Autowired private FileImageExtractService<FileImageExtract> fileImageExtractService;
    @Autowired private TelegramBot telegramBot;

    @ApiOperation(value = "api check lỗi could not parse json", notes = Constants.NOTE_API + "empty_note", response = Object.class, authorizations = {@Authorization(value = Constants.API_KEY)})
    @GetMapping("/test")
    public Object testEntity() {
        HashMap<Integer, Object> map = new HashMap();
        map.put(1, actionHistoryService.get(1L));
        map.put(2, admAuthoritiesService.get(1L));
        map.put(3, admDeptService.get(1L));
        map.put(4, groupService.get(1L));
        map.put(5, paraSystemService.get(1L));
        map.put(6, admRightService.get(1L));
        map.put(7, admUserService.get(1L));
        map.put(8, attachDocumentService.get(1L));
        map.put(9, attachmentService.get(1L));
        map.put(10, fileImageExtractService.get(1L));
        UtilsMail.sendMail("sonth290896@gmail.com", SentMailInfo.builder().mailContent("1").subject("1").build());
        telegramBot.sendBotMessage("TEST THÔNG BÁO");
        return map;
    }

}
