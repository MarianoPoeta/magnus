package com.magnus.domain;

import static com.magnus.domain.AccommodationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.magnus.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AccommodationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Accommodation.class);
        Accommodation accommodation1 = getAccommodationSample1();
        Accommodation accommodation2 = new Accommodation();
        assertThat(accommodation1).isNotEqualTo(accommodation2);

        accommodation2.setId(accommodation1.getId());
        assertThat(accommodation1).isEqualTo(accommodation2);

        accommodation2 = getAccommodationSample2();
        assertThat(accommodation1).isNotEqualTo(accommodation2);
    }
}
