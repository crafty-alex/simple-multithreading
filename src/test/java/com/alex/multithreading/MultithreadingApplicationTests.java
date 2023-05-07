package com.alex.multithreading;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;


import java.util.concurrent.TimeUnit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.MOCK, classes = MultithreadingApplication.class)
@AutoConfigureMockMvc
class MultithreadingApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void when_do_request_1_return_200() throws Exception {
		mockMvc.perform(get("/do-request-1"))
				.andExpect(status().isOk())
				.andDo(print());
	}
	@Test
	void when_do_request_2_return_200() throws Exception {
		mockMvc.perform(get("/do-request-2"))
				.andExpect(status().isOk())
				.andExpect(request().asyncStarted())
				.andExpect(request().asyncResult("Result: {\n" +
						"  \"userId\": 1,\n" +
						"  \"id\": 1,\n" +
						"  \"title\": \"delectus aut autem\",\n" +
						"  \"completed\": false\n" +
						"}"))
				.andDo(print());
	}

}
