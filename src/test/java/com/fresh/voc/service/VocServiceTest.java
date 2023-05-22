package com.fresh.voc.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.samePropertyValuesAs;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.fresh.voc.model.common.Company;
import com.fresh.voc.model.common.CompanyType;
import com.fresh.voc.model.common.Person;
import com.fresh.voc.model.voc.Compensation;
import com.fresh.voc.model.voc.DueType;
import com.fresh.voc.model.voc.Penalty;
import com.fresh.voc.model.voc.Voc;
import com.fresh.voc.service.dto.VocDto;

@Transactional
@SpringBootTest
class VocServiceTest {

	@Autowired
	VocService vocService;

	@Autowired
	EntityManager entityManager;
	
	@Test
	@DisplayName("VOC 전체 조회에 성공한다.")
	void successGetAllVoc() {
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

		VocDto expected = new VocDto(voc, penalty, compensation);

		// when
		List<VocDto> allVoc = vocService.getAllVoc();

	  // then
	  assertThat(allVoc.size(), greaterThanOrEqualTo(1));
		VocDto lastVoc = allVoc.get(allVoc.size() - 1);
		assertThat(lastVoc, samePropertyValuesAs(expected, "compensation"));
		assertThat(lastVoc.getCompensation(), samePropertyValuesAs(expected.getCompensation()));
	}
}