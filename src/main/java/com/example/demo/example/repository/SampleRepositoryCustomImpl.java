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
        QSample qSample =

        return queryFactory
                .selectFrom(sample)
                .where(sample.name.containsIgnoreCase(name))
                .fetch();
    }
}
