package com.magnus.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.magnus.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TaskDependencyDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaskDependencyDTO.class);
        TaskDependencyDTO taskDependencyDTO1 = new TaskDependencyDTO();
        taskDependencyDTO1.setId(1L);
        TaskDependencyDTO taskDependencyDTO2 = new TaskDependencyDTO();
        assertThat(taskDependencyDTO1).isNotEqualTo(taskDependencyDTO2);
        taskDependencyDTO2.setId(taskDependencyDTO1.getId());
        assertThat(taskDependencyDTO1).isEqualTo(taskDependencyDTO2);
        taskDependencyDTO2.setId(2L);
        assertThat(taskDependencyDTO1).isNotEqualTo(taskDependencyDTO2);
        taskDependencyDTO1.setId(null);
        assertThat(taskDependencyDTO1).isNotEqualTo(taskDependencyDTO2);
    }
}
