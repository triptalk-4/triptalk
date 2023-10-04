package com.zero.triptalk;

import com.zero.triptalk.user.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class LoginTests {

	@Test
	void contextLoads() {
	}

	/**
	 * 랜덤 코드 생성 확인
	 */
	@Test
	public void testCreateRandomString() {
		String randomString = AuthenticationService.createRandomString();

		// 생성된 문자열의 길이는 6이어야 합니다.
		assertEquals(6, randomString.length());

		// 생성된 문자열은 영문 대문자, 소문자, 숫자로만 이루어져야 합니다.
		assertTrue(randomString.matches("[A-Za-z0-9]+"));
	}




}
