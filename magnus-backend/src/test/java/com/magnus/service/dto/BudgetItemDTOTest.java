package com.magnus.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.magnus.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BudgetItemDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BudgetItemDTO.class);
        BudgetItemDTO budgetItemDTO1 = new BudgetItemDTO();
        budgetItemDTO1.setId(1L);
        BudgetItemDTO budgetItemDTO2 = new BudgetItemDTO();
        assertThat(budgetItemDTO1).isNotEqualTo(budgetItemDTO2);
        budgetItemDTO2.setId(budgetItemDTO1.getId());
        assertThat(budgetItemDTO1).isEqualTo(budgetItemDTO2);
        budgetItemDTO2.setId(2L);
        assertThat(budgetItemDTO1).isNotEqualTo(budgetItemDTO2);
        budgetItemDTO1.setId(null);
        assertThat(budgetItemDTO1).isNotEqualTo(budgetItemDTO2);
    }
}
