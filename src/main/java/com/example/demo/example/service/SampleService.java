package com.example.demo.example.service;

import com.example.demo.example.controller.request.SampleRequest;
import com.example.demo.example.entity.Sample;
import com.example.demo.example.exception.ExampleException;
import com.example.demo.example.repository.SampleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SampleService {

    private final SampleRepository sampleRepository;

    @Transactional(readOnly = true)
    public List<Sample> getAllSamples() {
        return sampleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Sample getSampleById(Long id) {
        return sampleRepository.findById(id)
                .orElseThrow(() -> new ExampleException("Sample not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Sample> getSamplesByDescriptionNative(String description) {
        return sampleRepository.findByDescriptionContainingNative(description);
    }

    @Transactional(readOnly = true)
    public List<Sample> getSamplesByNameQueryDsl(String name) {
        return sampleRepository.findByNameContainingQueryDsl(name);
    }

    @Transactional
    public void createSample(SampleRequest sampleRequest) {
        Sample sample = Sample.builder()
                .name(sampleRequest.name())
                .description(sampleRequest.description())
                .build();

        sampleRepository.save(sample);
    }
}
