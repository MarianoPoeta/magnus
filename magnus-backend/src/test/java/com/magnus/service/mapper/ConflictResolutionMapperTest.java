package com.magnus.service.mapper;

import static com.magnus.domain.ConflictResolutionAsserts.*;
import static com.magnus.domain.ConflictResolutionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConflictResolutionMapperTest {

    private ConflictResolutionMapper conflictResolutionMapper;

    @BeforeEach
    void setUp() {
        conflictResolutionMapper = new ConflictResolutionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getConflictResolutionSample1();
        var actual = conflictResolutionMapper.toEntity(conflictResolutionMapper.toDto(expected));
        assertConflictResolutionAllPropertiesEquals(expected, actual);
    }
}
