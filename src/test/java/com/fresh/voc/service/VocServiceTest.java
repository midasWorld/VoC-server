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
import com.fresh.voc.service.dto.VocCreateRequest;
import com.fresh.voc.service.dto.VocDto;

@Transactional
@SpringBootTest
class VocServiceTest {

	@Autowired
	VocService vocService;

	@Autowired
	EntityManager entityManager;
	
	@Test
	@Transactional
	@DisplayName("VOC 전체 조회에 성공한다.")
	void successGetAllVoc() {
		// given
		Company company = getNewCompany();
		Person person = getNewPerson(company);
		Voc voc = getNewVoc(person);
		Penalty penalty = new Penalty("물품 금액 전액 배상", 20000L, voc, true, false);
		Compensation compensation = new Compensation(20000L, voc);

		entityManager.persist(company);
		entityManager.persist(person);
		entityManager.persist(voc);
		entityManager.persist(penalty);
		entityManager.persist(compensation);
		entityManager.flush();
		entityManager.clear();

		VocDto expected = new VocDto(voc, penalty, compensation);

		// when
		List<VocDto> allVoc = vocService.getAllVoc();

	  // then
	  assertThat(allVoc.size(), greaterThanOrEqualTo(1));
		VocDto lastVoc = allVoc.get(allVoc.size() - 1);
		assertThat(lastVoc, samePropertyValuesAs(expected, "compensation"));
		assertThat(lastVoc.getCompensation(), samePropertyValuesAs(expected.getCompensation()));
	}
	
	@Test
	@Transactional
	@DisplayName("새로운 Voc 등록에 성공한다")
	void successCreate() {
	  // given
		Company company = getNewCompany();
		Person person = getNewPerson(company);
		Voc voc = getNewVoc(person);

		entityManager.persist(company);
		entityManager.persist(person);

		VocCreateRequest request = new VocCreateRequest(
			voc.getDueType(),
			voc.getDueTarget().getId(),
			voc.getDueReason()
		);

		// when
		Long savedVocId = vocService.create(request);

		// then
	  assertThat(savedVocId, notNullValue());
		Voc savedVoc = entityManager.find(Voc.class, savedVocId);
		assertThat(savedVoc, notNullValue());
		assertThat(savedVoc.getDueType(), is(voc.getDueType()));
		assertThat(savedVoc.getDueTarget(), is(voc.getDueTarget()));
		assertThat(savedVoc.getDueReason(), is(voc.getDueReason()));
	}

	private Company getNewCompany() {
		return new Company("999-99-99999", "더미 회사", CompanyType.SHIPPING, "02-999-9999");
	}

	private Person getNewPerson(Company company) {
		return new Person("더미 기사", company, "010-1111-1111");
	}

	private Voc getNewVoc(Person person) {
		return new Voc(SHIPPING, person, "다른 장소에 배송");
	}
	
	@Test
	@DisplayName("VOC 등록 시, 존재하지 않는 사원 ID로 등록하면 실패 한다.")
	void failCreate_notExistsPerson() {
	  // given
		Long johnDoeId = 99999L;
		VocCreateRequest request = new VocCreateRequest(
			SHIPPING,
			johnDoeId,
			"배송 안왔음"
		);

		// when, then
		assertThrows(IllegalArgumentException.class, () -> {
			vocService.create(request);
		});
	}

	@Test
	@DisplayName("VOC 등록 시, 귀책 대상이 Null 이면 실패한다.")
	void failCreate_nullDueType() {
		// given
		Company company = getNewCompany();
		Person person = getNewPerson(company);
		Voc voc = getNewVoc(person);

		entityManager.persist(company);
		entityManager.persist(person);

		VocCreateRequest request = new VocCreateRequest(
			null,
			person.getId(),
			"배송 안왔음"
		);

		// when, then
		assertThrows(ValidationException.class, () -> {
			vocService.create(request);
		});
	}

	@Test
	@DisplayName("VOC 등록 시, 귀책 이유가 Null 이면 실패한다.")
	void failCreate_nullDueReason() {
		// given
		Company company = getNewCompany();
		Person person = getNewPerson(company);
		Voc voc = getNewVoc(person);

		entityManager.persist(company);
		entityManager.persist(person);

		VocCreateRequest request = new VocCreateRequest(
			SHIPPING,
			person.getId(),
			null
		);

		// when, then
		assertThrows(ValidationException.class, () -> {
			vocService.create(request);
		});
	}

	@Test
	@DisplayName("VOC 등록 시, 귀책 이유가 빈값 이면 실패한다.")
	void failCreate_emptyDueReason() {
		// given
		Company company = getNewCompany();
		Person person = getNewPerson(company);
		Voc voc = getNewVoc(person);

		entityManager.persist(company);
		entityManager.persist(person);

		VocCreateRequest request = new VocCreateRequest(
			SHIPPING,
			person.getId(),
			""
		);

		// when, then
		assertThrows(ValidationException.class, () -> {
			vocService.create(request);
		});
	}
}