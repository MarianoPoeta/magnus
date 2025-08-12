package com.magnus.domain;

import static com.magnus.domain.AppUserTestSamples.*;
import static com.magnus.domain.BudgetTemplateTestSamples.*;
import static com.magnus.domain.BudgetTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.magnus.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class BudgetTemplateTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BudgetTemplate.class);
        BudgetTemplate budgetTemplate1 = getBudgetTemplateSample1();
        BudgetTemplate budgetTemplate2 = new BudgetTemplate();
        assertThat(budgetTemplate1).isNotEqualTo(budgetTemplate2);

        budgetTemplate2.setId(budgetTemplate1.getId());
        assertThat(budgetTemplate1).isEqualTo(budgetTemplate2);

        budgetTemplate2 = getBudgetTemplateSample2();
        assertThat(budgetTemplate1).isNotEqualTo(budgetTemplate2);
    }

    @Test
    void budgetTest() {
        BudgetTemplate budgetTemplate = getBudgetTemplateRandomSampleGenerator();
        Budget budgetBack = getBudgetRandomSampleGenerator();

        budgetTemplate.addBudget(budgetBack);
        assertThat(budgetTemplate.getBudgets()).containsOnly(budgetBack);
        assertThat(budgetBack.getTemplate()).isEqualTo(budgetTemplate);

        budgetTemplate.removeBudget(budgetBack);
        assertThat(budgetTemplate.getBudgets()).doesNotContain(budgetBack);
        assertThat(budgetBack.getTemplate()).isNull();

        budgetTemplate.budgets(new HashSet<>(Set.of(budgetBack)));
        assertThat(budgetTemplate.getBudgets()).containsOnly(budgetBack);
        assertThat(budgetBack.getTemplate()).isEqualTo(budgetTemplate);

        budgetTemplate.setBudgets(new HashSet<>());
        assertThat(budgetTemplate.getBudgets()).doesNotContain(budgetBack);
        assertThat(budgetBack.getTemplate()).isNull();
    }

    @Test
    void createdByTest() {
        BudgetTemplate budgetTemplate = getBudgetTemplateRandomSampleGenerator();
        AppUser appUserBack = getAppUserRandomSampleGenerator();

        budgetTemplate.setCreatedBy(appUserBack);
        assertThat(budgetTemplate.getCreatedBy()).isEqualTo(appUserBack);

        budgetTemplate.createdBy(null);
        assertThat(budgetTemplate.getCreatedBy()).isNull();
    }
}
