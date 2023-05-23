package com.fresh.voc.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fresh.voc.model.common.Person;
import com.fresh.voc.model.voc.Voc;
import com.fresh.voc.repository.common.PersonRepository;
import com.fresh.voc.repository.voc.VocRepository;
import com.fresh.voc.service.dto.VocCreateRequest;
import com.fresh.voc.service.dto.VocDto;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class VocService {

	private final VocRepository vocRepository;
	private final PersonRepository personRepository;

	public List<VocDto> getAllVoc() {
		return vocRepository.findAllWithPersonAndPenaltyAndCompensation().stream()
			.map(VocDto::new)
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
}
