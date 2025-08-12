package com.magnus.domain;

import static com.magnus.domain.BudgetItemTestSamples.*;
import static com.magnus.domain.BudgetTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.magnus.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BudgetItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BudgetItem.class);
        BudgetItem budgetItem1 = getBudgetItemSample1();
        BudgetItem budgetItem2 = new BudgetItem();
        assertThat(budgetItem1).isNotEqualTo(budgetItem2);

        budgetItem2.setId(budgetItem1.getId());
        assertThat(budgetItem1).isEqualTo(budgetItem2);

        budgetItem2 = getBudgetItemSample2();
        assertThat(budgetItem1).isNotEqualTo(budgetItem2);
    }

    @Test
    void budgetTest() {
        BudgetItem budgetItem = getBudgetItemRandomSampleGenerator();
        Budget budgetBack = getBudgetRandomSampleGenerator();

        budgetItem.setBudget(budgetBack);
        assertThat(budgetItem.getBudget()).isEqualTo(budgetBack);

        budgetItem.budget(null);
        assertThat(budgetItem.getBudget()).isNull();
    }
}
