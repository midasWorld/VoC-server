package com.fresh.voc.model.common;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fresh.voc.model.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "company")
@Entity
public class Company extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Pattern(regexp = "^[0-9]{3}-[0-9]{2}-[0-9]{5}$")
	private String registrationNo;

	@NotBlank
	@Size(max = 50)
	private String name;

	@NotNull
	@Enumerated(value = EnumType.STRING)
	private CompanyType type;

	@NotBlank
	@Pattern(regexp = "^([0-9]{2,3})-([0-9]{3,4})-([0-9]{4})$")
	private String phone;

	public Company(String registrationNo, String name, CompanyType type, String phone) {
		this.registrationNo = registrationNo;
		this.name = name;
		this.type = type;
		this.phone = phone;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("id", id)
			.append("registrationNo", registrationNo)
			.append("name", name)
			.append("type", type)
			.append("phone", phone)
			.toString();
	}
}