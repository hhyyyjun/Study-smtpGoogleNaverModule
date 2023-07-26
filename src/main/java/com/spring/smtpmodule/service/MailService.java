package com.spring.smtpmodule.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


/**
 * packageName    : com.spring.smtp.service
 * fileName       : MailService
 * author         : hj
 * date           : 2023-07-18
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-07-18        hj       최초 생성
 */
@Service
@RequiredArgsConstructor
public class MailService {

    //thymeleaf 템플릿을 이메일로 보내는 경우
    
    private final JavaMailSender mailSender;
//    private static final String FROM_ADDRESS = "gidwns617@naver.com"; //smtp 설정한 본인의 이메일만 가능하다. 네이버
    private static final String FROM_ADDRESS = "gidwns617@gmail.com";  //구글
    private final SpringTemplateEngine thymeleafTemplateEngine;
    
    //오픈밋
    //이메일 전송하는 경우
    //1. 예약 내역 작성 시
    //2. 회의실 예약 변경 시
    //3. 회의실 예약 삭제 시
    //파라미터 : 이메일 타입, 사원이메일, 사원이름, 예약날짜, 예약시간, 회의내용, 회의실종류 및 더 추가 필요

    
    @Transactional
    public void sendEmail(String mailType, List<String> userEmails, List<String> userNames, LocalDateTime selectDate) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        String reservateDate = selectDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); //예약 날짜
        String mailTitle = ""; //제목

        // create the Thymeleaf context object and add the name variable
        Context thymeleafContext = new Context();



        if (mailType.equals("reservation")){

            mailTitle = "<OPENMEET> 회의실 예약 안내입니다.";
        }
        else if (mailType.equals("reservationChange")){

            mailTitle = "<OPENMEET> 회의실 예약 변경 안내입니다.";
        }
        else if (mailType.equals("reservationDelete")){

            mailTitle = "<OPENMEET> 회의실 예약 삭제 안내입니다.";
        }
        thymeleafContext.setVariable("userEmails", userEmails); //초대된 사람들의 이메일
        thymeleafContext.setVariable("userNames", userNames); //초대된 사람들의 이름
        thymeleafContext.setVariable("reservateDate", reservateDate); //예약 날짜
//        thymeleafContext.setVariable("content", content); //예약 관련 내용

        // generate the HTML content from the Thymeleaf template
        String htmlContent = thymeleafTemplateEngine.process("/view/" + mailType + ".html", thymeleafContext);

        for(String userEmail : userEmails ){
            helper.setTo(userEmail);
            helper.setFrom(FROM_ADDRESS);
            helper.setSubject(mailTitle);
            helper.setText(htmlContent, true);
            mailSender.send(message);
            System.out.println("메일 전송 완료 --> " + userEmail);
        }
    }
}
