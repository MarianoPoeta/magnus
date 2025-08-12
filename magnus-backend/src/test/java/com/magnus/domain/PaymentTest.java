package com.magnus.domain;

import static com.magnus.domain.BudgetTestSamples.*;
import static com.magnus.domain.PaymentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.magnus.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Payment.class);
        Payment payment1 = getPaymentSample1();
        Payment payment2 = new Payment();
        assertThat(payment1).isNotEqualTo(payment2);

        payment2.setId(payment1.getId());
        assertThat(payment1).isEqualTo(payment2);

        payment2 = getPaymentSample2();
        assertThat(payment1).isNotEqualTo(payment2);
    }

    @Test
    void budgetTest() {
        Payment payment = getPaymentRandomSampleGenerator();
        Budget budgetBack = getBudgetRandomSampleGenerator();

        payment.setBudget(budgetBack);
        assertThat(payment.getBudget()).isEqualTo(budgetBack);

        payment.budget(null);
        assertThat(payment.getBudget()).isNull();
    }
}
