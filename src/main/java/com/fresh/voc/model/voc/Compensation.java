package com.fresh.voc.model.voc;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.DecimalMin;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Compensation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@DecimalMin("0")
	private Long amount;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "voc_id")
	private Voc voc;

	public Compensation(Long amount, Voc voc) {
		this.amount = amount;
		this.voc = voc;
	}

	@Override
	public String toString() {
		return new org.apache.commons.lang3.builder.ToStringBuilder(this)
			.append("id", id)
			.append("amount", amount)
			.append("voc", voc)
			.toString();
	}
}
