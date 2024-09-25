package com.example.demo.example.repository;

import com.example.demo.example.entity.QSample;
import com.example.demo.example.entity.Sample;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class SampleRepositoryCustomImpl implements SampleRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Sample> findByNameContainingQueryDsl(String name) {
        QSample qSample = QSample.sample;
        return queryFactory
                .selectFrom(qSample)
                .where(qSample.name.containsIgnoreCase(name))
                .fetch();
    }
}