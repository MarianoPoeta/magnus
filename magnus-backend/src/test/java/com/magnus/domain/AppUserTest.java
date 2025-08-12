package com.magnus.domain;

import static com.magnus.domain.AppUserTestSamples.*;
import static com.magnus.domain.BudgetTemplateTestSamples.*;
import static com.magnus.domain.BudgetTestSamples.*;
import static com.magnus.domain.ConflictResolutionTestSamples.*;
import static com.magnus.domain.NeedTestSamples.*;
import static com.magnus.domain.NotificationTestSamples.*;
import static com.magnus.domain.ProductTestSamples.*;
import static com.magnus.domain.ShoppingItemTestSamples.*;
import static com.magnus.domain.SystemConfigTestSamples.*;
import static com.magnus.domain.TaskTestSamples.*;
import static com.magnus.domain.WeeklyPlanTestSamples.*;
import static com.magnus.domain.WorkflowTriggerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.magnus.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AppUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppUser.class);
        AppUser appUser1 = getAppUserSample1();
        AppUser appUser2 = new AppUser();
        assertThat(appUser1).isNotEqualTo(appUser2);

        appUser2.setId(appUser1.getId());
        assertThat(appUser1).isEqualTo(appUser2);

        appUser2 = getAppUserSample2();
        assertThat(appUser1).isNotEqualTo(appUser2);
    }

    @Test
    void createdBudgetsTest() {
        AppUser appUser = getAppUserRandomSampleGenerator();
        Budget budgetBack = getBudgetRandomSampleGenerator();

        appUser.addCreatedBudgets(budgetBack);
        assertThat(appUser.getCreatedBudgets()).containsOnly(budgetBack);
        assertThat(budgetBack.getCreatedBy()).isEqualTo(appUser);

        appUser.removeCreatedBudgets(budgetBack);
        assertThat(appUser.getCreatedBudgets()).doesNotContain(budgetBack);
        assertThat(budgetBack.getCreatedBy()).isNull();

        appUser.createdBudgets(new HashSet<>(Set.of(budgetBack)));
        assertThat(appUser.getCreatedBudgets()).containsOnly(budgetBack);
        assertThat(budgetBack.getCreatedBy()).isEqualTo(appUser);

        appUser.setCreatedBudgets(new HashSet<>());
        assertThat(appUser.getCreatedBudgets()).doesNotContain(budgetBack);
        assertThat(budgetBack.getCreatedBy()).isNull();
    }

    @Test
    void assignedBudgetsTest() {
        AppUser appUser = getAppUserRandomSampleGenerator();
        Budget budgetBack = getBudgetRandomSampleGenerator();

        appUser.addAssignedBudgets(budgetBack);
        assertThat(appUser.getAssignedBudgets()).containsOnly(budgetBack);
        assertThat(budgetBack.getAssignedTo()).isEqualTo(appUser);

        appUser.removeAssignedBudgets(budgetBack);
        assertThat(appUser.getAssignedBudgets()).doesNotContain(budgetBack);
        assertThat(budgetBack.getAssignedTo()).isNull();

        appUser.assignedBudgets(new HashSet<>(Set.of(budgetBack)));
        assertThat(appUser.getAssignedBudgets()).containsOnly(budgetBack);
        assertThat(budgetBack.getAssignedTo()).isEqualTo(appUser);

        appUser.setAssignedBudgets(new HashSet<>());
        assertThat(appUser.getAssignedBudgets()).doesNotContain(budgetBack);
        assertThat(budgetBack.getAssignedTo()).isNull();
    }

    @Test
    void createdTasksTest() {
        AppUser appUser = getAppUserRandomSampleGenerator();
        Task taskBack = getTaskRandomSampleGenerator();

        appUser.addCreatedTasks(taskBack);
        assertThat(appUser.getCreatedTasks()).containsOnly(taskBack);
        assertThat(taskBack.getCreatedBy()).isEqualTo(appUser);

        appUser.removeCreatedTasks(taskBack);
        assertThat(appUser.getCreatedTasks()).doesNotContain(taskBack);
        assertThat(taskBack.getCreatedBy()).isNull();

        appUser.createdTasks(new HashSet<>(Set.of(taskBack)));
        assertThat(appUser.getCreatedTasks()).containsOnly(taskBack);
        assertThat(taskBack.getCreatedBy()).isEqualTo(appUser);

        appUser.setCreatedTasks(new HashSet<>());
        assertThat(appUser.getCreatedTasks()).doesNotContain(taskBack);
        assertThat(taskBack.getCreatedBy()).isNull();
    }

    @Test
    void assignedTasksTest() {
        AppUser appUser = getAppUserRandomSampleGenerator();
        Task taskBack = getTaskRandomSampleGenerator();

        appUser.addAssignedTasks(taskBack);
        assertThat(appUser.getAssignedTasks()).containsOnly(taskBack);
        assertThat(taskBack.getAssignedTo()).isEqualTo(appUser);

        appUser.removeAssignedTasks(taskBack);
        assertThat(appUser.getAssignedTasks()).doesNotContain(taskBack);
        assertThat(taskBack.getAssignedTo()).isNull();

        appUser.assignedTasks(new HashSet<>(Set.of(taskBack)));
        assertThat(appUser.getAssignedTasks()).containsOnly(taskBack);
        assertThat(taskBack.getAssignedTo()).isEqualTo(appUser);

        appUser.setAssignedTasks(new HashSet<>());
        assertThat(appUser.getAssignedTasks()).doesNotContain(taskBack);
        assertThat(taskBack.getAssignedTo()).isNull();
    }

    @Test
    void requestedNeedsTest() {
        AppUser appUser = getAppUserRandomSampleGenerator();
        Need needBack = getNeedRandomSampleGenerator();

        appUser.addRequestedNeeds(needBack);
        assertThat(appUser.getRequestedNeeds()).containsOnly(needBack);
        assertThat(needBack.getRequestedBy()).isEqualTo(appUser);

        appUser.removeRequestedNeeds(needBack);
        assertThat(appUser.getRequestedNeeds()).doesNotContain(needBack);
        assertThat(needBack.getRequestedBy()).isNull();

        appUser.requestedNeeds(new HashSet<>(Set.of(needBack)));
        assertThat(appUser.getRequestedNeeds()).containsOnly(needBack);
        assertThat(needBack.getRequestedBy()).isEqualTo(appUser);

        appUser.setRequestedNeeds(new HashSet<>());
        assertThat(appUser.getRequestedNeeds()).doesNotContain(needBack);
        assertThat(needBack.getRequestedBy()).isNull();
    }

    @Test
    void fulfilledNeedsTest() {
        AppUser appUser = getAppUserRandomSampleGenerator();
        Need needBack = getNeedRandomSampleGenerator();

        appUser.addFulfilledNeeds(needBack);
        assertThat(appUser.getFulfilledNeeds()).containsOnly(needBack);
        assertThat(needBack.getFulfilledBy()).isEqualTo(appUser);

        appUser.removeFulfilledNeeds(needBack);
        assertThat(appUser.getFulfilledNeeds()).doesNotContain(needBack);
        assertThat(needBack.getFulfilledBy()).isNull();

        appUser.fulfilledNeeds(new HashSet<>(Set.of(needBack)));
        assertThat(appUser.getFulfilledNeeds()).containsOnly(needBack);
        assertThat(needBack.getFulfilledBy()).isEqualTo(appUser);

        appUser.setFulfilledNeeds(new HashSet<>());
        assertThat(appUser.getFulfilledNeeds()).doesNotContain(needBack);
        assertThat(needBack.getFulfilledBy()).isNull();
    }

    @Test
    void createdProductsTest() {
        AppUser appUser = getAppUserRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        appUser.addCreatedProducts(productBack);
        assertThat(appUser.getCreatedProducts()).containsOnly(productBack);
        assertThat(productBack.getCreatedBy()).isEqualTo(appUser);

        appUser.removeCreatedProducts(productBack);
        assertThat(appUser.getCreatedProducts()).doesNotContain(productBack);
        assertThat(productBack.getCreatedBy()).isNull();

        appUser.createdProducts(new HashSet<>(Set.of(productBack)));
        assertThat(appUser.getCreatedProducts()).containsOnly(productBack);
        assertThat(productBack.getCreatedBy()).isEqualTo(appUser);

        appUser.setCreatedProducts(new HashSet<>());
        assertThat(appUser.getCreatedProducts()).doesNotContain(productBack);
        assertThat(productBack.getCreatedBy()).isNull();
    }

    @Test
    void createdNotificationsTest() {
        AppUser appUser = getAppUserRandomSampleGenerator();
        Notification notificationBack = getNotificationRandomSampleGenerator();

        appUser.addCreatedNotifications(notificationBack);
        assertThat(appUser.getCreatedNotifications()).containsOnly(notificationBack);
        assertThat(notificationBack.getCreatedBy()).isEqualTo(appUser);

        appUser.removeCreatedNotifications(notificationBack);
        assertThat(appUser.getCreatedNotifications()).doesNotContain(notificationBack);
        assertThat(notificationBack.getCreatedBy()).isNull();

        appUser.createdNotifications(new HashSet<>(Set.of(notificationBack)));
        assertThat(appUser.getCreatedNotifications()).containsOnly(notificationBack);
        assertThat(notificationBack.getCreatedBy()).isEqualTo(appUser);

        appUser.setCreatedNotifications(new HashSet<>());
        assertThat(appUser.getCreatedNotifications()).doesNotContain(notificationBack);
        assertThat(notificationBack.getCreatedBy()).isNull();
    }

    @Test
    void receivedNotificationsTest() {
        AppUser appUser = getAppUserRandomSampleGenerator();
        Notification notificationBack = getNotificationRandomSampleGenerator();

        appUser.addReceivedNotifications(notificationBack);
        assertThat(appUser.getReceivedNotifications()).containsOnly(notificationBack);
        assertThat(notificationBack.getTargetUser()).isEqualTo(appUser);

        appUser.removeReceivedNotifications(notificationBack);
        assertThat(appUser.getReceivedNotifications()).doesNotContain(notificationBack);
        assertThat(notificationBack.getTargetUser()).isNull();

        appUser.receivedNotifications(new HashSet<>(Set.of(notificationBack)));
        assertThat(appUser.getReceivedNotifications()).containsOnly(notificationBack);
        assertThat(notificationBack.getTargetUser()).isEqualTo(appUser);

        appUser.setReceivedNotifications(new HashSet<>());
        assertThat(appUser.getReceivedNotifications()).doesNotContain(notificationBack);
        assertThat(notificationBack.getTargetUser()).isNull();
    }

    @Test
    void purchasedItemsTest() {
        AppUser appUser = getAppUserRandomSampleGenerator();
        ShoppingItem shoppingItemBack = getShoppingItemRandomSampleGenerator();

        appUser.addPurchasedItems(shoppingItemBack);
        assertThat(appUser.getPurchasedItems()).containsOnly(shoppingItemBack);
        assertThat(shoppingItemBack.getPurchasedBy()).isEqualTo(appUser);

        appUser.removePurchasedItems(shoppingItemBack);
        assertThat(appUser.getPurchasedItems()).doesNotContain(shoppingItemBack);
        assertThat(shoppingItemBack.getPurchasedBy()).isNull();

        appUser.purchasedItems(new HashSet<>(Set.of(shoppingItemBack)));
        assertThat(appUser.getPurchasedItems()).containsOnly(shoppingItemBack);
        assertThat(shoppingItemBack.getPurchasedBy()).isEqualTo(appUser);

        appUser.setPurchasedItems(new HashSet<>());
        assertThat(appUser.getPurchasedItems()).doesNotContain(shoppingItemBack);
        assertThat(shoppingItemBack.getPurchasedBy()).isNull();
    }

    @Test
    void createdWeeklyPlansTest() {
        AppUser appUser = getAppUserRandomSampleGenerator();
        WeeklyPlan weeklyPlanBack = getWeeklyPlanRandomSampleGenerator();

        appUser.addCreatedWeeklyPlans(weeklyPlanBack);
        assertThat(appUser.getCreatedWeeklyPlans()).containsOnly(weeklyPlanBack);
        assertThat(weeklyPlanBack.getCreatedBy()).isEqualTo(appUser);

        appUser.removeCreatedWeeklyPlans(weeklyPlanBack);
        assertThat(appUser.getCreatedWeeklyPlans()).doesNotContain(weeklyPlanBack);
        assertThat(weeklyPlanBack.getCreatedBy()).isNull();

        appUser.createdWeeklyPlans(new HashSet<>(Set.of(weeklyPlanBack)));
        assertThat(appUser.getCreatedWeeklyPlans()).containsOnly(weeklyPlanBack);
        assertThat(weeklyPlanBack.getCreatedBy()).isEqualTo(appUser);

        appUser.setCreatedWeeklyPlans(new HashSet<>());
        assertThat(appUser.getCreatedWeeklyPlans()).doesNotContain(weeklyPlanBack);
        assertThat(weeklyPlanBack.getCreatedBy()).isNull();
    }

    @Test
    void createdTemplatesTest() {
        AppUser appUser = getAppUserRandomSampleGenerator();
        BudgetTemplate budgetTemplateBack = getBudgetTemplateRandomSampleGenerator();

        appUser.addCreatedTemplates(budgetTemplateBack);
        assertThat(appUser.getCreatedTemplates()).containsOnly(budgetTemplateBack);
        assertThat(budgetTemplateBack.getCreatedBy()).isEqualTo(appUser);

        appUser.removeCreatedTemplates(budgetTemplateBack);
        assertThat(appUser.getCreatedTemplates()).doesNotContain(budgetTemplateBack);
        assertThat(budgetTemplateBack.getCreatedBy()).isNull();

        appUser.createdTemplates(new HashSet<>(Set.of(budgetTemplateBack)));
        assertThat(appUser.getCreatedTemplates()).containsOnly(budgetTemplateBack);
        assertThat(budgetTemplateBack.getCreatedBy()).isEqualTo(appUser);

        appUser.setCreatedTemplates(new HashSet<>());
        assertThat(appUser.getCreatedTemplates()).doesNotContain(budgetTemplateBack);
        assertThat(budgetTemplateBack.getCreatedBy()).isNull();
    }

    @Test
    void createdConfigsTest() {
        AppUser appUser = getAppUserRandomSampleGenerator();
        SystemConfig systemConfigBack = getSystemConfigRandomSampleGenerator();

        appUser.addCreatedConfigs(systemConfigBack);
        assertThat(appUser.getCreatedConfigs()).containsOnly(systemConfigBack);
        assertThat(systemConfigBack.getCreatedBy()).isEqualTo(appUser);

        appUser.removeCreatedConfigs(systemConfigBack);
        assertThat(appUser.getCreatedConfigs()).doesNotContain(systemConfigBack);
        assertThat(systemConfigBack.getCreatedBy()).isNull();

        appUser.createdConfigs(new HashSet<>(Set.of(systemConfigBack)));
        assertThat(appUser.getCreatedConfigs()).containsOnly(systemConfigBack);
        assertThat(systemConfigBack.getCreatedBy()).isEqualTo(appUser);

        appUser.setCreatedConfigs(new HashSet<>());
        assertThat(appUser.getCreatedConfigs()).doesNotContain(systemConfigBack);
        assertThat(systemConfigBack.getCreatedBy()).isNull();
    }

    @Test
    void createdTriggersTest() {
        AppUser appUser = getAppUserRandomSampleGenerator();
        WorkflowTrigger workflowTriggerBack = getWorkflowTriggerRandomSampleGenerator();

        appUser.addCreatedTriggers(workflowTriggerBack);
        assertThat(appUser.getCreatedTriggers()).containsOnly(workflowTriggerBack);
        assertThat(workflowTriggerBack.getCreatedBy()).isEqualTo(appUser);

        appUser.removeCreatedTriggers(workflowTriggerBack);
        assertThat(appUser.getCreatedTriggers()).doesNotContain(workflowTriggerBack);
        assertThat(workflowTriggerBack.getCreatedBy()).isNull();

        appUser.createdTriggers(new HashSet<>(Set.of(workflowTriggerBack)));
        assertThat(appUser.getCreatedTriggers()).containsOnly(workflowTriggerBack);
        assertThat(workflowTriggerBack.getCreatedBy()).isEqualTo(appUser);

        appUser.setCreatedTriggers(new HashSet<>());
        assertThat(appUser.getCreatedTriggers()).doesNotContain(workflowTriggerBack);
        assertThat(workflowTriggerBack.getCreatedBy()).isNull();
    }

    @Test
    void resolvedConflictsTest() {
        AppUser appUser = getAppUserRandomSampleGenerator();
        ConflictResolution conflictResolutionBack = getConflictResolutionRandomSampleGenerator();

        appUser.addResolvedConflicts(conflictResolutionBack);
        assertThat(appUser.getResolvedConflicts()).containsOnly(conflictResolutionBack);
        assertThat(conflictResolutionBack.getResolvedBy()).isEqualTo(appUser);

        appUser.removeResolvedConflicts(conflictResolutionBack);
        assertThat(appUser.getResolvedConflicts()).doesNotContain(conflictResolutionBack);
        assertThat(conflictResolutionBack.getResolvedBy()).isNull();

        appUser.resolvedConflicts(new HashSet<>(Set.of(conflictResolutionBack)));
        assertThat(appUser.getResolvedConflicts()).containsOnly(conflictResolutionBack);
        assertThat(conflictResolutionBack.getResolvedBy()).isEqualTo(appUser);

        appUser.setResolvedConflicts(new HashSet<>());
        assertThat(appUser.getResolvedConflicts()).doesNotContain(conflictResolutionBack);
        assertThat(conflictResolutionBack.getResolvedBy()).isNull();
    }
}
