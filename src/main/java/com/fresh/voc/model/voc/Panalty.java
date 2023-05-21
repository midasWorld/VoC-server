package com.fresh.voc.model.voc;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Panalty {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@DecimalMin("0")
	private Long amount;

	@OneToOne(fetch = FetchType.LAZY)
	private Voc voc;

	@NotNull
	private Boolean confirmed;

	@NotNull
	private Boolean objected;

	public Panalty(Long amount, Voc voc, Boolean confirmed, Boolean objected) {
		this.amount = amount;
		this.voc = voc;
		this.confirmed = confirmed;
		this.objected = objected;
	}

	@Override
	public String toString() {
		return new org.apache.commons.lang3.builder.ToStringBuilder(this)
			.append("id", id)
			.append("amount", amount)
			.append("voc", voc)
			.append("confirmed", confirmed)
			.append("objected", objected)
			.toString();
	}
}
