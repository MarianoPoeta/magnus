package com.magnus.service.mapper;

import static com.magnus.domain.ProductRequirementAsserts.*;
import static com.magnus.domain.ProductRequirementTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductRequirementMapperTest {

    private ProductRequirementMapper productRequirementMapper;

    @BeforeEach
    void setUp() {
        productRequirementMapper = new ProductRequirementMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProductRequirementSample1();
        var actual = productRequirementMapper.toEntity(productRequirementMapper.toDto(expected));
        assertProductRequirementAllPropertiesEquals(expected, actual);
    }
}
