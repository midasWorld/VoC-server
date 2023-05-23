package com.fresh.voc.service;

import org.springframework.stereotype.Service;

import com.fresh.voc.model.voc.Compensation;
import com.fresh.voc.model.voc.Voc;
import com.fresh.voc.repository.voc.CompensationRepository;
import com.fresh.voc.service.dto.CompensationCreateRequest;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CompensationService {

	private final CompensationRepository compensationRepository;

	private final VocGiverService vocGiverService;

	public Long create(CompensationCreateRequest request) {
		Voc voc = vocGiverService.getVocById(request.getVocId());
		Compensation compensation = new Compensation(request.getAmount(), voc);
		compensationRepository.save(compensation);

		return compensation.getId();
	}
}
