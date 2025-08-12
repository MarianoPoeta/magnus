package com.magnus.domain;

import static com.magnus.domain.AppUserTestSamples.*;
import static com.magnus.domain.NotificationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.magnus.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NotificationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Notification.class);
        Notification notification1 = getNotificationSample1();
        Notification notification2 = new Notification();
        assertThat(notification1).isNotEqualTo(notification2);

        notification2.setId(notification1.getId());
        assertThat(notification1).isEqualTo(notification2);

        notification2 = getNotificationSample2();
        assertThat(notification1).isNotEqualTo(notification2);
    }

    @Test
    void createdByTest() {
        Notification notification = getNotificationRandomSampleGenerator();
        AppUser appUserBack = getAppUserRandomSampleGenerator();

        notification.setCreatedBy(appUserBack);
        assertThat(notification.getCreatedBy()).isEqualTo(appUserBack);

        notification.createdBy(null);
        assertThat(notification.getCreatedBy()).isNull();
    }

    @Test
    void targetUserTest() {
        Notification notification = getNotificationRandomSampleGenerator();
        AppUser appUserBack = getAppUserRandomSampleGenerator();

        notification.setTargetUser(appUserBack);
        assertThat(notification.getTargetUser()).isEqualTo(appUserBack);

        notification.targetUser(null);
        assertThat(notification.getTargetUser()).isNull();
    }
}
