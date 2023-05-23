package com.fresh.voc.service;

import org.springframework.stereotype.Service;

import com.fresh.voc.model.voc.Voc;
import com.fresh.voc.repository.voc.VocRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class VocGiverService {

	private final VocRepository vocRepository;

	public Voc getVocById(Long id) {
		return vocRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("voc not exists. id=" + id));
	}
}
