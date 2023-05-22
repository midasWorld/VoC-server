package com.fresh.voc.web.voc;

import static com.fresh.voc.web.ApiResult.ok;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fresh.voc.service.VocService;
import com.fresh.voc.service.dto.VocDto;
import com.fresh.voc.web.ApiResult;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/voc")
@RequiredArgsConstructor
@RestController
public class VocRestController {

	private final VocService vocService;

	@GetMapping
	public ApiResult<List<VocDto>> getAllVoc() {
		List<VocDto> allVoc = vocService.getAllVoc();

		return ok(allVoc);
	}
}
