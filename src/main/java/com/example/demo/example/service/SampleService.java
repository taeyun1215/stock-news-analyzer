package com.example.demo.example.service;

import com.example.demo.example.controller.request.SampleRequest;
import com.example.demo.example.entity.Sample;
import com.example.demo.example.repository.SampleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SampleService {

    private final SampleRepository sampleRepository;

    public List<Sample> getAllSamples() {
        return sampleRepository.findAll();
    }

    public Optional<Sample> getSampleById(Long id) {
        return sampleRepository.findById(id);
    }

    public List<Sample> getSamplesByName(String name) {
        return sampleRepository.findByNameContaining(name);
    }

    public void createSample(SampleRequest sampleRequest) {
        Sample sample = Sample.builder()
                .name(sampleRequest.name())
                .description(sampleRequest.description())
                .build();
        sampleRepository.save(sample);
    }
}
