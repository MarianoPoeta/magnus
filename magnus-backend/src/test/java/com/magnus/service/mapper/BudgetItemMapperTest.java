package com.magnus.service.mapper;

import static com.magnus.domain.BudgetItemAsserts.*;
import static com.magnus.domain.BudgetItemTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BudgetItemMapperTest {

    private BudgetItemMapper budgetItemMapper;

    @BeforeEach
    void setUp() {
        budgetItemMapper = new BudgetItemMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getBudgetItemSample1();
        var actual = budgetItemMapper.toEntity(budgetItemMapper.toDto(expected));
        assertBudgetItemAllPropertiesEquals(expected, actual);
    }
}
