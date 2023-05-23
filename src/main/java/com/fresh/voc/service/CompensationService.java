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
import com.fresh.voc.service.dto.VocDetailDto;
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

		boolean alreadyExists = compensationRepository.existsByVoc(voc);
		if (alreadyExists) {
			throw new IllegalArgumentException("compensation already exists. vocId=" + voc.getId());
		}

		Compensation compensation = new Compensation(request.getAmount(), voc);
		compensationRepository.save(compensation);

		return compensation.getId();
	}

	public List<CompensationSearchDto> getAllCompensation() {
		List<Compensation> allCompensation = compensationRepository.findAll();

		List<Long> vocIds = allCompensation.stream()
			.map(c -> c.getVoc().getId())
			.collect(Collectors.toList());
		Map<Long, VocDetailDto> mapAllVoc = vocGiverService.getAllVocDetail(vocIds);

		return allCompensation.stream()
			.map(c -> new CompensationSearchDto(c, mapAllVoc.get(c.getVoc().getId())))
			.collect(Collectors.toList());
	}
}
