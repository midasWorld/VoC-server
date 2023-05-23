package com.fresh.voc.service;

import static com.fresh.voc.model.voc.DueType.SHIPPING;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import javax.persistence.EntityManager;
import javax.validation.ValidationException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.fresh.voc.model.common.Company;
import com.fresh.voc.model.common.CompanyType;
import com.fresh.voc.model.common.Person;
import com.fresh.voc.model.voc.Compensation;
import com.fresh.voc.model.voc.Penalty;
import com.fresh.voc.model.voc.Voc;
import com.fresh.voc.service.dto.CompensationCreateRequest;
import com.fresh.voc.service.dto.CompensationSearchDto;

@Transactional
@SpringBootTest
class CompensationServiceTest {

	@Autowired
	CompensationService compensationService;

	@Autowired
	EntityManager entityManager;

	@Test
	@Transactional
	@DisplayName("배상 전체 조회에 성공한다.")
	void successGetAllCompensation() {
	  // given
		Company company = new Company("999-99-99999", "더미 회사", CompanyType.SHIPPING, "02-999-9999");
		Person person = new Person("더미 기사", company, "010-1111-1111");
		Voc voc = new Voc(SHIPPING, person, "다른 장소에 배송");
		Compensation compensation = new Compensation(20000L, voc);

		entityManager.persist(company);
		entityManager.persist(person);
		entityManager.persist(voc);
		entityManager.persist(compensation);
		entityManager.flush();
		entityManager.clear();

		CompensationSearchDto expected = new CompensationSearchDto(compensation);

	  // when
		List<CompensationSearchDto> allCompensation = compensationService.getAllCompensation();

		// then
		assertThat(allCompensation.size(), greaterThanOrEqualTo(1));
		CompensationSearchDto lastCompensation = allCompensation.get(allCompensation.size() - 1);
		assertThat(lastCompensation.getAmouont(), is(compensation.getAmount()));
		assertThat(lastCompensation.getVoc(), samePropertyValuesAs(expected.getVoc()));
	}

	@Test
	@Transactional
	@DisplayName("배상 정보 등록에 성공한다.")
	void successCreateCompensation() {
	  // given
		Company company = new Company("999-99-99999", "더미 회사", CompanyType.SHIPPING, "02-999-9999");
		Person person = new Person("더미 기사", company, "010-1111-1111");
		Voc voc = new Voc(SHIPPING, person, "다른 장소에 배송");
		Penalty penalty = new Penalty("물품 금액 전액 배상", 20000L, voc, true, false);

		entityManager.persist(company);
		entityManager.persist(person);
		entityManager.persist(voc);
		entityManager.persist(penalty);

		CompensationCreateRequest request = new CompensationCreateRequest(20000L, voc.getId());

		// when
		Long compensationId = compensationService.create(request);

		// then
		assertThat(compensationId, notNullValue());
		Compensation compensation = entityManager.find(Compensation.class, compensationId);
		assertThat(compensation.getAmount(), is(request.getAmount()));
		assertThat(compensation.getVoc().getId(), is(voc.getId()));
	}

	@Test
	@Transactional
	@DisplayName("배상 등록 시, 존재하지 않는 Voc 인 경우 실패한다.")
	void failCreateCompensation_notExistsVoc() {
		// given
		Company company = new Company("999-99-99999", "더미 회사", CompanyType.SHIPPING, "02-999-9999");
		Person person = new Person("더미 기사", company, "010-1111-1111");
		Voc voc = new Voc(SHIPPING, person, "다른 장소에 배송");
		Penalty penalty = new Penalty("물품 금액 전액 배상", 20000L, voc, true, false);

		entityManager.persist(company);
		entityManager.persist(person);
		entityManager.persist(voc);
		entityManager.persist(penalty);

		Long invalidVocId = 99999L;
		CompensationCreateRequest request = new CompensationCreateRequest(20000L, invalidVocId);

		// when, then
		assertThrows(IllegalArgumentException.class, () -> {
			compensationService.create(request);
		});
	}
	
	@Test
	@DisplayName("해당 VOC 에 배상이 이미 존재하면 실패한다.")
	void failCreateCompensation_alreadyCompensationExists() {
	  // given
		Company company = new Company("999-99-99999", "더미 회사", CompanyType.SHIPPING, "02-999-9999");
		Person person = new Person("더미 기사", company, "010-1111-1111");
		Voc voc = new Voc(SHIPPING, person, "다른 장소에 배송");
		Penalty penalty = new Penalty("물품 금액 전액 배상", 20000L, voc, true, false);
		Compensation compensation = new Compensation(20000L, voc);

		entityManager.persist(company);
		entityManager.persist(person);
		entityManager.persist(voc);
		entityManager.persist(penalty);
		entityManager.persist(compensation);

		CompensationCreateRequest request = new CompensationCreateRequest(20000L, voc.getId());

		// when, then
		assertThrows(IllegalArgumentException.class, () -> {
			compensationService.create(request);
		});
	}

	@Test
	@Transactional
	@DisplayName("배상 등록 시, 금액이 마이너스인 경우 실패한다.")
	void failCreateCompensation_minusAmount() {
		// given
		Company company = new Company("999-99-99999", "더미 회사", CompanyType.SHIPPING, "02-999-9999");
		Person person = new Person("더미 기사", company, "010-1111-1111");
		Voc voc = new Voc(SHIPPING, person, "다른 장소에 배송");
		Penalty penalty = new Penalty("물품 금액 전액 배상", 20000L, voc, true, false);

		entityManager.persist(company);
		entityManager.persist(person);
		entityManager.persist(voc);
		entityManager.persist(penalty);

		Long invalidAmount = -1L;
		CompensationCreateRequest request = new CompensationCreateRequest(invalidAmount, voc.getId());

		// when, then
		assertThrows(ValidationException.class, () -> {
			compensationService.create(request);
		});
	}
}