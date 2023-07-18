package com.spring.smtpmodule;

import com.spring.smtpmodule.dto.MailDto;
import com.spring.smtpmodule.service.MailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import java.io.IOException;
import java.time.LocalDateTime;

@SpringBootTest
class SmtpmoduleApplicationTests {

	MailService mailService;
	JavaMailSender mailSender;
	SpringTemplateEngine thymeleafTemplateEngine;

	@BeforeEach
	public void beforeEach(){
		mailService  = new MailService(mailSender, thymeleafTemplateEngine);
	}

	@Test
	void 회원가입이메일() throws MessagingException, IOException {
		MailDto joinDto = new MailDto();
		LocalDateTime currentTime  = LocalDateTime.now();

		joinDto.setMailType("joinMail");
		joinDto.setUserEmail("jun950617@naver.com");
		joinDto.setUsername("이향준");
		joinDto.setCurrentDate(currentTime);

		mailService.sendEmail(joinDto.getMailType(), joinDto.getUserEmail(), joinDto.getUsername(), joinDto.getCurrentDate());
	}

	@Test
	void 계정휴면처리이메일() {
		MailDto blockDto = new MailDto();
	}

	@Test
	void 정보안내이메일() {
		MailDto joinDto = new MailDto();
	}
}
