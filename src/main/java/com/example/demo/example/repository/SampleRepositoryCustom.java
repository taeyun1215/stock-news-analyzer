package com.example.demo.example.repository;

import com.example.demo.example.entity.Sample;

import java.util.List;

public interface SampleRepositoryCustom {
    List<Sample> findByNameContainingQueryDsl(String name);
}