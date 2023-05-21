package com.fresh.voc.model.voc;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.DecimalMin;

import com.fresh.voc.model.BaseEntity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Compensation extends BaseEntity {

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
