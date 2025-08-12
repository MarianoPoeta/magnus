package com.magnus.repository;

import com.magnus.domain.TaskDependency;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TaskDependency entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TaskDependencyRepository extends JpaRepository<TaskDependency, Long> {
    
    /**
     * Find dependencies where the given task is the dependent task.
     */
    List<TaskDependency> findByDependentTaskId(Long taskId);
    
    /**
     * Find dependencies where the given task is the prerequisite task.
     */
    List<TaskDependency> findByPrerequisiteTaskId(Long taskId);
}
