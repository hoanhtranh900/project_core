package com.osp.core.utils;

import com.osp.core.contants.Constants;
import com.osp.core.dto.request.SentMailInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

/**
 * TODO: write you class description here
 *
 * @author
 */

@Component
public class UtilsMail {

    public static JavaMailSender emailSender;

    @Autowired
    public UtilsMail(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public static boolean sendMail(String mailTo, SentMailInfo mailInfo) {

        Properties props = new Properties();

        props.put("mail.smtp.host", Constants.mail_host);
        props.put("mail.smtp.port", Constants.mail_port);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props,new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Constants.mail_user_name, Constants.mail_pass_word);
            }
        });

        try {
            session.setDebug(Constants.mail_debug.equals("true"));
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(Constants.mail_user_name));

            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailTo));
            message.setSubject(mailInfo.getSubject(), "utf-8");
            message.setContent(mailInfo.getMailContent(), "text/html; charset=UTF-8");

            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean sendMailSpring(String mailTo, SentMailInfo mailInfo) {

        try {
            MimeMessage message = emailSender.createMimeMessage();

            boolean multipart = true;
            MimeMessageHelper helper = new MimeMessageHelper(message, multipart, "utf-8");

            helper.setTo(mailTo);
            helper.setSubject(mailInfo.getSubject());

            message.setContent(mailInfo.getMailContent(), "text/html");

            if(StringUtils.isNotBlank(mailInfo.getFilePaths())) {
                String[] paths = mailInfo.getFilePaths().split(",");
                for (String path : paths) {
                    FileSystemResource file = new FileSystemResource(new File(path));
                    helper.addAttachment("Txt file", file);
                }
            }

            emailSender.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String loadTemplate(String template, Map<String, Object> map) {
        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        ve.init();



        Template t = ve.getTemplate("template/" + template, "UTF-8");


        VelocityContext context = new VelocityContext();
        if (map != null) {
            H.each(new ArrayList<>(map.keySet()), (index, key) -> context.put(key, map.get(key)));
        }

        //utf-8
        StringWriter writer = new StringWriter();
        t.merge(context, writer);
        String str = writer.toString();
        return str;
    }
}
