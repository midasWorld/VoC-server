package com.fresh.voc.repository.voc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.samePropertyValuesAs;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.fresh.voc.model.common.Company;
import com.fresh.voc.model.common.CompanyType;
import com.fresh.voc.model.common.Person;
import com.fresh.voc.model.voc.Compensation;
import com.fresh.voc.model.voc.DueType;
import com.fresh.voc.model.voc.Penalty;
import com.fresh.voc.model.voc.Voc;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class VocRepositoryTest {
	
	@Autowired
	VocRepository vocRepository;

	@Autowired
	EntityManager entityManager;

	@Test
	@Transactional
	@DisplayName("VOC 패치 조인 조회가 성공한다. (with 사원, 패널티, 배상)")
	void successGetVocAll() {
	  // given
		Company company = new Company("999-99-99999", "더미 회사", CompanyType.SHIPPING, "02-999-9999");
		Person person = new Person("더미 기사", company, "010-1111-1111");
		Voc voc = new Voc(DueType.SHIPPING, person, "다른 장소에 배송");
		Penalty penalty = new Penalty("물품 금액 전액 배상", 20000L, voc, true, false);
		Compensation compensation = new Compensation(20000L, voc);

		entityManager.persist(company);
		entityManager.persist(person);
		entityManager.persist(voc);
		entityManager.persist(penalty);
		entityManager.persist(compensation);
		entityManager.flush();
		entityManager.clear();

		// when
		List<Voc> allVoc = vocRepository.findAllWithPersonAndPenaltyAndCompensation();
	  
	  // then
		assertThat(allVoc.size(), greaterThanOrEqualTo(2));
		Voc lastVoc = allVoc.get(allVoc.size() - 1);
		assertThat(lastVoc, samePropertyValuesAs(voc, "penalty", "compensation"));
		assertThat(lastVoc.getDueTarget(), samePropertyValuesAs(person, "company"));
		assertThat(lastVoc.getPenalty(), samePropertyValuesAs(penalty));
		assertThat(lastVoc.getCompensation(), samePropertyValuesAs(compensation));
	}
}