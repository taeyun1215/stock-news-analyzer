package com.example.demo.example;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/samples")
class SampleController {
	private final SampleService sampleService;

	// GET all samples
	@GetMapping
	public List<SampleResponse> getAllSamples() {
		return sampleService.getAllSamples();
	}

	// GET sample by ID
	@GetMapping("/{id}")
	public ResponseEntity<SampleResponse> getSampleById(@PathVariable Long id) {
		return sampleService.getSampleById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	// GET sample by name (query parameter)
	@GetMapping("/search")
	public List<SampleResponse> getSamplesByName(@RequestParam String name) {
		return sampleService.getSamplesByName(name);
	}

	// POST create a new sample with request body
	@PostMapping
	public ResponseEntity<SampleResponse> createSample(@RequestBody SampleRequest sampleRequest) {
		return new ResponseEntity<>(sampleService.createSample(sampleRequest), HttpStatus.CREATED);
	}

	// POST update an existing sample with request parameter
	@PostMapping("/update")
	public ResponseEntity<SampleResponse> updateSampleWithParam(@RequestParam Long id, @RequestParam String name, @RequestParam String description) {
		SampleRequest sampleRequest = new SampleRequest(name, description);
		return sampleService.updateSample(id, sampleRequest)
				.map(updatedSample -> new ResponseEntity<>(updatedSample, HttpStatus.OK))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}
}
