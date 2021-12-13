package com.sr.mail.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import com.sr.mail.dto.MailRequest;
import com.sr.mail.dto.MailResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;


import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service
public class MailService {
    @Autowired
    private JavaMailSender sender;
    @Autowired
    private Configuration config;

    public MailResponse sendMail(MailRequest request, Map<String, Object> model) {

        MailResponse response = new MailResponse();
        MimeMessage message = sender.createMimeMessage();

        try {
            // set MediaType
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            // Add Attachment
//            helper.addAttachment("blog_img.png", new ClassPathResource("blog_img.png"));
            Template t = config.getTemplate("email-template.ftl");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
            helper.setTo(request.getTo());
            helper.setText(html, true);
            helper.setSubject(request.getSubject());
            helper.setFrom(request.getFrom());
            sender.send(message);
            response.setMessage("Mail Send to : " + request.getTo());
            response.setStatus(Boolean.TRUE);

        } catch (MessagingException | IOException | TemplateException e) {
            response.setMessage("Mail Sending Failure : " + e.getMessage());
            response.setStatus(Boolean.FALSE);
        }

        return response;
    }

    public String generateRandomNumber(String nameEn) {
        String code = nameEn.substring(0, 1);
        LocalDate date = LocalDate.now();
        String[] part = date.toString().split("-");
        String year = part[0].substring(2, 4);
        String month = part[1];
        code = code  + year +   month + code;

        return code;
    }

}
