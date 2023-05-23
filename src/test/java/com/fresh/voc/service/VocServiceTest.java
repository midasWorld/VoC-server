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
import com.fresh.voc.service.dto.PenaltyCreateRequest;
import com.fresh.voc.service.dto.VocCreateRequest;
import com.fresh.voc.service.dto.VocSearchDto;

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
		Penalty penalty = getPenalty(voc, true);
		Compensation compensation = new Compensation(20000L, voc);

		entityManager.persist(company);
		entityManager.persist(person);
		entityManager.persist(voc);
		entityManager.persist(penalty);
		entityManager.persist(compensation);
		entityManager.flush();
		entityManager.clear();

		VocSearchDto expected = new VocSearchDto(voc, penalty, compensation);

		// when
		List<VocSearchDto> allVoc = vocService.getAllVoc();

	  // then
	  assertThat(allVoc.size(), greaterThanOrEqualTo(1));
		VocSearchDto lastVoc = allVoc.get(allVoc.size() - 1);
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

	@Test
	@DisplayName("패널티 등록에 성공한다.")
	void successCreatePenalty() {
	  // given
		Company company = getNewCompany();
		Person person = getNewPerson(company);
		Voc voc = getNewVoc(person);

		entityManager.persist(company);
		entityManager.persist(person);
		entityManager.persist(voc);

		String content = "배상금 지급";
		Long amount = 10000L;
		PenaltyCreateRequest request = new PenaltyCreateRequest(content, amount);

		// when
		Long savedPenaltyId = vocService.createPenalty(voc.getId(), request);

		// then
	  assertThat(savedPenaltyId, notNullValue());
		Penalty penalty = entityManager.find(Penalty.class, savedPenaltyId);
		assertThat(penalty.getId(), is(savedPenaltyId));
		assertThat(penalty.getContent(), is(content));
		assertThat(penalty.getAmount(), is(amount));
		assertThat(penalty.getVoc(), samePropertyValuesAs(voc));
	}

	@Test
	@DisplayName("존재하지 않는 vocId로 패널티 등록 실패한다.")
	void failCreatePenalty_notExistsVocId() {
	  // given
		Company company = getNewCompany();
		Person person = getNewPerson(company);

		entityManager.persist(company);
		entityManager.persist(person);

		Long invalidVocId = 99999L;
		PenaltyCreateRequest request = new PenaltyCreateRequest("배상금 지급", 10000L);

	  // when, then
		assertThrows(IllegalArgumentException.class, () -> {
			vocService.createPenalty(invalidVocId, request);
		});
	}

	@Test
	@DisplayName("해당 VOC 에 패널티가 이미 존재하면 실패한다.")
	void failCreatePenalty_alreadyPenaltyExists() {
		// given
		Company company = getNewCompany();
		Person person = getNewPerson(company);
		Voc voc = getNewVoc(person);
		Penalty penalty = getPenalty(voc, false);

		entityManager.persist(company);
		entityManager.persist(person);
		entityManager.persist(voc);
		entityManager.persist(penalty);

		PenaltyCreateRequest request = new PenaltyCreateRequest("배상금 지급", 10000L);

		// when, then
		assertThrows(IllegalArgumentException.class, () -> {
			vocService.createPenalty(voc.getId(), request);
		});
	}

	@Test
	@DisplayName("패널티 내용이 Null 이면 패널티 등록 실패한다.")
	void failCreatePenalty_nullContent() {
		// given
		Company company = getNewCompany();
		Person person = getNewPerson(company);
		Voc voc = getNewVoc(person);

		entityManager.persist(company);
		entityManager.persist(person);
		entityManager.persist(voc);

		String invalidContent = null;
		PenaltyCreateRequest request = new PenaltyCreateRequest(invalidContent, 10000L);

		// when, then
		assertThrows(ValidationException.class, () -> {
			vocService.createPenalty(voc.getId(), request);
		});
	}

	@Test
	@DisplayName("패널티 내용이 빈값 이면 패널티 등록 실패한다.")
	void failCreatePenalty_emptyContent() {
		// given
		Company company = getNewCompany();
		Person person = getNewPerson(company);
		Voc voc = getNewVoc(person);

		entityManager.persist(company);
		entityManager.persist(person);
		entityManager.persist(voc);

		String invalidContent = "";
		PenaltyCreateRequest request = new PenaltyCreateRequest(invalidContent, 10000L);

		// when, then
		assertThrows(ValidationException.class, () -> {
			vocService.createPenalty(voc.getId(), request);
		});
	}

	@Test
	@DisplayName("패널티 비용이 0 미만이면 패널티 등록 실패한다.")
	void failCreatePenalty_lessThanZeroAmount() {
		// given
		Company company = getNewCompany();
		Person person = getNewPerson(company);
		Voc voc = getNewVoc(person);

		entityManager.persist(company);
		entityManager.persist(person);
		entityManager.persist(voc);

		Long invalidAmount = -1L;
		PenaltyCreateRequest request = new PenaltyCreateRequest("배상금 지급", invalidAmount);

		// when, then
		assertThrows(ValidationException.class, () -> {
			vocService.createPenalty(voc.getId(), request);
		});
	}

	@Test
	@DisplayName("패널티 승인 등록에 성공한다.")
	void successPenaltyConfirmed() {
		// given
		Company company = getNewCompany();
		Person person = getNewPerson(company);
		Voc voc = getNewVoc(person);
		Penalty penalty = getPenalty(voc, false);

		entityManager.persist(company);
		entityManager.persist(person);
		entityManager.persist(voc);
		entityManager.persist(penalty);

		// when
		vocService.confirmPenalty(voc.getId(), penalty.getId());
		entityManager.flush();
		entityManager.clear();

		// then
		Penalty findPenalty = entityManager.find(Penalty.class, penalty.getId());
		assertThat(findPenalty, samePropertyValuesAs(penalty));
		assertThat(findPenalty.getConfirmed(), is(true));
	}

	@Test
	@DisplayName("존재하지 않는 vocId로 패널티 승인 등록에 실패한다.")
	void failPenaltyConfirmed_notExistsVocId() {
		// given
		Company company = getNewCompany();
		Person person = getNewPerson(company);
		Voc voc = getNewVoc(person);
		Penalty penalty = getPenalty(voc, false);

		entityManager.persist(company);
		entityManager.persist(person);
		entityManager.persist(voc);
		entityManager.persist(penalty);

		Long invalidVocId = 99999L;

		// when, then
		assertThrows(IllegalArgumentException.class, () -> {
			vocService.confirmPenalty(invalidVocId, penalty.getId());
		});
	}

	@Test
	@DisplayName("존재하지 않는 penaltyId로 패널티 승인 등록에 실패한다.")
	void failPenaltyConfirmed_notExistsPenaltyId() {
		// given
		Company company = getNewCompany();
		Person person = getNewPerson(company);
		Voc voc = getNewVoc(person);
		Penalty penalty = getPenalty(voc, false);

		entityManager.persist(company);
		entityManager.persist(person);
		entityManager.persist(voc);
		entityManager.persist(penalty);

		Long invalidPenaltyId = 99999L;

		// when, then
		assertThrows(IllegalArgumentException.class, () -> {
			vocService.confirmPenalty(voc.getId(), invalidPenaltyId);
		});
	}

	@Test
	@DisplayName("이미 승인된 경우 패널티 승인 등록에 실패한다.")
	void failPenaltyConfirmed_alreadyConfirmed() {
		// given
		Company company = getNewCompany();
		Person person = getNewPerson(company);
		Voc voc = getNewVoc(person);
		Penalty penalty = getPenalty(voc, true);

		entityManager.persist(company);
		entityManager.persist(person);
		entityManager.persist(voc);
		entityManager.persist(penalty);

		// when, then
		assertThrows(IllegalArgumentException.class, () -> {
			vocService.confirmPenalty(voc.getId(), penalty.getId());
		});
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

	private Penalty getPenalty(Voc voc, boolean confirmed) {
		return new Penalty("물품 금액 전액 배상", 20000L, voc, confirmed, false);
	}
}