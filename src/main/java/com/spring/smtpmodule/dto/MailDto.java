package com.spring.smtpmodule.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * packageName    : com.spring.smtp.dto
 * fileName       : UserDto
 * author         : hj
 * date           : 2023-07-18
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-07-18        hj       최초 생성
 */
@Data
@NoArgsConstructor
public class MailDto {

    private String mailType; //보내는 메일 종류
    private List<String> userEmail; //유저 메일
    private List<String> userName; //유저 이름
    private LocalDateTime selectDate; //예약 선택 날짜

}
