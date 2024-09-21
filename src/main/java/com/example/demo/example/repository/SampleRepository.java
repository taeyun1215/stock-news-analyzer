package com.example.demo.example.repository;

import com.example.demo.example.entity.Sample;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SampleRepository extends JpaRepository<Sample, Long> {
    List<Sample> findByNameContaining(String name);
}
