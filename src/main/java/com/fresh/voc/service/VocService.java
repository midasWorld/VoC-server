package com.fresh.voc.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fresh.voc.model.common.Person;
import com.fresh.voc.model.voc.Penalty;
import com.fresh.voc.model.voc.Voc;
import com.fresh.voc.repository.common.PersonRepository;
import com.fresh.voc.repository.voc.PenaltyRepository;
import com.fresh.voc.repository.voc.VocRepository;
import com.fresh.voc.service.dto.request.PenaltyCreateRequest;
import com.fresh.voc.service.dto.request.VocCreateRequest;
import com.fresh.voc.service.dto.VocSearchDetailDto;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class VocService {

	private final VocRepository vocRepository;
	private final PersonRepository personRepository;
	private final PenaltyRepository penaltyRepository;

	public List<VocSearchDetailDto> getAllVoc() {
		return vocRepository.findAllWithPersonAndPenaltyAndCompensation().stream()
			.map(VocSearchDetailDto::new)
			.collect(Collectors.toList());
	}

	@Transactional
	public Long create(VocCreateRequest request) {
		Person person = personRepository.findById(request.getDueTargetId())
			.orElseThrow(() -> new IllegalArgumentException("person not exists. id=" + request.getDueTargetId()));
		Voc voc = new Voc(request.getDueType(), person, request.getDueReason());
		vocRepository.save(voc);

		return voc.getId();
	}

	@Transactional
	public Long createPenalty(Long vocId, PenaltyCreateRequest request) {
		Voc voc = vocRepository.findWithPenaltyAndCompensationById(vocId)
			.orElseThrow(() -> new IllegalArgumentException("voc not exists. id=" + vocId));

		if (voc.getPenalty() != null && voc.getPenalty().getId() != null) {
			throw new IllegalArgumentException("penalty already exists. vocId=" + voc.getId());
		}

		Penalty penalty = new Penalty(request.getContent(), request.getAmount(), voc);
		penaltyRepository.save(penalty);

		return penalty.getId();
	}

	@Transactional
	public void confirmPenalty(Long vocId, Long penaltyId) {
		Penalty penalty = penaltyRepository.findByIdAndVocId(penaltyId, vocId)
			.orElseThrow(() -> new IllegalArgumentException("penalty not exists. id=" + penaltyId + ", vocId=" + vocId));

		penalty.confirm();
	}
}
