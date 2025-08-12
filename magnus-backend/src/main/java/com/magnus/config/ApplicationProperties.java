package com.magnus.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Magnus.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link tech.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private final Liquibase liquibase = new Liquibase();

    private final Workflow workflow = new Workflow();
    private final Cache cache = new Cache();
    private final Websocket websocket = new Websocket();
    private final Realtime realtime = new Realtime();

    // jhipster-needle-application-properties-property

    public Liquibase getLiquibase() {
        return liquibase;
    }

    public Workflow getWorkflow() {
        return workflow;
    }

    public Cache getCache() {
        return cache;
    }

    public Websocket getWebsocket() {
        return websocket;
    }

    public Realtime getRealtime() {
        return realtime;
    }

    // jhipster-needle-application-properties-property-getter

    public static class Liquibase {

        private Boolean asyncStart = true;

        public Boolean getAsyncStart() {
            return asyncStart;
        }

        public void setAsyncStart(Boolean asyncStart) {
            this.asyncStart = asyncStart;
        }
    }
    // jhipster-needle-application-properties-property-class

    public static class Workflow {

        private final TaskGeneration taskGeneration = new TaskGeneration();
        private final Notifications notifications = new Notifications();
        private final Scheduling scheduling = new Scheduling();

        public TaskGeneration getTaskGeneration() {
            return taskGeneration;
        }

        public Notifications getNotifications() {
            return notifications;
        }

        public Scheduling getScheduling() {
            return scheduling;
        }

        public static class TaskGeneration {
            private Boolean enabled;
            private Boolean asyncProcessing;

            public Boolean getEnabled() {
                return enabled;
            }

            public void setEnabled(Boolean enabled) {
                this.enabled = enabled;
            }

            public Boolean getAsyncProcessing() {
                return asyncProcessing;
            }

            public void setAsyncProcessing(Boolean asyncProcessing) {
                this.asyncProcessing = asyncProcessing;
            }
        }

        public static class Notifications {
            private Boolean enabled;

            public Boolean getEnabled() {
                return enabled;
            }

            public void setEnabled(Boolean enabled) {
                this.enabled = enabled;
            }
        }

        public static class Scheduling {
            private Integer shoppingDaysBefore;
            private Integer preparationDaysBefore;
            private Integer deliveryDaysBefore;
            private Integer cookingHoursBefore;

            public Integer getShoppingDaysBefore() {
                return shoppingDaysBefore;
            }

            public void setShoppingDaysBefore(Integer shoppingDaysBefore) {
                this.shoppingDaysBefore = shoppingDaysBefore;
            }

            public Integer getPreparationDaysBefore() {
                return preparationDaysBefore;
            }

            public void setPreparationDaysBefore(Integer preparationDaysBefore) {
                this.preparationDaysBefore = preparationDaysBefore;
            }

            public Integer getDeliveryDaysBefore() {
                return deliveryDaysBefore;
            }

            public void setDeliveryDaysBefore(Integer deliveryDaysBefore) {
                this.deliveryDaysBefore = deliveryDaysBefore;
            }

            public Integer getCookingHoursBefore() {
                return cookingHoursBefore;
            }

            public void setCookingHoursBefore(Integer cookingHoursBefore) {
                this.cookingHoursBefore = cookingHoursBefore;
            }
        }
    }

    public static class Cache {
        private Integer templatesTtl;
        private Integer shoppingListsTtl;
        private Integer taskListsTtl;
        private Integer dashboardAnalyticsTtl;

        public Integer getTemplatesTtl() {
            return templatesTtl;
        }

        public void setTemplatesTtl(Integer templatesTtl) {
            this.templatesTtl = templatesTtl;
        }

        public Integer getShoppingListsTtl() {
            return shoppingListsTtl;
        }

        public void setShoppingListsTtl(Integer shoppingListsTtl) {
            this.shoppingListsTtl = shoppingListsTtl;
        }

        public Integer getTaskListsTtl() {
            return taskListsTtl;
        }

        public void setTaskListsTtl(Integer taskListsTtl) {
            this.taskListsTtl = taskListsTtl;
        }

        public Integer getDashboardAnalyticsTtl() {
            return dashboardAnalyticsTtl;
        }

        public void setDashboardAnalyticsTtl(Integer dashboardAnalyticsTtl) {
            this.dashboardAnalyticsTtl = dashboardAnalyticsTtl;
        }
    }

    public static class Websocket {
        private Integer heartbeatInterval;
        private Integer taskSchedulerPoolSize;

        public Integer getHeartbeatInterval() {
            return heartbeatInterval;
        }

        public void setHeartbeatInterval(Integer heartbeatInterval) {
            this.heartbeatInterval = heartbeatInterval;
        }

        public Integer getTaskSchedulerPoolSize() {
            return taskSchedulerPoolSize;
        }

        public void setTaskSchedulerPoolSize(Integer taskSchedulerPoolSize) {
            this.taskSchedulerPoolSize = taskSchedulerPoolSize;
        }
    }

    public static class Realtime {
        private Boolean cookLogisticsSync;
        private Boolean shoppingConsolidation;
        private Boolean taskStatusBroadcast;
        private Boolean ingredientModificationSync;

        public Boolean getCookLogisticsSync() {
            return cookLogisticsSync;
        }

        public void setCookLogisticsSync(Boolean cookLogisticsSync) {
            this.cookLogisticsSync = cookLogisticsSync;
        }

        public Boolean getShoppingConsolidation() {
            return shoppingConsolidation;
        }

        public void setShoppingConsolidation(Boolean shoppingConsolidation) {
            this.shoppingConsolidation = shoppingConsolidation;
        }

        public Boolean getTaskStatusBroadcast() {
            return taskStatusBroadcast;
        }

        public void setTaskStatusBroadcast(Boolean taskStatusBroadcast) {
            this.taskStatusBroadcast = taskStatusBroadcast;
        }

        public Boolean getIngredientModificationSync() {
            return ingredientModificationSync;
        }

        public void setIngredientModificationSync(Boolean ingredientModificationSync) {
            this.ingredientModificationSync = ingredientModificationSync;
        }
    }
}
