package com.magnus.domain;

import static com.magnus.domain.AppUserTestSamples.*;
import static com.magnus.domain.BudgetTestSamples.*;
import static com.magnus.domain.CookingScheduleTestSamples.*;
import static com.magnus.domain.NeedTestSamples.*;
import static com.magnus.domain.ProductRequirementTestSamples.*;
import static com.magnus.domain.TaskDependencyTestSamples.*;
import static com.magnus.domain.TaskTestSamples.*;
import static com.magnus.domain.WeeklyPlanTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.magnus.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TaskTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Task.class);
        Task task1 = getTaskSample1();
        Task task2 = new Task();
        assertThat(task1).isNotEqualTo(task2);

        task2.setId(task1.getId());
        assertThat(task1).isEqualTo(task2);

        task2 = getTaskSample2();
        assertThat(task1).isNotEqualTo(task2);
    }

    @Test
    void needTest() {
        Task task = getTaskRandomSampleGenerator();
        Need needBack = getNeedRandomSampleGenerator();

        task.addNeed(needBack);
        assertThat(task.getNeeds()).containsOnly(needBack);
        assertThat(needBack.getParentTask()).isEqualTo(task);

        task.removeNeed(needBack);
        assertThat(task.getNeeds()).doesNotContain(needBack);
        assertThat(needBack.getParentTask()).isNull();

        task.needs(new HashSet<>(Set.of(needBack)));
        assertThat(task.getNeeds()).containsOnly(needBack);
        assertThat(needBack.getParentTask()).isEqualTo(task);

        task.setNeeds(new HashSet<>());
        assertThat(task.getNeeds()).doesNotContain(needBack);
        assertThat(needBack.getParentTask()).isNull();
    }

    @Test
    void productRequirementTest() {
        Task task = getTaskRandomSampleGenerator();
        ProductRequirement productRequirementBack = getProductRequirementRandomSampleGenerator();

        task.addProductRequirement(productRequirementBack);
        assertThat(task.getProductRequirements()).containsOnly(productRequirementBack);
        assertThat(productRequirementBack.getRelatedTask()).isEqualTo(task);

        task.removeProductRequirement(productRequirementBack);
        assertThat(task.getProductRequirements()).doesNotContain(productRequirementBack);
        assertThat(productRequirementBack.getRelatedTask()).isNull();

        task.productRequirements(new HashSet<>(Set.of(productRequirementBack)));
        assertThat(task.getProductRequirements()).containsOnly(productRequirementBack);
        assertThat(productRequirementBack.getRelatedTask()).isEqualTo(task);

        task.setProductRequirements(new HashSet<>());
        assertThat(task.getProductRequirements()).doesNotContain(productRequirementBack);
        assertThat(productRequirementBack.getRelatedTask()).isNull();
    }

    @Test
    void taskDependenciesTest() {
        Task task = getTaskRandomSampleGenerator();
        TaskDependency taskDependencyBack = getTaskDependencyRandomSampleGenerator();

        task.addTaskDependencies(taskDependencyBack);
        assertThat(task.getTaskDependencies()).containsOnly(taskDependencyBack);
        assertThat(taskDependencyBack.getDependentTask()).isEqualTo(task);

        task.removeTaskDependencies(taskDependencyBack);
        assertThat(task.getTaskDependencies()).doesNotContain(taskDependencyBack);
        assertThat(taskDependencyBack.getDependentTask()).isNull();

        task.taskDependencies(new HashSet<>(Set.of(taskDependencyBack)));
        assertThat(task.getTaskDependencies()).containsOnly(taskDependencyBack);
        assertThat(taskDependencyBack.getDependentTask()).isEqualTo(task);

        task.setTaskDependencies(new HashSet<>());
        assertThat(task.getTaskDependencies()).doesNotContain(taskDependencyBack);
        assertThat(taskDependencyBack.getDependentTask()).isNull();
    }

    @Test
    void createdByTest() {
        Task task = getTaskRandomSampleGenerator();
        AppUser appUserBack = getAppUserRandomSampleGenerator();

        task.setCreatedBy(appUserBack);
        assertThat(task.getCreatedBy()).isEqualTo(appUserBack);

        task.createdBy(null);
        assertThat(task.getCreatedBy()).isNull();
    }

    @Test
    void assignedToTest() {
        Task task = getTaskRandomSampleGenerator();
        AppUser appUserBack = getAppUserRandomSampleGenerator();

        task.setAssignedTo(appUserBack);
        assertThat(task.getAssignedTo()).isEqualTo(appUserBack);

        task.assignedTo(null);
        assertThat(task.getAssignedTo()).isNull();
    }

    @Test
    void weeklyPlanTest() {
        Task task = getTaskRandomSampleGenerator();
        WeeklyPlan weeklyPlanBack = getWeeklyPlanRandomSampleGenerator();

        task.setWeeklyPlan(weeklyPlanBack);
        assertThat(task.getWeeklyPlan()).isEqualTo(weeklyPlanBack);

        task.weeklyPlan(null);
        assertThat(task.getWeeklyPlan()).isNull();
    }

    @Test
    void relatedBudgetTest() {
        Task task = getTaskRandomSampleGenerator();
        Budget budgetBack = getBudgetRandomSampleGenerator();

        task.setRelatedBudget(budgetBack);
        assertThat(task.getRelatedBudget()).isEqualTo(budgetBack);

        task.relatedBudget(null);
        assertThat(task.getRelatedBudget()).isNull();
    }

    @Test
    void cookingScheduleTest() {
        Task task = getTaskRandomSampleGenerator();
        CookingSchedule cookingScheduleBack = getCookingScheduleRandomSampleGenerator();

        task.setCookingSchedule(cookingScheduleBack);
        assertThat(task.getCookingSchedule()).isEqualTo(cookingScheduleBack);
        assertThat(cookingScheduleBack.getRelatedTask()).isEqualTo(task);

        task.cookingSchedule(null);
        assertThat(task.getCookingSchedule()).isNull();
        assertThat(cookingScheduleBack.getRelatedTask()).isNull();
    }
}
