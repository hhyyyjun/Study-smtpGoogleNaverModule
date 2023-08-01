package com.spring.smtpmodule.controller;

import com.spring.smtpmodule.dto.MailDto;
import com.spring.smtpmodule.dto.MailDtoJsp;
import com.spring.smtpmodule.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * packageName    : com.spring.smtp.controller
 * fileName       : mailController
 * author         : hj
 * date           : 2023-07-18
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-07-18        hj       최초 생성
 */
@Controller
@RequiredArgsConstructor
public class mailController {

    private final MailService mailService;

    @GetMapping("/")
    public String main(){
        return "view/mailsend";
    }

    @PostMapping("/sendMailWithThymeleaf")
    @ResponseBody
    public ResponseEntity<?> sendMail(@RequestBody MailDto request) throws MessagingException, IOException {
        mailService.sendEmailThymeleaf(request.getMailType(), request.getUserEmail(), request.getUserName(), request.getSelectDate());
        return ResponseEntity.ok(200);
    }

    @PostMapping("/sendEmailWithJsp")
    @ResponseBody
    public ResponseEntity<?> mailToPeople(@RequestBody MailDtoJsp request) {
//        mailService.sendMailJspWithPostMan(request.getMailType(), request.getReceiveMail(), request.getUserName(), request.getMeetRoom(), request.getMeetTime(), request.getMeetTitle(), request.getMeetContent());
        mailService.sendMailJsp(request.getMailType(), request.getReceiveMail(), request.getMeetRoom(), request.getMeetDate(), request.getStartTime(), request.getEndTime(), request.getMeetTitle(), request.getMeetContent());
        return ResponseEntity.ok(200);
    }
}
