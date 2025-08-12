package com.magnus.domain;

import static com.magnus.domain.AppUserTestSamples.*;
import static com.magnus.domain.BudgetItemTestSamples.*;
import static com.magnus.domain.BudgetTemplateTestSamples.*;
import static com.magnus.domain.BudgetTestSamples.*;
import static com.magnus.domain.ClientTestSamples.*;
import static com.magnus.domain.CookingScheduleTestSamples.*;
import static com.magnus.domain.PaymentTestSamples.*;
import static com.magnus.domain.TaskTestSamples.*;
import static com.magnus.domain.TransportAssignmentTestSamples.*;
import static com.magnus.domain.WeeklyPlanTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.magnus.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class BudgetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Budget.class);
        Budget budget1 = getBudgetSample1();
        Budget budget2 = new Budget();
        assertThat(budget1).isNotEqualTo(budget2);

        budget2.setId(budget1.getId());
        assertThat(budget1).isEqualTo(budget2);

        budget2 = getBudgetSample2();
        assertThat(budget1).isNotEqualTo(budget2);
    }

    @Test
    void budgetItemTest() {
        Budget budget = getBudgetRandomSampleGenerator();
        BudgetItem budgetItemBack = getBudgetItemRandomSampleGenerator();

        budget.addBudgetItem(budgetItemBack);
        assertThat(budget.getBudgetItems()).containsOnly(budgetItemBack);
        assertThat(budgetItemBack.getBudget()).isEqualTo(budget);

        budget.removeBudgetItem(budgetItemBack);
        assertThat(budget.getBudgetItems()).doesNotContain(budgetItemBack);
        assertThat(budgetItemBack.getBudget()).isNull();

        budget.budgetItems(new HashSet<>(Set.of(budgetItemBack)));
        assertThat(budget.getBudgetItems()).containsOnly(budgetItemBack);
        assertThat(budgetItemBack.getBudget()).isEqualTo(budget);

        budget.setBudgetItems(new HashSet<>());
        assertThat(budget.getBudgetItems()).doesNotContain(budgetItemBack);
        assertThat(budgetItemBack.getBudget()).isNull();
    }

    @Test
    void paymentTest() {
        Budget budget = getBudgetRandomSampleGenerator();
        Payment paymentBack = getPaymentRandomSampleGenerator();

        budget.addPayment(paymentBack);
        assertThat(budget.getPayments()).containsOnly(paymentBack);
        assertThat(paymentBack.getBudget()).isEqualTo(budget);

        budget.removePayment(paymentBack);
        assertThat(budget.getPayments()).doesNotContain(paymentBack);
        assertThat(paymentBack.getBudget()).isNull();

        budget.payments(new HashSet<>(Set.of(paymentBack)));
        assertThat(budget.getPayments()).containsOnly(paymentBack);
        assertThat(paymentBack.getBudget()).isEqualTo(budget);

        budget.setPayments(new HashSet<>());
        assertThat(budget.getPayments()).doesNotContain(paymentBack);
        assertThat(paymentBack.getBudget()).isNull();
    }

    @Test
    void taskTest() {
        Budget budget = getBudgetRandomSampleGenerator();
        Task taskBack = getTaskRandomSampleGenerator();

        budget.addTask(taskBack);
        assertThat(budget.getTasks()).containsOnly(taskBack);
        assertThat(taskBack.getRelatedBudget()).isEqualTo(budget);

        budget.removeTask(taskBack);
        assertThat(budget.getTasks()).doesNotContain(taskBack);
        assertThat(taskBack.getRelatedBudget()).isNull();

        budget.tasks(new HashSet<>(Set.of(taskBack)));
        assertThat(budget.getTasks()).containsOnly(taskBack);
        assertThat(taskBack.getRelatedBudget()).isEqualTo(budget);

        budget.setTasks(new HashSet<>());
        assertThat(budget.getTasks()).doesNotContain(taskBack);
        assertThat(taskBack.getRelatedBudget()).isNull();
    }

    @Test
    void transportAssignmentTest() {
        Budget budget = getBudgetRandomSampleGenerator();
        TransportAssignment transportAssignmentBack = getTransportAssignmentRandomSampleGenerator();

        budget.addTransportAssignment(transportAssignmentBack);
        assertThat(budget.getTransportAssignments()).containsOnly(transportAssignmentBack);
        assertThat(transportAssignmentBack.getBudget()).isEqualTo(budget);

        budget.removeTransportAssignment(transportAssignmentBack);
        assertThat(budget.getTransportAssignments()).doesNotContain(transportAssignmentBack);
        assertThat(transportAssignmentBack.getBudget()).isNull();

        budget.transportAssignments(new HashSet<>(Set.of(transportAssignmentBack)));
        assertThat(budget.getTransportAssignments()).containsOnly(transportAssignmentBack);
        assertThat(transportAssignmentBack.getBudget()).isEqualTo(budget);

        budget.setTransportAssignments(new HashSet<>());
        assertThat(budget.getTransportAssignments()).doesNotContain(transportAssignmentBack);
        assertThat(transportAssignmentBack.getBudget()).isNull();
    }

    @Test
    void cookingScheduleTest() {
        Budget budget = getBudgetRandomSampleGenerator();
        CookingSchedule cookingScheduleBack = getCookingScheduleRandomSampleGenerator();

        budget.addCookingSchedule(cookingScheduleBack);
        assertThat(budget.getCookingSchedules()).containsOnly(cookingScheduleBack);
        assertThat(cookingScheduleBack.getBudget()).isEqualTo(budget);

        budget.removeCookingSchedule(cookingScheduleBack);
        assertThat(budget.getCookingSchedules()).doesNotContain(cookingScheduleBack);
        assertThat(cookingScheduleBack.getBudget()).isNull();

        budget.cookingSchedules(new HashSet<>(Set.of(cookingScheduleBack)));
        assertThat(budget.getCookingSchedules()).containsOnly(cookingScheduleBack);
        assertThat(cookingScheduleBack.getBudget()).isEqualTo(budget);

        budget.setCookingSchedules(new HashSet<>());
        assertThat(budget.getCookingSchedules()).doesNotContain(cookingScheduleBack);
        assertThat(cookingScheduleBack.getBudget()).isNull();
    }

    @Test
    void createdByTest() {
        Budget budget = getBudgetRandomSampleGenerator();
        AppUser appUserBack = getAppUserRandomSampleGenerator();

        budget.setCreatedBy(appUserBack);
        assertThat(budget.getCreatedBy()).isEqualTo(appUserBack);

        budget.createdBy(null);
        assertThat(budget.getCreatedBy()).isNull();
    }

    @Test
    void assignedToTest() {
        Budget budget = getBudgetRandomSampleGenerator();
        AppUser appUserBack = getAppUserRandomSampleGenerator();

        budget.setAssignedTo(appUserBack);
        assertThat(budget.getAssignedTo()).isEqualTo(appUserBack);

        budget.assignedTo(null);
        assertThat(budget.getAssignedTo()).isNull();
    }

    @Test
    void clientTest() {
        Budget budget = getBudgetRandomSampleGenerator();
        Client clientBack = getClientRandomSampleGenerator();

        budget.setClient(clientBack);
        assertThat(budget.getClient()).isEqualTo(clientBack);

        budget.client(null);
        assertThat(budget.getClient()).isNull();
    }

    @Test
    void templateTest() {
        Budget budget = getBudgetRandomSampleGenerator();
        BudgetTemplate budgetTemplateBack = getBudgetTemplateRandomSampleGenerator();

        budget.setTemplate(budgetTemplateBack);
        assertThat(budget.getTemplate()).isEqualTo(budgetTemplateBack);

        budget.template(null);
        assertThat(budget.getTemplate()).isNull();
    }

    @Test
    void weeklyPlanTest() {
        Budget budget = getBudgetRandomSampleGenerator();
        WeeklyPlan weeklyPlanBack = getWeeklyPlanRandomSampleGenerator();

        budget.setWeeklyPlan(weeklyPlanBack);
        assertThat(budget.getWeeklyPlan()).isEqualTo(weeklyPlanBack);

        budget.weeklyPlan(null);
        assertThat(budget.getWeeklyPlan()).isNull();
    }
}
