package com.fresh.voc.web.voc;

import static com.fresh.voc.web.ApiResult.ok;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fresh.voc.service.VocService;
import com.fresh.voc.service.dto.request.PenaltyCreateRequest;
import com.fresh.voc.service.dto.request.VocCreateRequest;
import com.fresh.voc.service.dto.VocSearchDto;
import com.fresh.voc.web.ApiResult;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/voc")
@RequiredArgsConstructor
@RestController
public class VocRestController {

	private final VocService vocService;

	@GetMapping
	public ApiResult<List<VocSearchDto>> getAllVoc() {
		List<VocSearchDto> allVoc = vocService.getAllVoc();

		return ok(allVoc);
	}

	@PostMapping
	public ApiResult<Long> create(@Valid @RequestBody VocCreateRequest request) {
		Long vocId = vocService.create(request);

		return ok(vocId);
	}

	@PostMapping("/{id}/penalties")
	public ApiResult<Long> createPenalty(@PathVariable Long id, @Valid @RequestBody PenaltyCreateRequest request) {
		Long penaltyId = vocService.createPenalty(id, request);

		return ok(penaltyId);
	}

	@PatchMapping("/{id}/penalties/{penaltyId}/confirm")
	public void confirmPenalty(@PathVariable Long id, @PathVariable Long penaltyId) {
		vocService.confirmPenalty(id, penaltyId);
	}
}
