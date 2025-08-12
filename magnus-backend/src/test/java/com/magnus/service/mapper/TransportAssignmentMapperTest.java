package com.magnus.service.mapper;

import static com.magnus.domain.TransportAssignmentAsserts.*;
import static com.magnus.domain.TransportAssignmentTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TransportAssignmentMapperTest {

    private TransportAssignmentMapper transportAssignmentMapper;

    @BeforeEach
    void setUp() {
        transportAssignmentMapper = new TransportAssignmentMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTransportAssignmentSample1();
        var actual = transportAssignmentMapper.toEntity(transportAssignmentMapper.toDto(expected));
        assertTransportAssignmentAllPropertiesEquals(expected, actual);
    }
}
