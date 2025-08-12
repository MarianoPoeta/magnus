package com.magnus.domain;

import static com.magnus.domain.TaskDependencyTestSamples.*;
import static com.magnus.domain.TaskTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.magnus.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TaskDependencyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaskDependency.class);
        TaskDependency taskDependency1 = getTaskDependencySample1();
        TaskDependency taskDependency2 = new TaskDependency();
        assertThat(taskDependency1).isNotEqualTo(taskDependency2);

        taskDependency2.setId(taskDependency1.getId());
        assertThat(taskDependency1).isEqualTo(taskDependency2);

        taskDependency2 = getTaskDependencySample2();
        assertThat(taskDependency1).isNotEqualTo(taskDependency2);
    }

    @Test
    void prerequisiteTaskTest() {
        TaskDependency taskDependency = getTaskDependencyRandomSampleGenerator();
        Task taskBack = getTaskRandomSampleGenerator();

        taskDependency.setPrerequisiteTask(taskBack);
        assertThat(taskDependency.getPrerequisiteTask()).isEqualTo(taskBack);

        taskDependency.prerequisiteTask(null);
        assertThat(taskDependency.getPrerequisiteTask()).isNull();
    }

    @Test
    void dependentTaskTest() {
        TaskDependency taskDependency = getTaskDependencyRandomSampleGenerator();
        Task taskBack = getTaskRandomSampleGenerator();

        taskDependency.setDependentTask(taskBack);
        assertThat(taskDependency.getDependentTask()).isEqualTo(taskBack);

        taskDependency.dependentTask(null);
        assertThat(taskDependency.getDependentTask()).isNull();
    }
}
