package com.magnus.service.mapper;

import static com.magnus.domain.AccommodationAsserts.*;
import static com.magnus.domain.AccommodationTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AccommodationMapperTest {

    private AccommodationMapper accommodationMapper;

    @BeforeEach
    void setUp() {
        accommodationMapper = new AccommodationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAccommodationSample1();
        var actual = accommodationMapper.toEntity(accommodationMapper.toDto(expected));
        assertAccommodationAllPropertiesEquals(expected, actual);
    }
}
