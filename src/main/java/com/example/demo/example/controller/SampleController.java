package com.example.demo.example.controller;

import com.example.demo.example.controller.request.SampleRequest;
import com.example.demo.example.controller.response.SampleResponse;
import com.example.demo.example.entity.Sample;
import com.example.demo.example.service.SampleService;
import com.example.demo.global.util.SuccessApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/samples")
class SampleController {
	private final SampleService sampleService;

	@GetMapping
	public SuccessApiResponse<List<SampleResponse>> getAllSamples() {
		List<Sample> samples = sampleService.getAllSamples();
		List<SampleResponse> sampleResponses = samples.stream()
				.map(sample -> new SampleResponse(sample.getId(), sample.getName(), sample.getDescription()))
				.toList();

		return SuccessApiResponse.of(HttpStatus.OK.value(), sampleResponses);
	}

	@GetMapping("/{id}")
	public SuccessApiResponse<SampleResponse> getSampleById(@PathVariable Long id) {
		Sample sample = sampleService.getSampleById(id);
		SampleResponse sampleResponse = new SampleResponse(sample.getId(), sample.getName(), sample.getDescription());
		return SuccessApiResponse.of(HttpStatus.OK.value(), sampleResponse);
	}

	// Native Query
	@GetMapping("/searchByDescription")
	public SuccessApiResponse<List<SampleResponse>> getSamplesByDescription(@RequestParam String description) {
		List<Sample> samples = sampleService.getSamplesByDescriptionNative(description);
		List<SampleResponse> sampleResponses = samples.stream()
				.map(sample -> new SampleResponse(sample.getId(), sample.getName(), sample.getDescription()))
				.toList();
		return SuccessApiResponse.of(HttpStatus.OK.value(), sampleResponses);
	}

	// QueryDSL
	@GetMapping("/searchByName")
	public SuccessApiResponse<List<SampleResponse>> getSamplesByName(@RequestParam String name) {
		List<Sample> samples = sampleService.getSamplesByNameQueryDsl(name);
		List<SampleResponse> sampleResponses = samples.stream()
				.map(sample -> new SampleResponse(sample.getId(), sample.getName(), sample.getDescription()))
				.toList();
		return SuccessApiResponse.of(HttpStatus.OK.value(), sampleResponses);
	}

	@PostMapping
	public SuccessApiResponse<String> createSample(@RequestBody SampleRequest sampleRequest) {
		sampleService.createSample(sampleRequest);
		return SuccessApiResponse.of(HttpStatus.CREATED.value(), "Sample created successfully");
	}
}