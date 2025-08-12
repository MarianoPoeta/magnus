package com.magnus.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.magnus.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BudgetTemplateDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BudgetTemplateDTO.class);
        BudgetTemplateDTO budgetTemplateDTO1 = new BudgetTemplateDTO();
        budgetTemplateDTO1.setId(1L);
        BudgetTemplateDTO budgetTemplateDTO2 = new BudgetTemplateDTO();
        assertThat(budgetTemplateDTO1).isNotEqualTo(budgetTemplateDTO2);
        budgetTemplateDTO2.setId(budgetTemplateDTO1.getId());
        assertThat(budgetTemplateDTO1).isEqualTo(budgetTemplateDTO2);
        budgetTemplateDTO2.setId(2L);
        assertThat(budgetTemplateDTO1).isNotEqualTo(budgetTemplateDTO2);
        budgetTemplateDTO1.setId(null);
        assertThat(budgetTemplateDTO1).isNotEqualTo(budgetTemplateDTO2);
    }
}
