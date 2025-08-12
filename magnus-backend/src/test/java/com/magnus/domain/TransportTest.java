package com.magnus.domain;

import static com.magnus.domain.TransportTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.magnus.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransportTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Transport.class);
        Transport transport1 = getTransportSample1();
        Transport transport2 = new Transport();
        assertThat(transport1).isNotEqualTo(transport2);

        transport2.setId(transport1.getId());
        assertThat(transport1).isEqualTo(transport2);

        transport2 = getTransportSample2();
        assertThat(transport1).isNotEqualTo(transport2);
    }
}
