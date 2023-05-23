package com.fresh.voc.web.voc;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fresh.voc.service.CompensationService;
import com.fresh.voc.service.dto.CompensationCreateRequest;
import com.fresh.voc.web.ApiResult;

@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest({CompensationRestController.class})
class CompensationRestControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@MockBean
	CompensationService compensationService;

	@Autowired
	private WebApplicationContext ctx;

	@BeforeEach
	public void setUp() {
		// 반환 값 한글 깨지는 현상 해결을 위한 필터 추가
		this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
			.addFilters(new CharacterEncodingFilter("UTF-8", true))
			.alwaysDo(print())
			.build();
	}

	@Test
	@DisplayName("새로운 배상 등록에 성공하여 200 상태 코드를 반환한다.")
	void successCreateCompensation() throws Exception {
		// given
		Long compensationId = 1L;
		CompensationCreateRequest compensationCreateRequest = new CompensationCreateRequest(10000L, 1L);

		String request = objectMapper.writeValueAsString(compensationCreateRequest);
		String response = objectMapper.writeValueAsString(new ApiResult<>(true, compensationId, null));

		given(compensationService.create(compensationCreateRequest))
			.willReturn(compensationId);

		// when
		ResultActions resultActions = mockMvc.perform(
			post("/api/compensation")
				.contentType(APPLICATION_JSON)
				.content(request)
		).andDo(print());

		// then
		then(compensationService)
			.should()
			.create(compensationCreateRequest);

		resultActions
			.andExpect(status().isOk())
			.andExpect(content().string(response));
	}

	@Test
	@DisplayName("배상 등록 시, 마이너스 금액이면 실패하고 400 코드를 반환한다.")
	void failCreateCompensation_lessThanZeroAmount() throws Exception {
		// given
		Long invalidAmount = -1L;
		CompensationCreateRequest compensationCreateRequest = new CompensationCreateRequest(invalidAmount, 1L);

		String request = objectMapper.writeValueAsString(compensationCreateRequest);

		given(compensationService.create(compensationCreateRequest))
			.willReturn(1L);

		// when
		ResultActions resultActions = mockMvc.perform(
			post("/api/compensation")
				.contentType(APPLICATION_JSON)
				.content(request)
		).andDo(print());

		// then
		then(compensationService)
			.should(never())
			.create(compensationCreateRequest);

		resultActions
			.andExpect(status().isBadRequest())
			.andExpect(handler().handlerType(CompensationRestController.class))
			.andExpect(handler().methodName("create"))
			.andExpect(jsonPath("$.success", is(false)))
			.andExpect(jsonPath("$.response", nullValue()))
			.andExpect(jsonPath("$.error", notNullValue()));
	}

	@Test
	@DisplayName("배상 등록 시, VocId 가 Null 이면 실패하고 400 코드를 반환한다.")
	void failCreateCompensation_nullVocId() throws Exception {
		// given
		Long invalidVocId = null;
		CompensationCreateRequest compensationCreateRequest = new CompensationCreateRequest(20000L, invalidVocId);

		String request = objectMapper.writeValueAsString(compensationCreateRequest);

		given(compensationService.create(compensationCreateRequest))
			.willReturn(1L);

		// when
		ResultActions resultActions = mockMvc.perform(
			post("/api/compensation")
				.contentType(APPLICATION_JSON)
				.content(request)
		).andDo(print());

		// then
		then(compensationService)
			.should(never())
			.create(compensationCreateRequest);

		resultActions
			.andExpect(status().isBadRequest())
			.andExpect(handler().handlerType(CompensationRestController.class))
			.andExpect(handler().methodName("create"))
			.andExpect(jsonPath("$.success", is(false)))
			.andExpect(jsonPath("$.response", nullValue()))
			.andExpect(jsonPath("$.error", notNullValue()));
	}
}