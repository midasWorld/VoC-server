package com.fresh.voc.repository.common;

import static com.fresh.voc.model.common.CompanyType.CLIENT;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.fresh.voc.model.common.Company;
import com.fresh.voc.model.common.CompanyType;

import javax.validation.ValidationException;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class CompanyRepositoryTest {

	@Autowired
	CompanyRepository companyRepository;

	@Test
	@DisplayName("회사 등록에 성공한다.")
	void successCreateCompany() {
		//given
		String registrationNo = "111-11-11111";
		String name = "주".repeat(50);
		CompanyType type = CLIENT;
		String phone = "02-222-2222";
		Company company = new Company(registrationNo, name, type, phone);

		//when
		Company saved = companyRepository.save(company);

		//then
		assertThat(saved.getRegistrationNo(), is(registrationNo));
		assertThat(saved.getName(), is(name));
		assertThat(saved.getType(), is(type));
		assertThat(saved.getPhone(), is(phone));
	}

	@Test
	@DisplayName("사업자 등록번호가 Null 이면 실패한다")
	void failCreateCompany_nullRegistrationNo() {
		//given
		Company company = getDummyFromRegistNo(null);

		//when, then
		assertThrows(ValidationException.class, () -> {
			companyRepository.save(company);
		});
	}

	@Test
	@DisplayName("사업자 등록번호가 빈값이면 실패한다")
	void failCreateCompany_emptyRegistrationNo() {
		//given
		String registrationNo = "";
		Company company = getDummyFromRegistNo(registrationNo);

		//when, then
		assertThrows(ValidationException.class, () -> {
			companyRepository.save(company);
		});
	}

	@Test
	@DisplayName("사업자 등록번호 등록 시, 'xxx-xx-xxxxx' 형식에 일치하지 않으면 실패한다")
	void failCreateCompany_invalidRegistrationNoFormat() {
		//given
		String registrationNo = "1234567890";
		Company company = getDummyFromRegistNo(registrationNo);

		//when, then
		assertThrows(ValidationException.class, () -> {
			companyRepository.save(company);
		});
	}

	@Test
	@DisplayName("사업자 등록번호 등록 시, 규정 글자수보다 크면 실패한다")
	void failCreateCompany_overRegistrationNoLength() {
		//given
		String registrationNo = "111-11-111112";
		Company company = getDummyFromRegistNo(registrationNo);

		//when, then
		assertThrows(ValidationException.class, () -> {
			companyRepository.save(company);
		});
	}

	@Test
	@DisplayName("사업자 등록번호 등록 시, 숫자와 '-' 외의 글자가 포함되면 실패한다")
	void failCreateCompany_invalidCharacter() {
		//given
		String registrationNo = "a11-11-11111";
		Company company = getDummyFromRegistNo(registrationNo);

		//when, then
		assertThrows(ValidationException.class, () -> {
			companyRepository.save(company);
		});
	}

	@Test
	@DisplayName("회사 이름이 null 이면 실패한다")
	void failCreateCompany_nullName() {
		// given
		Company company = getDummyFromName(null);

		//when, then
		assertThrows(ValidationException.class, () -> {
			companyRepository.save(company);
		});
	}

	@Test
	@DisplayName("회사 이름이 빈값 이면 실패한다")
	void failCreateCompany_emptyName() {
		// given
		String name = "";
		Company company = getDummyFromName(name);

		//when, then
		assertThrows(ValidationException.class, () -> {
			companyRepository.save(company);
		});
	}

	@Test
	@DisplayName("회사 이름 등록 시, 50자 이상일 경우 실패한다")
	void failCreateCompany_overNameLength() {
	  // given
		String name = "주".repeat(51);
		Company company = getDummyFromName(name);

		//when, then
		assertThrows(ValidationException.class, () -> {
			companyRepository.save(company);
		});
	}
	
	@Test
	@DisplayName("회사 타입이 null 이면 실패한다")
	void failCreateCompany_nullType() {
	  // given
		Company company = new Company("111-11-11111", "dummy", null, "02-222-2222");

		//when, then
		assertThrows(ValidationException.class, () -> {
			companyRepository.save(company);
		});
	}

	@Test
	@DisplayName("회사 전화번호가 NULL이면 실패한다")
	void failCreateCompany_nullPhone() {
		// given
		Company company = getDummyFromPhone(null);

		//when, then
		assertThrows(ValidationException.class, () -> {
			companyRepository.save(company);
		});
	}
	
	@Test
	@DisplayName("회사 전화번호가 빈값이면 실패한다")
	void failCreateCompany_emptyPhone() {
	  // given
		Company company = getDummyFromPhone("");

		//when, then
		assertThrows(ValidationException.class, () -> {
			companyRepository.save(company);
		});
	}

	@Test
	@DisplayName("회사 전화번호가 규정 글자수보다 크면 실패한다")
	void failCreateCompany_overPhoneLength() {
		// given
		String phone = "010-1111-11111";
		Company company = getDummyFromPhone(phone);

		//when, then
		assertThrows(ValidationException.class, () -> {
			companyRepository.save(company);
		});
	}

	@Test
	@DisplayName("회사 전화번호가 'xxx-xx-xxxxx' 형식에 일치하지 않으면 실패한다")
	void failCreateCompany_invalidPhoneFormat() {
		// given
		String phone = "0101111111111";
		Company company = getDummyFromPhone(phone);

		//when, then
		assertThrows(ValidationException.class, () -> {
			companyRepository.save(company);
		});
	}

	private Company getDummyFromRegistNo(String registrationNo) {
		return new Company(registrationNo, "dummy", CLIENT, "02-222-2222");
	}

	private Company getDummyFromName(String name) {
		return new Company("111-11-11111", name, CLIENT, "02-222-2222");
	}

	private Company getDummyFromPhone(String phone) {
		return new Company("111-11-11111", "dummy", CLIENT, phone);
	}
}