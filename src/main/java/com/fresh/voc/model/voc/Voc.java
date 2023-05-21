package com.fresh.voc.model.voc;

import com.fresh.voc.model.BaseEntity;
import com.fresh.voc.model.common.Person;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Voc extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(value = EnumType.STRING)
	private DueType dueType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "due_target_id")
	private Person dueTarget;

	@NotBlank
	private String dueReason;

	public Voc(DueType dueType, Person dueTarget, String dueReason) {
		this.dueType = dueType;
		this.dueTarget = dueTarget;
		this.dueReason = dueReason;
	}

	@Override
	public String toString() {
		return new org.apache.commons.lang3.builder.ToStringBuilder(this)
			.append("id", id)
			.append("dueType", dueType)
			.append("dueTarget", dueTarget)
			.append("dueReason", dueReason)
			.toString();
	}
}
