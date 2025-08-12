package com.magnus.service.mapper;

import static com.magnus.domain.BudgetAsserts.*;
import static com.magnus.domain.BudgetTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BudgetMapperTest {

    private BudgetMapper budgetMapper;

    @BeforeEach
    void setUp() {
        budgetMapper = new BudgetMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getBudgetSample1();
        var actual = budgetMapper.toEntity(budgetMapper.toDto(expected));
        assertBudgetAllPropertiesEquals(expected, actual);
    }
}
