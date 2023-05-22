package com.fresh.voc.model.common;

import java.util.Objects;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fresh.voc.model.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Person extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(min = 2, max = 50)
	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "company_id")
	private Company company;

	@Pattern(regexp = "^([0-9]{2,3})-([0-9]{3,4})-([0-9]{4})$")
	private String phone;

	public Person(String name, Company company, String phone) {
		this.name = name;
		this.company = company;
		this.phone = phone;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Person person = (Person)o;
		return Objects.equals(id, person.id)
			&& Objects.equals(name, person.name)
			&& Objects.equals(phone, person.phone)
			&& Objects.equals(company.getId(), person.company.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, company, phone);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("id", id)
			.append("name", name)
			.append("company", company)
			.append("phone", phone)
			.toString();
	}
}
