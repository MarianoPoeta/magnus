package com.magnus.domain;

import static com.magnus.domain.BudgetTestSamples.*;
import static com.magnus.domain.CookingIngredientTestSamples.*;
import static com.magnus.domain.CookingScheduleTestSamples.*;
import static com.magnus.domain.TaskTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.magnus.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CookingScheduleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CookingSchedule.class);
        CookingSchedule cookingSchedule1 = getCookingScheduleSample1();
        CookingSchedule cookingSchedule2 = new CookingSchedule();
        assertThat(cookingSchedule1).isNotEqualTo(cookingSchedule2);

        cookingSchedule2.setId(cookingSchedule1.getId());
        assertThat(cookingSchedule1).isEqualTo(cookingSchedule2);

        cookingSchedule2 = getCookingScheduleSample2();
        assertThat(cookingSchedule1).isNotEqualTo(cookingSchedule2);
    }

    @Test
    void relatedTaskTest() {
        CookingSchedule cookingSchedule = getCookingScheduleRandomSampleGenerator();
        Task taskBack = getTaskRandomSampleGenerator();

        cookingSchedule.setRelatedTask(taskBack);
        assertThat(cookingSchedule.getRelatedTask()).isEqualTo(taskBack);

        cookingSchedule.relatedTask(null);
        assertThat(cookingSchedule.getRelatedTask()).isNull();
    }

    @Test
    void cookingIngredientTest() {
        CookingSchedule cookingSchedule = getCookingScheduleRandomSampleGenerator();
        CookingIngredient cookingIngredientBack = getCookingIngredientRandomSampleGenerator();

        cookingSchedule.addCookingIngredient(cookingIngredientBack);
        assertThat(cookingSchedule.getCookingIngredients()).containsOnly(cookingIngredientBack);
        assertThat(cookingIngredientBack.getCookingSchedule()).isEqualTo(cookingSchedule);

        cookingSchedule.removeCookingIngredient(cookingIngredientBack);
        assertThat(cookingSchedule.getCookingIngredients()).doesNotContain(cookingIngredientBack);
        assertThat(cookingIngredientBack.getCookingSchedule()).isNull();

        cookingSchedule.cookingIngredients(new HashSet<>(Set.of(cookingIngredientBack)));
        assertThat(cookingSchedule.getCookingIngredients()).containsOnly(cookingIngredientBack);
        assertThat(cookingIngredientBack.getCookingSchedule()).isEqualTo(cookingSchedule);

        cookingSchedule.setCookingIngredients(new HashSet<>());
        assertThat(cookingSchedule.getCookingIngredients()).doesNotContain(cookingIngredientBack);
        assertThat(cookingIngredientBack.getCookingSchedule()).isNull();
    }

    @Test
    void budgetTest() {
        CookingSchedule cookingSchedule = getCookingScheduleRandomSampleGenerator();
        Budget budgetBack = getBudgetRandomSampleGenerator();

        cookingSchedule.setBudget(budgetBack);
        assertThat(cookingSchedule.getBudget()).isEqualTo(budgetBack);

        cookingSchedule.budget(null);
        assertThat(cookingSchedule.getBudget()).isNull();
    }
}
