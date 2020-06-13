package com.subway;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest
class HomeControllerTest {

	@Autowired
	private MockMvc mockMvc;	//MockMvc 주입
	
	@Test
	public void testHomePage() throws Exception {
		mockMvc.perform(get("/"))	//GET "/" 요청 수행
		.andExpect(status().isOk())	//HTTP200상태 확인
		.andExpect(view().name("home"))	//뷰 페이지 이름 확인
		.andExpect(content().string(containsString("Welcome to...")));	//뷰 페이지 콘텐츠에 문자 "Welcome to..." 포함확인
	}

	

}
