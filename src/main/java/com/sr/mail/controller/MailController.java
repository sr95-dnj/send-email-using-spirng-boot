package com.sr.mail.controller;

import com.sr.mail.dto.MailRequest;
import com.sr.mail.dto.MailResponse;
import com.sr.mail.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@Controller
public class MailController {

    @Autowired
    private MailService mailService;

    @GetMapping(path = "/home")
    public String home(){
        return "home";
    }

    @PostMapping(path = "/send")
    public String sendMail(MailRequest request){
        Map<String, Object> model = new HashMap<>();
        model.put("Name", request.getName());
        model.put("Address", "Rangpur, Dinajpur");
        model.put("OTP", mailService.generateRandomNumber(request.getName()));
         mailService.sendMail(request, model);
        return "opt-page";
    }
}
