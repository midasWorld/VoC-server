package com.fresh.voc.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fresh.voc.model.voc.Compensation;
import com.fresh.voc.model.voc.Voc;
import com.fresh.voc.repository.voc.CompensationRepository;
import com.fresh.voc.service.dto.CompensationCreateRequest;
import com.fresh.voc.service.dto.CompensationSearchDto;
import com.fresh.voc.service.dto.VocDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CompensationService {

	private final CompensationRepository compensationRepository;

	private final VocGiverService vocGiverService;

	public Long create(CompensationCreateRequest request) {
		VocDto vocDto = vocGiverService.getVocById(request.getVocId());
		Voc voc = vocDto.toEntity();

		Compensation compensation = new Compensation(request.getAmount(), voc);
		compensationRepository.save(compensation);

		return compensation.getId();
	}
}
