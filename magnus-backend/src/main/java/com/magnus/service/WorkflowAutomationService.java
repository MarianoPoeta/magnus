package com.magnus.service;

import com.magnus.domain.Budget;
import com.magnus.domain.Task;
import com.magnus.domain.TaskDependency;
import com.magnus.domain.enumeration.*;
import com.magnus.service.dto.BudgetDTO;
import com.magnus.service.dto.TaskDTO;
import com.magnus.service.dto.TaskDependencyDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for handling workflow automation when budget status changes to RESERVA.
 * This is the core service that orchestrates task generation and workflow triggers.
 */
@Service
@Transactional
public class WorkflowAutomationService {

    private static final Logger LOG = LoggerFactory.getLogger(WorkflowAutomationService.class);

    private final TaskService taskService;
    private final TaskDependencyService taskDependencyService;
    private final NotificationService notificationService;
    private final ApplicationEventPublisher eventPublisher;

    public WorkflowAutomationService(
        TaskService taskService,
        TaskDependencyService taskDependencyService,
        NotificationService notificationService,
        ApplicationEventPublisher eventPublisher
    ) {
        this.taskService = taskService;
        this.taskDependencyService = taskDependencyService;
        this.notificationService = notificationService;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Main workflow trigger when budget status changes to RESERVA.
     * Generates all necessary tasks with proper dependencies and timing.
     */
    public void triggerWorkflowForBudget(BudgetDTO budget) {
        LOG.info("Triggering workflow automation for budget: {} (ID: {})", budget.getName(), budget.getId());

        try {
            // 1. Generate all tasks for the budget
            List<TaskDTO> generatedTasks = generateTasksForBudget(budget);
            
            // 2. Set up task dependencies
            createTaskDependencies(generatedTasks);
            
            // 3. Send notifications to assigned roles
            sendWorkflowNotifications(budget, generatedTasks);
            
            // 4. Update budget workflow status
            markWorkflowTriggered(budget);
            
            LOG.info("Workflow automation completed successfully for budget: {}", budget.getId());
            
        } catch (Exception e) {
            LOG.error("Error triggering workflow for budget: {}", budget.getId(), e);
            throw new RuntimeException("Failed to trigger workflow automation", e);
        }
    }

    /**
     * Generates all required tasks for a budget based on its details.
     */
    private List<TaskDTO> generateTasksForBudget(BudgetDTO budget) {
        List<TaskDTO> tasks = new ArrayList<>();
        LocalDate eventDate = budget.getEventDate();
        
        // Generate Shopping Tasks (2-7 days before event)
        if (budget.getMealsAmount() != null && budget.getMealsAmount().compareTo(java.math.BigDecimal.ZERO) > 0) {
            TaskDTO shoppingTask = createShoppingTask(budget, eventDate.minusDays(3));
            tasks.add(taskService.save(shoppingTask));
        }
        
        // Generate Cooking Tasks (day of event)
        if (budget.getMealsAmount() != null && budget.getMealsAmount().compareTo(java.math.BigDecimal.ZERO) > 0) {
            TaskDTO cookingTask = createCookingTask(budget, eventDate);
            tasks.add(taskService.save(cookingTask));
        }
        
        // Generate Delivery Tasks (day of event)
        TaskDTO deliveryTask = createDeliveryTask(budget, eventDate);
        tasks.add(taskService.save(deliveryTask));
        
        // Generate Setup Tasks (day of event)
        TaskDTO setupTask = createSetupTask(budget, eventDate);
        tasks.add(taskService.save(setupTask));
        
        // Generate Activities Tasks if activities are included
        if (budget.getActivitiesAmount() != null && budget.getActivitiesAmount().compareTo(java.math.BigDecimal.ZERO) > 0) {
            TaskDTO activitiesTask = createActivitiesTask(budget, eventDate);
            tasks.add(taskService.save(activitiesTask));
        }
        
        // Generate Transport Tasks if transport is included
        if (budget.getTransportAmount() != null && budget.getTransportAmount().compareTo(java.math.BigDecimal.ZERO) > 0) {
            TaskDTO transportTask = createTransportTask(budget, eventDate);
            tasks.add(taskService.save(transportTask));
        }

        LOG.info("Generated {} tasks for budget: {}", tasks.size(), budget.getId());
        return tasks;
    }

    /**
     * Creates a shopping task for ingredient procurement.
     */
    private TaskDTO createShoppingTask(BudgetDTO budget, LocalDate dueDate) {
        TaskDTO task = new TaskDTO();
        task.setTitle("Shopping - " + budget.getName());
        task.setDescription("Procure all ingredients and supplies for " + budget.getName() + 
                          " (Event Date: " + budget.getEventDate() + ", Guests: " + budget.getGuestCount() + ")");
        task.setType(TaskType.SHOPPING);
        task.setPriority(TaskPriority.HIGH);
        task.setStatus(TaskStatus.TODO);
        task.setAssignedToRole(UserRole.LOGISTICS);
        task.setDueDate(dueDate);
        task.setDueTime(LocalTime.of(17, 0)); // 5:00 PM
        task.setEstimatedDuration(240); // 4 hours
        task.setLocation("Suppliers/Markets");
        task.setRequirements("Check shopping list for complete ingredient requirements");
        task.setAutoScheduled(true);
        task.setIsRecurring(false);
        task.setVersion(1);
        task.setConflictStatus(ConflictStatus.NONE);
        task.setCreatedAt(Instant.now());
        task.setUpdatedAt(Instant.now());
        
        return task;
    }

    /**
     * Creates a cooking task for meal preparation.
     */
    private TaskDTO createCookingTask(BudgetDTO budget, LocalDate dueDate) {
        TaskDTO task = new TaskDTO();
        task.setTitle("Cooking - " + budget.getName());
        task.setDescription("Prepare all meals for " + budget.getName() + 
                          " (Guests: " + budget.getGuestCount() + ")");
        task.setType(TaskType.COOKING);
        task.setPriority(TaskPriority.URGENT);
        task.setStatus(TaskStatus.BLOCKED); // Blocked until shopping is complete
        task.setAssignedToRole(UserRole.COOK);
        task.setDueDate(dueDate);
        task.setDueTime(calculateCookingStartTime(budget));
        task.setEstimatedDuration(calculateCookingDuration(budget.getGuestCount()));
        task.setLocation("Kitchen/Cooking Facility");
        task.setRequirements("All ingredients must be available and prepared");
        task.setAutoScheduled(true);
        task.setIsRecurring(false);
        task.setVersion(1);
        task.setConflictStatus(ConflictStatus.NONE);
        task.setCreatedAt(Instant.now());
        task.setUpdatedAt(Instant.now());
        
        return task;
    }

    /**
     * Creates a delivery task for equipment and supplies.
     */
    private TaskDTO createDeliveryTask(BudgetDTO budget, LocalDate dueDate) {
        TaskDTO task = new TaskDTO();
        task.setTitle("Delivery & Equipment - " + budget.getName());
        task.setDescription("Deliver equipment and supplies to " + budget.getEventLocation());
        task.setType(TaskType.DELIVERY);
        task.setPriority(TaskPriority.HIGH);
        task.setStatus(TaskStatus.TODO);
        task.setAssignedToRole(UserRole.LOGISTICS);
        task.setDueDate(dueDate);
        task.setDueTime(LocalTime.of(8, 0)); // 8:00 AM
        task.setEstimatedDuration(120); // 2 hours
        task.setLocation(budget.getEventLocation());
        task.setRequirements("Coordinate delivery timing with event schedule");
        task.setAutoScheduled(true);
        task.setIsRecurring(false);
        task.setVersion(1);
        task.setConflictStatus(ConflictStatus.NONE);
        task.setCreatedAt(Instant.now());
        task.setUpdatedAt(Instant.now());
        
        return task;
    }

    /**
     * Creates a setup task for event preparation.
     */
    private TaskDTO createSetupTask(BudgetDTO budget, LocalDate dueDate) {
        TaskDTO task = new TaskDTO();
        task.setTitle("Event Setup - " + budget.getName());
        task.setDescription("Setup venue and equipment for " + budget.getName());
        task.setType(TaskType.SETUP);
        task.setPriority(TaskPriority.HIGH);
        task.setStatus(TaskStatus.BLOCKED); // Blocked until delivery is complete
        task.setAssignedToRole(UserRole.LOGISTICS);
        task.setDueDate(dueDate);
        task.setDueTime(LocalTime.of(10, 0)); // 10:00 AM
        task.setEstimatedDuration(180); // 3 hours
        task.setLocation(budget.getEventLocation());
        task.setRequirements("All equipment must be delivered before setup");
        task.setAutoScheduled(true);
        task.setIsRecurring(false);
        task.setVersion(1);
        task.setConflictStatus(ConflictStatus.NONE);
        task.setCreatedAt(Instant.now());
        task.setUpdatedAt(Instant.now());
        
        return task;
    }

    /**
     * Creates an activities coordination task.
     */
    private TaskDTO createActivitiesTask(BudgetDTO budget, LocalDate dueDate) {
        TaskDTO task = new TaskDTO();
        task.setTitle("Activities Coordination - " + budget.getName());
        task.setDescription("Coordinate and manage activities for " + budget.getName());
        task.setType(TaskType.PREPARATION);
        task.setPriority(TaskPriority.MEDIUM);
        task.setStatus(TaskStatus.TODO);
        task.setAssignedToRole(UserRole.LOGISTICS);
        task.setDueDate(dueDate);
        task.setDueTime(LocalTime.of(14, 0)); // 2:00 PM
        task.setEstimatedDuration(120); // 2 hours
        task.setLocation(budget.getEventLocation());
        task.setRequirements("Confirm activity schedules and requirements");
        task.setAutoScheduled(true);
        task.setIsRecurring(false);
        task.setVersion(1);
        task.setConflictStatus(ConflictStatus.NONE);
        task.setCreatedAt(Instant.now());
        task.setUpdatedAt(Instant.now());
        
        return task;
    }

    /**
     * Creates a transport coordination task.
     */
    private TaskDTO createTransportTask(BudgetDTO budget, LocalDate dueDate) {
        TaskDTO task = new TaskDTO();
        task.setTitle("Transport Coordination - " + budget.getName());
        task.setDescription("Coordinate transportation for " + budget.getGuestCount() + " guests");
        task.setType(TaskType.DELIVERY);
        task.setPriority(TaskPriority.MEDIUM);
        task.setStatus(TaskStatus.TODO);
        task.setAssignedToRole(UserRole.LOGISTICS);
        task.setDueDate(dueDate.minusDays(1)); // Day before event
        task.setDueTime(LocalTime.of(16, 0)); // 4:00 PM
        task.setEstimatedDuration(60); // 1 hour
        task.setLocation("Transport Hub");
        task.setRequirements("Confirm transport schedules and capacity");
        task.setAutoScheduled(true);
        task.setIsRecurring(false);
        task.setVersion(1);
        task.setConflictStatus(ConflictStatus.NONE);
        task.setCreatedAt(Instant.now());
        task.setUpdatedAt(Instant.now());
        
        return task;
    }

    /**
     * Creates task dependencies to ensure proper execution order.
     */
    private void createTaskDependencies(List<TaskDTO> tasks) {
        TaskDTO shoppingTask = findTaskByType(tasks, TaskType.SHOPPING);
        TaskDTO cookingTask = findTaskByType(tasks, TaskType.COOKING);
        TaskDTO deliveryTask = findTaskByType(tasks, TaskType.DELIVERY);
        TaskDTO setupTask = findTaskByType(tasks, TaskType.SETUP);

        // Shopping blocks cooking
        if (shoppingTask != null && cookingTask != null) {
            createDependency(cookingTask.getId(), shoppingTask.getId(), DependencyType.BLOCKS);
        }

        // Delivery blocks setup
        if (deliveryTask != null && setupTask != null) {
            createDependency(setupTask.getId(), deliveryTask.getId(), DependencyType.BLOCKS);
        }
    }

    /**
     * Creates a task dependency relationship.
     */
    private void createDependency(Long dependentTaskId, Long prerequisiteTaskId, DependencyType dependencyType) {
        try {
            // Get the task DTOs
            TaskDTO dependentTask = taskService.findOne(dependentTaskId).orElse(null);
            TaskDTO prerequisiteTask = taskService.findOne(prerequisiteTaskId).orElse(null);
            
            if (dependentTask == null || prerequisiteTask == null) {
                LOG.warn("Cannot create dependency: tasks not found (dependent: {}, prerequisite: {})", 
                    dependentTaskId, prerequisiteTaskId);
                return;
            }

            TaskDependencyDTO dependency = new TaskDependencyDTO();
            dependency.setDependencyType(dependencyType);
            dependency.setNotes("Auto-generated workflow dependency");
            dependency.setIsActive(true);
            dependency.setCreatedAt(Instant.now());
            dependency.setDependentTask(dependentTask);
            dependency.setPrerequisiteTask(prerequisiteTask);
            
            taskDependencyService.save(dependency);
            
            LOG.debug("Created dependency: {} depends on {}", dependentTaskId, prerequisiteTaskId);
        } catch (Exception e) {
            LOG.error("Failed to create task dependency between {} and {}: {}", 
                dependentTaskId, prerequisiteTaskId, e.getMessage());
        }
    }

    /**
     * Sends notifications to users about new task assignments.
     */
    private void sendWorkflowNotifications(BudgetDTO budget, List<TaskDTO> tasks) {
        // Group tasks by assigned role
        tasks.stream()
             .collect(java.util.stream.Collectors.groupingBy(TaskDTO::getAssignedToRole))
             .forEach((role, roleTasks) -> {
                 // Send notification to each role
                 String message = String.format("New event approved: %s (%d new tasks assigned)", 
                                               budget.getName(), roleTasks.size());
                 sendRoleNotification(role, "New Event Assignment", message, budget.getId());
             });
    }

    /**
     * Sends a notification to all users of a specific role.
     */
    private void sendRoleNotification(UserRole targetRole, String title, String message, Long relatedBudgetId) {
        try {
            notificationService.sendWorkflowNotification(targetRole, title, message, relatedBudgetId);
            LOG.info("Notification sent successfully to role {}: {}", targetRole, title);
        } catch (Exception e) {
            LOG.error("Failed to send notification to role {}: {}", targetRole, title, e);
            // Don't fail the entire workflow for notification failures
        }
    }

    /**
     * Updates budget to mark workflow as triggered.
     */
    private void markWorkflowTriggered(BudgetDTO budget) {
        // This would be handled by the calling service
        LOG.info("Marking workflow as triggered for budget: {}", budget.getId());
    }

    /**
     * Calculates optimal cooking start time based on event details.
     */
    private LocalTime calculateCookingStartTime(BudgetDTO budget) {
        // Default to 4 hours before typical event time (6 PM)
        // This could be more sophisticated based on menu complexity
        return LocalTime.of(14, 0); // 2:00 PM
    }

    /**
     * Calculates cooking duration based on guest count.
     */
    private Integer calculateCookingDuration(Integer guestCount) {
        // Base time + additional time per guest
        int baseMinutes = 120; // 2 hours base
        int additionalMinutes = Math.max(0, (guestCount - 10) * 5); // 5 minutes per additional guest over 10
        return Math.min(baseMinutes + additionalMinutes, 480); // Max 8 hours
    }

    /**
     * Finds a task by type from the list.
     */
    private TaskDTO findTaskByType(List<TaskDTO> tasks, TaskType type) {
        return tasks.stream()
                   .filter(task -> task.getType() == type)
                   .findFirst()
                   .orElse(null);
    }
} 