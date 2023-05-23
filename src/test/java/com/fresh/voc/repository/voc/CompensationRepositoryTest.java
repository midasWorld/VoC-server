package com.fresh.voc.repository.voc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;

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
class CompensationRepositoryTest {

	@Autowired
	CompensationRepository compensationRepository;

	@Autowired
	EntityManager entityManager;

	@Test
	@DisplayName("해당 Voc 등록된 배상이 있다면 True 를 반환한다.")
	void successExistsCompensationByVoc_returnTrue() {
		// given
		Company company = new Company("999-99-99999", "더미 회사", CompanyType.SHIPPING, "02-999-9999");
		Person person = new Person("더미 기사", company, "010-1111-1111");
		Voc voc = new Voc(DueType.SHIPPING, person, "다른 장소에 배송");
		Penalty penalty = new Penalty("물품 금액 전액 배상", 20000L, voc, false, false);
		Compensation compensation = new Compensation(20000L, voc);

		entityManager.persist(company);
		entityManager.persist(person);
		entityManager.persist(voc);
		entityManager.persist(penalty);
		entityManager.persist(compensation);
		entityManager.flush();
		entityManager.clear();
		
		// when
		boolean result = compensationRepository.existsByVoc(voc);

		// then
		assertThat(result, is(true));
	}

	@Test
	@DisplayName("해당 Voc 등록된 배상이 없다면 False 를 반환한다.")
	void successNotExistsPenaltyByVoc_returnFalse() {
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
		boolean result = compensationRepository.existsByVoc(voc);

		// then
		assertThat(result, is(false));
	}
}