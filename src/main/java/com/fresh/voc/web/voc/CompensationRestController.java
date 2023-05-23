package com.fresh.voc.web.voc;

import static com.fresh.voc.web.ApiResult.ok;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fresh.voc.service.CompensationService;
import com.fresh.voc.service.dto.CompensationCreateRequest;
import com.fresh.voc.service.dto.CompensationSearchDto;
import com.fresh.voc.web.ApiResult;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/compensation")
@RequiredArgsConstructor
@RestController
public class CompensationRestController {

	private final CompensationService compensationService;

	@PostMapping
	public ApiResult<Long> create(@Valid @RequestBody CompensationCreateRequest request) {
		Long compensationId = compensationService.create(request);

		return ok(compensationId);
	}

	@GetMapping
	public ApiResult<List<CompensationSearchDto>> getAllCompensation() {
		List<CompensationSearchDto> allCompensation = compensationService.getAllCompensation();

		return ok(allCompensation);
	}
}
