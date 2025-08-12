package com.magnus.service.mapper;

import static com.magnus.domain.NeedAsserts.*;
import static com.magnus.domain.NeedTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NeedMapperTest {

    private NeedMapper needMapper;

    @BeforeEach
    void setUp() {
        needMapper = new NeedMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNeedSample1();
        var actual = needMapper.toEntity(needMapper.toDto(expected));
        assertNeedAllPropertiesEquals(expected, actual);
    }
}
