package com.magnus.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.magnus.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductRequirementDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductRequirementDTO.class);
        ProductRequirementDTO productRequirementDTO1 = new ProductRequirementDTO();
        productRequirementDTO1.setId(1L);
        ProductRequirementDTO productRequirementDTO2 = new ProductRequirementDTO();
        assertThat(productRequirementDTO1).isNotEqualTo(productRequirementDTO2);
        productRequirementDTO2.setId(productRequirementDTO1.getId());
        assertThat(productRequirementDTO1).isEqualTo(productRequirementDTO2);
        productRequirementDTO2.setId(2L);
        assertThat(productRequirementDTO1).isNotEqualTo(productRequirementDTO2);
        productRequirementDTO1.setId(null);
        assertThat(productRequirementDTO1).isNotEqualTo(productRequirementDTO2);
    }
}
