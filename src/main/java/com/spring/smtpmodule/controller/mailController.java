package com.spring.smtpmodule.controller;

import com.spring.smtpmodule.dto.MailDto;
import com.spring.smtpmodule.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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

    @PostMapping("/sendMail")
    @ResponseBody
    public ResponseEntity<?> sendMail(@RequestBody MailDto request) throws MessagingException, IOException {
        mailService.sendEmail(request.getMailType(), request.getUserEmail(), request.getUserName(), request.getCurrentDate());
        return ResponseEntity.ok(200);
    }
}
