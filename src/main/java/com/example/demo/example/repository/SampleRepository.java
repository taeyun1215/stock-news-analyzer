package com.example.demo.example.repository;

import com.example.demo.example.entity.Sample;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SampleRepository extends JpaRepository<Sample, Long>, SampleRepositoryCustom {

    @Query(value = "SELECT * FROM sample WHERE description LIKE %:description%", nativeQuery = true)
    List<Sample> findByDescriptionContainingNative(@Param("description") String description);


}