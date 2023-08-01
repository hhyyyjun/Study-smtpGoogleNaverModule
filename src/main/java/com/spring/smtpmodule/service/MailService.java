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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    
    private final JavaMailSender javaMailSender;
//    private static final String FROM_ADDRESS = "gidwns617@naver.com"; //smtp 설정한 본인의 이메일만 가능하다. 네이버
    private static final String FROM_ADDRESS = "gidwns617@gmail.com";  //구글
    private final SpringTemplateEngine thymeleafTemplateEngine;
    
    //오픈밋
    //이메일 전송하는 경우
    //1. 예약 내역 작성 시
    //2. 회의실 예약 변경 시
    //3. 회의실 예약 삭제 시
    //파라미터 : 이메일 타입, 사원이메일, 사원이름, 예약날짜, 예약시간, 회의내용, 회의실종류 및 더 추가 필요

    //thymeleaf 템플릿으로 메일 보내는 경우
    @Transactional
    public void sendEmailThymeleaf(String mailType, List<String> userEmails, List<String> userNames, LocalDateTime selectDate) throws MessagingException, IOException {
        MimeMessage message = javaMailSender.createMimeMessage();
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
            javaMailSender.send(message);
            System.out.println("메일 전송 완료 --> " + userEmail);
        }
    }



    //html/jsp로 메일 보내는 경우
    public void sendMailJsp(String mailType, List<String> userMail, Long meetRoom, String meetDate, String startTime, String endTime, String meetTitle, String meetContent){
        List<String> userEmailList = new ArrayList<>(); //참여자 이메일 리스트

        for(String userMails : userMail) {
            userEmailList.add(userMails);
        }
        int toUserSize = userEmailList.size();

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            if(mailType.equals("reservation")) {
                mimeMessageHelper.setSubject("<OPENMEET> 회의실 예약 안내 메일입니다.");  //이메일 제목 설정
            }
            else if(mailType.equals("reservationUpdate")){
                mimeMessageHelper.setSubject("<OPENMEET> 회의실 예약변경 안내 메일입니다.");
            }
            else if(mailType.equals("reservationRemove")){
                mimeMessageHelper.setSubject("<OPENMEET> 회의실 예약취소 안내 메일입니다.");
            }

            mimeMessageHelper.setTo((String[]) userEmailList.toArray(new String[toUserSize]));

            String htmlContent = "<div style='width:500px;'>"
                    + "<h2  style='margin-left:10px;'>" + mimeMessageHelper.getMimeMessage().getSubject() + "</h2>"
                    + "<h3  style='margin-left:10px;'>" + "<strong>회의 안건</strong> : " + meetTitle + "</h3>"
                    + "<div style='padding:15px;font-size:16px;line-height:1.5;color:#555;background:#f1f4f6;'>"
                    + "<p>" + "<strong>회의실</strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + meetRoom + " 회의실" + "</p>"
                    + "<p style='margin-block:10px;'>" + "<strong>회의 날짜</strong>&nbsp;&nbsp;&nbsp;&nbsp;" + meetDate + "</p>"
                    + "<p style='margin-block:10px;'>" + "<strong>회의 시간</strong>&nbsp;&nbsp;&nbsp;&nbsp;" + startTime + " ~ " + endTime + "</p>"
                    + "<p style='margin-block:10px;'>" + "<strong>회의 내용</strong>&nbsp;&nbsp;&nbsp;&nbsp;" + meetContent + "</p>"
                    + "</div>"
                    + "</div>";
            mimeMessageHelper.setText(htmlContent, true); // true로 설정하여 HTML 형식으로 전송

            javaMailSender.send(mimeMessage);
            System.out.println("이메일이 성공적으로 전송되었습니다.");

        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("이메일 보내기 실패: " + e.getMessage());
        }
    }

    //html/jsp로 메일 보내는 경우(postman으로 테스트 완료)
    public void sendMailJspWithPostMan(String mailType, List<String> userMail, List<String> userName, Long meetRoom, LocalDateTime meetTime, String meetTitle, String meetContent){
        List<String> userEmailList = new ArrayList<>(); //참여자 이메일 리스트
        List<String> userList = new ArrayList<>(); //참여자 리스트
        LocalDate reservateDate = meetTime.toLocalDate(); //회의 날짜, localDateTime 사용하는 경우
        LocalTime reservateTime = meetTime.toLocalTime(); //회의 시간, localDateTime 사용하는 경우
        int hour = reservateTime.getHour(); //시간, localDateTime 사용하는 경우
        String minute = String.format("%02d", reservateTime.getMinute()); //분, localDateTime 사용하는 경우

        for(String userMails : userMail) {
            userEmailList.add(userMails);
        }
        for(String userNames : userName) {  //사람 이름까지 가져오는 경우
            userList.add(userNames);
        }
        int toUserSize = userEmailList.size();
        String userListOutput = String.join(", ", userList);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            if(mailType.equals("reservation")) {
                mimeMessageHelper.setSubject("<OPENMEET> 회의실 예약 안내 메일입니다.");  //이메일 제목 설정
            }
            else if(mailType.equals("reservationUpdate")){
                mimeMessageHelper.setSubject("<OPENMEET> 회의실 예약변경 안내 메일입니다.");
            }
            else if(mailType.equals("reservationRemove")){
                mimeMessageHelper.setSubject("<OPENMEET> 회의실 예약취소 안내 메일입니다.");
            }

            mimeMessageHelper.setTo((String[]) userEmailList.toArray(new String[toUserSize]));

            String htmlContent = "<div style='width:500px;'>"
                    + "<h2  style='margin-left:10px;'>" + mimeMessageHelper.getMimeMessage().getSubject() + "</h2>"
                    + "<h3  style='margin-left:10px;'>" + "<strong>회의 안건</strong> : " + meetTitle + "</h3>"
                    + "<div style='padding:15px;font-size:16px;line-height:1.5;color:#555;background:#f1f4f6;'>"
                    + "<p>" + "<strong>회의실</strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + meetRoom + " 회의실" + "</p>"
                    + "<p style='margin-block:10px;'>" + "<strong>회의 날짜</strong>&nbsp;&nbsp;&nbsp;&nbsp;" + reservateDate + "</p>" //localDateTime 사용하는 경우
                    + "<p style='margin-block:10px;'>" + "<strong>회의 시간</strong>&nbsp;&nbsp;&nbsp;&nbsp;" + hour + "시 " + minute + "분 ~ " + "</p>" //localDateTime 사용하는 경우
                    + "<p style='margin-block:10px;'>" + "<strong>회의 내용</strong>&nbsp;&nbsp;&nbsp;&nbsp;" + meetContent + "</p>"
                    + "<p>" + "<strong>회의 참가자</strong>&nbsp;" + userListOutput + "</p>"
                    + "</div>"
                    + "</div>";
            mimeMessageHelper.setText(htmlContent, true); // true로 설정하여 HTML 형식으로 전송

            javaMailSender.send(mimeMessage);
            System.out.println("이메일이 성공적으로 전송되었습니다.");

        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("이메일 보내기 실패: " + e.getMessage());
        }
    }
}
