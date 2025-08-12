package com.magnus.domain;

import static com.magnus.domain.AppUserTestSamples.*;
import static com.magnus.domain.ProductTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.magnus.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Product.class);
        Product product1 = getProductSample1();
        Product product2 = new Product();
        assertThat(product1).isNotEqualTo(product2);

        product2.setId(product1.getId());
        assertThat(product1).isEqualTo(product2);

        product2 = getProductSample2();
        assertThat(product1).isNotEqualTo(product2);
    }

    @Test
    void createdByTest() {
        Product product = getProductRandomSampleGenerator();
        AppUser appUserBack = getAppUserRandomSampleGenerator();

        product.setCreatedBy(appUserBack);
        assertThat(product.getCreatedBy()).isEqualTo(appUserBack);

        product.createdBy(null);
        assertThat(product.getCreatedBy()).isNull();
    }
}
