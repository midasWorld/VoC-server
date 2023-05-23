package com.fresh.voc.model.voc;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fresh.voc.model.BaseEntity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Penalty extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	private String content;

	@DecimalMin("0")
	private Long amount;

	@OneToOne(fetch = FetchType.LAZY)
	private Voc voc;

	@NotNull
	private Boolean confirmed;

	@NotNull
	private Boolean objected;

	public Penalty(String content, Long amount, Voc voc) {
		this(content, amount, voc, false, false);
	}

	public Penalty(String content, Long amount, Voc voc, Boolean confirmed, Boolean objected) {
		this.content = content;
		this.amount = amount;
		this.voc = voc;
		this.confirmed = confirmed;
		this.objected = objected;
	}

	//== 비지니스 로직 ==//
	public void confirm() {
		if (this.confirmed) {
			throw new IllegalArgumentException("penalty already confirmed.");
		}
		this.confirmed = true;
	}

	@Override
	public String toString() {
		return new org.apache.commons.lang3.builder.ToStringBuilder(this)
			.append("id", id)
			.append("content", content)
			.append("amount", amount)
			.append("voc", voc)
			.append("confirmed", confirmed)
			.append("objected", objected)
			.toString();
	}
}
