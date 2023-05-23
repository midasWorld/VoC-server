package com.fresh.voc.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fresh.voc.model.voc.Voc;
import com.fresh.voc.repository.voc.VocRepository;
import com.fresh.voc.service.dto.VocDetailDto;
import com.fresh.voc.service.dto.VocDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class VocGiverService {

	private final VocRepository vocRepository;

	public VocDto getVocById(Long id) {
		Voc voc = vocRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("voc not exists. id=" + id));

		return new VocDto(voc);
	}

	public Map<Long, VocDetailDto> getAllVocDetail(List<Long> ids) {
		return vocRepository.findAllWithPersonByIdIn(ids).stream()
			.map(VocDetailDto::new)
			.collect(Collectors.toMap(VocDetailDto::getId, Function.identity()));
	}
}
