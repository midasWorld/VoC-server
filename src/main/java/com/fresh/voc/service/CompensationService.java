package com.fresh.voc.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fresh.voc.model.voc.Compensation;
import com.fresh.voc.model.voc.Voc;
import com.fresh.voc.repository.voc.CompensationRepository;
import com.fresh.voc.service.dto.request.CompensationCreateRequest;
import com.fresh.voc.service.dto.CompensationSearchDto;
import com.fresh.voc.service.dto.VocSearchDto;
import com.fresh.voc.service.dto.VocDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CompensationService {

	private final CompensationRepository compensationRepository;

	private final VocGiverService vocGiverService;

	public Long create(CompensationCreateRequest request) {
		VocDto vocDto = vocGiverService.getVocById(request.getVocId());

		if (vocDto.getCompensation() != null && vocDto.getCompensation().getId() != null) {
			throw new IllegalArgumentException("compensation already exists. vocId=" + vocDto.getId());
		}

		Voc voc = vocDto.toEntity();
		Compensation compensation = new Compensation(request.getAmount(), voc);
		compensationRepository.save(compensation);

		return compensation.getId();
	}

	public List<CompensationSearchDto> getAllCompensation() {
		List<Compensation> allCompensation = compensationRepository.findAll();

		List<Long> vocIds = allCompensation.stream()
			.map(c -> c.getVoc().getId())
			.collect(Collectors.toList());
		Map<Long, VocSearchDto> mapAllVoc = vocGiverService.getAllVocDetail(vocIds);

		return allCompensation.stream()
			.map(c -> new CompensationSearchDto(c, mapAllVoc.get(c.getVoc().getId())))
			.collect(Collectors.toList());
	}
}
