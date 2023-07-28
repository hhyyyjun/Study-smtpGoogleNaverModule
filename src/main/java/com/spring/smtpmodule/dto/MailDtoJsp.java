package com.spring.smtpmodule.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * packageName    : com.spring.smtpmodule.dto
 * fileName       : MailDtoJsp
 * author         : hj
 * date           : 2023-07-28
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-07-28        hj       최초 생성
 */
@Data
public class MailDtoJsp {

    private String mailType; //이메일 타입
    private List<String> receiveMail; //초대할 인원 이메일
    private List<String> userName; //초대할 인원 이름
    private Long meetRoom; //회의실 번호
    private LocalDateTime meetTime; //회의 시간, 날짜
    private String meetTitle; //회의 제목
    private String meetContent; //회의 내용
}
