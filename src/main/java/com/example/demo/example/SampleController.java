package com.example.demo.example;

import com.example.demo.global.util.ApiResponse;
import com.example.demo.global.util.ErrorApiResponse;
import com.example.demo.global.util.SuccessApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/samples")
class SampleController {
	private final SampleService sampleService;

	@GetMapping
	public ResponseEntity<SuccessApiResponse<List<SampleResponse>>> getAllSamples() {
		List<Sample> samples = sampleService.getAllSamples();
		List<SampleResponse> sampleResponses = samples.stream()
				.map(sample -> new SampleResponse(sample.getId(), sample.getName(), sample.getDescription()))
				.toList();
		return ResponseEntity.ok(SuccessApiResponse.of(HttpStatus.OK.value(), sampleResponses));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse> getSampleById(@PathVariable Long id) {
		return sampleService.getSampleById(id)
				.map(sample -> ResponseEntity.ok(SuccessApiResponse.of(HttpStatus.OK.value(), new SampleResponse(sample.getId(), sample.getName(), sample.getDescription()));
	}

	@GetMapping("/search")
	public ResponseEntity<SuccessApiResponse<List<SampleResponse>>> getSamplesByName(@RequestParam String name) {
		List<Sample> samples = sampleService.getSamplesByName(name);
		List<SampleResponse> sampleResponses = samples.stream()
				.map(sample -> new SampleResponse(sample.getId(), sample.getName(), sample.getDescription()))
				.toList();
		return ResponseEntity.ok(SuccessApiResponse.of(HttpStatus.OK.value(), sampleResponses));
	}

	@PostMapping
	public ResponseEntity<ApiResponse> createSample(@RequestBody SampleRequest sampleRequest) {
		sampleService.createSample(sampleRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(SuccessApiResponse.of(HttpStatus.CREATED.value(), "Sample created successfully"));
	}
}