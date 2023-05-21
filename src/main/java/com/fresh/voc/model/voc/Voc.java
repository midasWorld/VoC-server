package com.fresh.voc.model.voc;

import com.fresh.voc.model.BaseEntity;
import com.fresh.voc.model.common.Person;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
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
