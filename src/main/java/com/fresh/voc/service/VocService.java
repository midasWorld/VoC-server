package com.fresh.voc.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fresh.voc.repository.voc.VocRepository;
import com.fresh.voc.service.dto.VocDto;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class VocService {

	private final VocRepository vocRepository;

	public List<VocDto> getAllVoc() {
		return vocRepository.findAllWithPersonAndPenaltyAndCompensation().stream()
			.map(VocDto::new)
			.collect(Collectors.toList());
	}
}
