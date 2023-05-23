package com.fresh.voc.repository.voc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.samePropertyValuesAs;

import java.util.Optional;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import com.fresh.voc.model.common.Company;
import com.fresh.voc.model.common.CompanyType;
import com.fresh.voc.model.common.Person;
import com.fresh.voc.model.voc.DueType;
import com.fresh.voc.model.voc.Penalty;
import com.fresh.voc.model.voc.Voc;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class PenaltyRepositoryTest {

	@Autowired
	PenaltyRepository penaltyRepository;

	@Autowired
	EntityManager entityManager;

	@Test
	@Transactional
	@DisplayName("패널티 조회에 성공한다. By penaltyId, vocId")
	void successFindPenaltyByIdAndVocId() {
	  // given
		Company company = new Company("999-99-99999", "더미 회사", CompanyType.SHIPPING, "02-999-9999");
		Person person = new Person("더미 기사", company, "010-1111-1111");
		Voc voc = new Voc(DueType.SHIPPING, person, "다른 장소에 배송");
		Penalty penalty = new Penalty("물품 금액 전액 배상", 20000L, voc, true, false);

		entityManager.persist(company);
		entityManager.persist(person);
		entityManager.persist(voc);
		entityManager.persist(penalty);
		entityManager.flush();
		entityManager.clear();

	  // when
		Optional<Penalty> findPenalty = penaltyRepository.findByIdAndVocId(voc.getId(), penalty.getId());

		// then
	  assertThat(findPenalty.isEmpty(), is(false));
		assertThat(findPenalty.get(), samePropertyValuesAs(penalty));
	}

	@Test
	@DisplayName("해당 Voc 등록된 패널티가 있다면 True 를 반환한다.")
	void successExistsPenaltyByVoc_returnTrue() {
	  // given
		Company company = new Company("999-99-99999", "더미 회사", CompanyType.SHIPPING, "02-999-9999");
		Person person = new Person("더미 기사", company, "010-1111-1111");
		Voc voc = new Voc(DueType.SHIPPING, person, "다른 장소에 배송");
		Penalty penalty = new Penalty("물품 금액 전액 배상", 20000L, voc, false, false);

		entityManager.persist(company);
		entityManager.persist(person);
		entityManager.persist(voc);
		entityManager.persist(penalty);
		entityManager.flush();
		entityManager.clear();

	  // when
		boolean result = penaltyRepository.existsByVoc(voc);

		// then
		assertThat(result, is(true));
	}

	@Test
	@DisplayName("해당 Voc 등록된 패널티가 없다면 False 를 반환한다.")
	void successNotExistsPenaltyByVoc() {
		// given
		Company company = new Company("999-99-99999", "더미 회사", CompanyType.SHIPPING, "02-999-9999");
		Person person = new Person("더미 기사", company, "010-1111-1111");
		Voc voc = new Voc(DueType.SHIPPING, person, "다른 장소에 배송");

		entityManager.persist(company);
		entityManager.persist(person);
		entityManager.persist(voc);
		entityManager.flush();
		entityManager.clear();

		// when
		boolean result = penaltyRepository.existsByVoc(voc);

		// then
		assertThat(result, is(false));
	}
}