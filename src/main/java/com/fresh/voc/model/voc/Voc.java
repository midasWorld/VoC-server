package com.fresh.voc.model.voc;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fresh.voc.model.BaseEntity;
import com.fresh.voc.model.common.Person;

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

	@OneToOne(mappedBy = "voc", fetch = FetchType.LAZY)
	private Penalty penalty;

	@OneToOne(mappedBy = "voc", fetch = FetchType.LAZY)
	private Compensation compensation;

	public Voc(DueType dueType, Person dueTarget, String dueReason) {
		this(dueType, dueTarget, dueReason, null, null);
	}

	public Voc(DueType dueType, Person dueTarget, String dueReason, Penalty penalty, Compensation compensation) {
		this.dueType = dueType;
		this.dueTarget = dueTarget;
		this.dueReason = dueReason;
		this.penalty = penalty;
		this.compensation = compensation;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Voc voc = (Voc)o;
		return Objects.equals(id, voc.id)
			&& dueType == voc.dueType
			&& Objects.equals(dueReason, voc.dueReason)
			&& Objects.equals(dueTarget.getId(), voc.dueTarget.getId());
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(id)
			.append(dueType)
			.append(dueTarget)
			.append(dueReason)
			.append(penalty)
			.append(compensation)
			.toHashCode();
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
