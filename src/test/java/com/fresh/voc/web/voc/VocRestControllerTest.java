package com.fresh.voc.web.voc;

import static com.fresh.voc.model.voc.DueType.SHIPPING;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.stream.LongStream;

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
import com.fresh.voc.model.common.Person;
import com.fresh.voc.model.voc.Compensation;
import com.fresh.voc.model.voc.Penalty;
import com.fresh.voc.model.voc.Voc;
import com.fresh.voc.service.VocService;
import com.fresh.voc.service.dto.PenaltyCreateRequest;
import com.fresh.voc.service.dto.VocCreateRequest;
import com.fresh.voc.service.dto.VocDto;
import com.fresh.voc.web.ApiResult;

@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest({VocRestController.class})
class VocRestControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@MockBean
	VocService vocService;

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
	@DisplayName("VOC 목록 조회에 성공한다.")
	void successGetAllVoc() throws Exception {
		// given
		List<VocDto> allVoc = LongStream.range(1, 2).mapToObj(index -> {
			Person person = new Person("더미 기사", null, "010-222-2222");
			Voc voc = new Voc(SHIPPING, person, "배송 잘못 옴");
			Penalty penalty = new Penalty("배상금 지급", 10000L, voc, true, false);
			Compensation compensation = new Compensation(10000L, voc);
			return new VocDto(voc, penalty, compensation);
		}).collect(toList());

		String response = objectMapper.writeValueAsString(
			new ApiResult<>(true, allVoc, null)
		);

		given(vocService.getAllVoc())
			.willReturn(allVoc);

		// when
		ResultActions resultActions = mockMvc.perform(
			get("/api/voc")
				.contentType(APPLICATION_JSON)
		).andDo(print());

		// then
		verify(vocService, times(1)).getAllVoc();

		resultActions
			.andExpect(status().isOk())
			.andExpect(content().string(response));
	}
	
	@Test
	@DisplayName("새로운 VOC 등록에 성공하여 200 상태 코드를 반환한다.")
	void successCreateVoc() throws Exception {
	  // given
		Long vocId = 1L;
		VocCreateRequest vocCreateRequest = new VocCreateRequest(SHIPPING, 1L, "배송 하루 늦음");

		String request = objectMapper.writeValueAsString(vocCreateRequest);
		String response = objectMapper.writeValueAsString(new ApiResult<>(true, vocId, null));

		given(vocService.create(vocCreateRequest)).willReturn(vocId);

	  // when
		ResultActions resultActions = mockMvc.perform(
			post("/api/voc")
				.contentType(APPLICATION_JSON)
				.content(request)
		).andDo(print());

		// then
		verify(vocService, times(1))
			.create(vocCreateRequest);

		resultActions
			.andExpect(status().isOk())
			.andExpect(content().string(response));
	}

	@Test
	@DisplayName("VOC 등록 시, dueType Null 이면 실패하고 400 코드를 반환한다.")
	void failCreateVoc_nullDueType() throws Exception {
		// given
		VocCreateRequest vocCreateRequest = new VocCreateRequest(null, 1L, "배송 하루 늦음");

		String request = objectMapper.writeValueAsString(vocCreateRequest);

		// when
		ResultActions resultActions = mockMvc.perform(
			post("/api/voc")
				.contentType(APPLICATION_JSON)
				.content(request)
		).andDo(print());

		// then
		resultActions
			.andExpect(status().isBadRequest())
			.andExpect(handler().handlerType(VocRestController.class))
			.andExpect(handler().methodName("create"))
			.andExpect(jsonPath("$.success", is(false)))
			.andExpect(jsonPath("$.response", nullValue()))
			.andExpect(jsonPath("$.error", notNullValue()));
	}

	@Test
	@DisplayName("VOC 등록 시, dueTargetId Null 이면 실패하고 400 코드를 반환한다.")
	void failCreateVoc_nullDueTargetId() throws Exception {
		// given
		VocCreateRequest vocCreateRequest = new VocCreateRequest(SHIPPING, null, "배송 하루 늦음");

		String request = objectMapper.writeValueAsString(vocCreateRequest);

		// when
		ResultActions resultActions = mockMvc.perform(
			post("/api/voc")
				.contentType(APPLICATION_JSON)
				.content(request)
		).andDo(print());

		// then
		resultActions
			.andExpect(status().isBadRequest())
			.andExpect(handler().handlerType(VocRestController.class))
			.andExpect(handler().methodName("create"))
			.andExpect(jsonPath("$.success", is(false)))
			.andExpect(jsonPath("$.response", nullValue()))
			.andExpect(jsonPath("$.error", notNullValue()));
	}

	@Test
	@DisplayName("VOC 등록 시, dueReason Null 이면 실패하고 400 코드를 반환한다.")
	void failCreateVoc_nullDueReason() throws Exception {
		// given
		VocCreateRequest vocCreateRequest = new VocCreateRequest(SHIPPING, 1L, null);

		String request = objectMapper.writeValueAsString(vocCreateRequest);

		// when
		ResultActions resultActions = mockMvc.perform(
			post("/api/voc")
				.contentType(APPLICATION_JSON)
				.content(request)
		).andDo(print());

		// then
		resultActions
			.andExpect(status().isBadRequest())
			.andExpect(handler().handlerType(VocRestController.class))
			.andExpect(handler().methodName("create"))
			.andExpect(jsonPath("$.success", is(false)))
			.andExpect(jsonPath("$.response", nullValue()))
			.andExpect(jsonPath("$.error", notNullValue()));
	}

	@Test
	@DisplayName("VOC 등록 시, dueReason 빈값 이면 실패하고 400 코드를 반환한다.")
	void failCreateVoc_emptyDueReason() throws Exception {
		// given
		VocCreateRequest vocCreateRequest = new VocCreateRequest(SHIPPING, 1L, "");

		String request = objectMapper.writeValueAsString(vocCreateRequest);

		// when
		ResultActions resultActions = mockMvc.perform(
			post("/api/voc")
				.contentType(APPLICATION_JSON)
				.content(request)
		).andDo(print());

		// then
		resultActions
			.andExpect(status().isBadRequest())
			.andExpect(handler().handlerType(VocRestController.class))
			.andExpect(handler().methodName("create"))
			.andExpect(jsonPath("$.success", is(false)))
			.andExpect(jsonPath("$.response", nullValue()))
			.andExpect(jsonPath("$.error", notNullValue()));
	}

	@Test
	@DisplayName("VOC 등록 시, 존재하지 않는 사원이면 실패하고 400 코드를 반환한다.")
	void failCreateVoc_notExistsPerson() throws Exception {
		// given
		Long johnDoeId = 99999L;
		VocCreateRequest vocCreateRequest = new VocCreateRequest(SHIPPING, johnDoeId, "배송 하루 늦음");

		String request = objectMapper.writeValueAsString(vocCreateRequest);

		String errorMessage = "존재하지 않는 사원 ID 입니다.";

		doThrow(new IllegalArgumentException(errorMessage))
			.when(vocService).create(vocCreateRequest);

		// when
		ResultActions resultActions = mockMvc.perform(
			post("/api/voc")
				.contentType(APPLICATION_JSON)
				.content(request)
		).andDo(print());

		verify(vocService, times(1))
			.create(vocCreateRequest);

		// then
		resultActions
			.andExpect(status().isBadRequest())
			.andExpect(handler().handlerType(VocRestController.class))
			.andExpect(handler().methodName("create"))
			.andExpect(jsonPath("$.success", is(false)))
			.andExpect(jsonPath("$.response", nullValue()))
			.andExpect(jsonPath("$.error", notNullValue()))
			.andExpect(jsonPath("$.error.message", is(errorMessage)));
	}

	@Test
	@DisplayName("패널티 등록에 성공하고 200 코드를 반환한다.")
	void successCreatePenalty() throws Exception {
	  // given
		Long vocId = 1L;
		Long expectedPenaltyId = 1L;
		PenaltyCreateRequest penaltyCreateRequest = new PenaltyCreateRequest("배상금 지급", 10000L);

		String request = objectMapper.writeValueAsString(penaltyCreateRequest);
		String response = objectMapper.writeValueAsString(new ApiResult<>(true, expectedPenaltyId, null));

		given(vocService.createPenalty(vocId, penaltyCreateRequest))
			.willReturn(expectedPenaltyId);

		// when
		ResultActions resultActions = mockMvc.perform(
			post("/api/voc/{vocId}/penalties", vocId)
				.contentType(APPLICATION_JSON)
				.content(request)
		);

		// then
		verify(vocService, times(1))
			.createPenalty(vocId, penaltyCreateRequest);

		resultActions
			.andExpect(status().isOk())
			.andExpect(handler().handlerType(VocRestController.class))
			.andExpect(handler().methodName("createPenalty"))
			.andExpect(content().string(response));
	}

	@Test
	@DisplayName("패널티 등록 시, 존재하지 않는 VOC 인 경우 실패하고 400 코드를 반환한다.")
	void failCreatePenalty_notExistsVoc() throws Exception {
		// given
		Long vocId = 1L;
		PenaltyCreateRequest penaltyCreateRequest = new PenaltyCreateRequest("배상금 지급", 10000L);

		String request = objectMapper.writeValueAsString(penaltyCreateRequest);

		doThrow(IllegalArgumentException.class)
			.when(vocService).createPenalty(vocId, penaltyCreateRequest);

		// when
		ResultActions resultActions = mockMvc.perform(
			post("/api/voc/{vocId}/penalties", vocId)
				.contentType(APPLICATION_JSON)
				.content(request)
		).andDo(print());

		// then
		resultActions
			.andExpect(status().isBadRequest())
			.andExpect(handler().handlerType(VocRestController.class))
			.andExpect(handler().methodName("createPenalty"))
			.andExpect(jsonPath("$.success", is(false)))
			.andExpect(jsonPath("$.response", nullValue()))
			.andExpect(jsonPath("$.error", notNullValue()));
	}

	//
	@Test
	@DisplayName("패널티 등록 시, 패널티 내용이 Null 이면 실패하고 400 코드를 반환한다.")
	void failCreatePenalty_nullContent() throws Exception {
		// given
		Long vocId = 1L;
		String invalidContent = null;
		PenaltyCreateRequest penaltyCreateRequest = new PenaltyCreateRequest(invalidContent, 10000L);
		String request = objectMapper.writeValueAsString(penaltyCreateRequest);

		// when
		ResultActions resultActions = mockMvc.perform(
			post("/api/voc/{vocId}/penalties", vocId)
				.contentType(APPLICATION_JSON)
				.content(request)
		).andDo(print());

		// then
		resultActions
			.andExpect(status().isBadRequest())
			.andExpect(handler().handlerType(VocRestController.class))
			.andExpect(handler().methodName("createPenalty"))
			.andExpect(jsonPath("$.success", is(false)))
			.andExpect(jsonPath("$.response", nullValue()))
			.andExpect(jsonPath("$.error", notNullValue()));
	}

	@Test
	@DisplayName("패널티 등록 시, 패널티 내용이 빈값이면 실패하고 400 코드를 반환한다.")
	void failCreatePenalty_emptyContent() throws Exception {
		// given
		Long vocId = 1L;
		String invalidContent = "";
		PenaltyCreateRequest penaltyCreateRequest = new PenaltyCreateRequest(invalidContent, 10000L);
		String request = objectMapper.writeValueAsString(penaltyCreateRequest);

		// when
		ResultActions resultActions = mockMvc.perform(
			post("/api/voc/{vocId}/penalties", vocId)
				.contentType(APPLICATION_JSON)
				.content(request)
		).andDo(print());

		// then
		resultActions
			.andExpect(status().isBadRequest())
			.andExpect(handler().handlerType(VocRestController.class))
			.andExpect(handler().methodName("createPenalty"))
			.andExpect(jsonPath("$.success", is(false)))
			.andExpect(jsonPath("$.response", nullValue()))
			.andExpect(jsonPath("$.error", notNullValue()));
	}

	@Test
	@DisplayName("패널티 등록 시, 패널티 금액이 0 미망니면 실패하고 400 코드를 반환한다.")
	void failCreatePenalty_lessThanZeroAmount() throws Exception {
		// given
		Long vocId = 1L;
		Long invalidAmount = -1L;
		PenaltyCreateRequest penaltyCreateRequest = new PenaltyCreateRequest("배상금 지급", invalidAmount);
		String request = objectMapper.writeValueAsString(penaltyCreateRequest);

		// when
		ResultActions resultActions = mockMvc.perform(
			post("/api/voc/{vocId}/penalties", vocId)
				.contentType(APPLICATION_JSON)
				.content(request)
		).andDo(print());

		// then
		resultActions
			.andExpect(status().isBadRequest())
			.andExpect(handler().handlerType(VocRestController.class))
			.andExpect(handler().methodName("createPenalty"))
			.andExpect(jsonPath("$.success", is(false)))
			.andExpect(jsonPath("$.response", nullValue()))
			.andExpect(jsonPath("$.error", notNullValue()));
	}

	@Test
	@DisplayName("패널티 승인 등록에 성공하고 200 코드를 반환한다.")
	void successPenaltyConfirmed() throws Exception {
		// given
		Long vocId = 1L;
		Long penaltyId = 1L;

		doNothing()
			.when(vocService).confirmPenalty(vocId, penaltyId);

		// when
		ResultActions resultActions = mockMvc.perform(
			patch("/api/voc/{vocId}/penalties/{penaltyId}/confirm", vocId, penaltyId)
				.contentType(APPLICATION_JSON)
		);

		// then
		verify(vocService, times(1))
			.confirmPenalty(vocId, penaltyId);

		resultActions
			.andExpect(status().isOk())
			.andExpect(handler().handlerType(VocRestController.class))
			.andExpect(handler().methodName("confirmPenalty"))
			.andExpect(content().string(""));
	}

	@Test
	@DisplayName("존재하지 않는 패널티 승인 등록 시, 실패하고 400코드를 반환한다.")
	void failPenaltyConfirmed_notExistsPenalty() throws Exception {
		// given
		Long vocId = 1L;
		Long penaltyId = 1L;

		doThrow(IllegalArgumentException.class)
			.when(vocService).confirmPenalty(vocId, penaltyId);

		// when
		ResultActions resultActions = mockMvc.perform(
			patch("/api/voc/{vocId}/penalties/{penaltyId}/confirm", vocId, penaltyId)
				.contentType(APPLICATION_JSON)
		);

		// then
		verify(vocService, times(1))
			.confirmPenalty(vocId, penaltyId);

		resultActions
			.andExpect(status().isBadRequest())
			.andExpect(handler().handlerType(VocRestController.class))
			.andExpect(handler().methodName("confirmPenalty"))
			.andExpect(jsonPath("$.success", is(false)))
			.andExpect(jsonPath("$.response", nullValue()))
			.andExpect(jsonPath("$.error", notNullValue()));
	}
}