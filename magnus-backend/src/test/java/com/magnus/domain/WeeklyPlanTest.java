package com.magnus.domain;

import static com.magnus.domain.AppUserTestSamples.*;
import static com.magnus.domain.BudgetTestSamples.*;
import static com.magnus.domain.ShoppingItemTestSamples.*;
import static com.magnus.domain.TaskTestSamples.*;
import static com.magnus.domain.WeeklyPlanTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.magnus.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class WeeklyPlanTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WeeklyPlan.class);
        WeeklyPlan weeklyPlan1 = getWeeklyPlanSample1();
        WeeklyPlan weeklyPlan2 = new WeeklyPlan();
        assertThat(weeklyPlan1).isNotEqualTo(weeklyPlan2);

        weeklyPlan2.setId(weeklyPlan1.getId());
        assertThat(weeklyPlan1).isEqualTo(weeklyPlan2);

        weeklyPlan2 = getWeeklyPlanSample2();
        assertThat(weeklyPlan1).isNotEqualTo(weeklyPlan2);
    }

    @Test
    void budgetTest() {
        WeeklyPlan weeklyPlan = getWeeklyPlanRandomSampleGenerator();
        Budget budgetBack = getBudgetRandomSampleGenerator();

        weeklyPlan.addBudget(budgetBack);
        assertThat(weeklyPlan.getBudgets()).containsOnly(budgetBack);
        assertThat(budgetBack.getWeeklyPlan()).isEqualTo(weeklyPlan);

        weeklyPlan.removeBudget(budgetBack);
        assertThat(weeklyPlan.getBudgets()).doesNotContain(budgetBack);
        assertThat(budgetBack.getWeeklyPlan()).isNull();

        weeklyPlan.budgets(new HashSet<>(Set.of(budgetBack)));
        assertThat(weeklyPlan.getBudgets()).containsOnly(budgetBack);
        assertThat(budgetBack.getWeeklyPlan()).isEqualTo(weeklyPlan);

        weeklyPlan.setBudgets(new HashSet<>());
        assertThat(weeklyPlan.getBudgets()).doesNotContain(budgetBack);
        assertThat(budgetBack.getWeeklyPlan()).isNull();
    }

    @Test
    void shoppingItemTest() {
        WeeklyPlan weeklyPlan = getWeeklyPlanRandomSampleGenerator();
        ShoppingItem shoppingItemBack = getShoppingItemRandomSampleGenerator();

        weeklyPlan.addShoppingItem(shoppingItemBack);
        assertThat(weeklyPlan.getShoppingItems()).containsOnly(shoppingItemBack);
        assertThat(shoppingItemBack.getWeeklyPlan()).isEqualTo(weeklyPlan);

        weeklyPlan.removeShoppingItem(shoppingItemBack);
        assertThat(weeklyPlan.getShoppingItems()).doesNotContain(shoppingItemBack);
        assertThat(shoppingItemBack.getWeeklyPlan()).isNull();

        weeklyPlan.shoppingItems(new HashSet<>(Set.of(shoppingItemBack)));
        assertThat(weeklyPlan.getShoppingItems()).containsOnly(shoppingItemBack);
        assertThat(shoppingItemBack.getWeeklyPlan()).isEqualTo(weeklyPlan);

        weeklyPlan.setShoppingItems(new HashSet<>());
        assertThat(weeklyPlan.getShoppingItems()).doesNotContain(shoppingItemBack);
        assertThat(shoppingItemBack.getWeeklyPlan()).isNull();
    }

    @Test
    void taskTest() {
        WeeklyPlan weeklyPlan = getWeeklyPlanRandomSampleGenerator();
        Task taskBack = getTaskRandomSampleGenerator();

        weeklyPlan.addTask(taskBack);
        assertThat(weeklyPlan.getTasks()).containsOnly(taskBack);
        assertThat(taskBack.getWeeklyPlan()).isEqualTo(weeklyPlan);

        weeklyPlan.removeTask(taskBack);
        assertThat(weeklyPlan.getTasks()).doesNotContain(taskBack);
        assertThat(taskBack.getWeeklyPlan()).isNull();

        weeklyPlan.tasks(new HashSet<>(Set.of(taskBack)));
        assertThat(weeklyPlan.getTasks()).containsOnly(taskBack);
        assertThat(taskBack.getWeeklyPlan()).isEqualTo(weeklyPlan);

        weeklyPlan.setTasks(new HashSet<>());
        assertThat(weeklyPlan.getTasks()).doesNotContain(taskBack);
        assertThat(taskBack.getWeeklyPlan()).isNull();
    }

    @Test
    void createdByTest() {
        WeeklyPlan weeklyPlan = getWeeklyPlanRandomSampleGenerator();
        AppUser appUserBack = getAppUserRandomSampleGenerator();

        weeklyPlan.setCreatedBy(appUserBack);
        assertThat(weeklyPlan.getCreatedBy()).isEqualTo(appUserBack);

        weeklyPlan.createdBy(null);
        assertThat(weeklyPlan.getCreatedBy()).isNull();
    }
}
