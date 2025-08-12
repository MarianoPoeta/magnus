package com.magnus.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(
                Object.class,
                Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries())
            )
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build()
        );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.magnus.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.magnus.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.magnus.domain.User.class.getName());
            createCache(cm, com.magnus.domain.Authority.class.getName());
            createCache(cm, com.magnus.domain.User.class.getName() + ".authorities");
            createCache(cm, com.magnus.domain.AppUser.class.getName());
            createCache(cm, com.magnus.domain.AppUser.class.getName() + ".createdBudgets");
            createCache(cm, com.magnus.domain.AppUser.class.getName() + ".assignedBudgets");
            createCache(cm, com.magnus.domain.AppUser.class.getName() + ".createdTasks");
            createCache(cm, com.magnus.domain.AppUser.class.getName() + ".assignedTasks");
            createCache(cm, com.magnus.domain.AppUser.class.getName() + ".requestedNeeds");
            createCache(cm, com.magnus.domain.AppUser.class.getName() + ".fulfilledNeeds");
            createCache(cm, com.magnus.domain.AppUser.class.getName() + ".createdProducts");
            createCache(cm, com.magnus.domain.AppUser.class.getName() + ".createdNotifications");
            createCache(cm, com.magnus.domain.AppUser.class.getName() + ".receivedNotifications");
            createCache(cm, com.magnus.domain.AppUser.class.getName() + ".purchasedItems");
            createCache(cm, com.magnus.domain.AppUser.class.getName() + ".createdWeeklyPlans");
            createCache(cm, com.magnus.domain.AppUser.class.getName() + ".createdTemplates");
            createCache(cm, com.magnus.domain.AppUser.class.getName() + ".createdConfigs");
            createCache(cm, com.magnus.domain.AppUser.class.getName() + ".createdTriggers");
            createCache(cm, com.magnus.domain.AppUser.class.getName() + ".resolvedConflicts");
            createCache(cm, com.magnus.domain.Client.class.getName());
            createCache(cm, com.magnus.domain.Client.class.getName() + ".budgets");
            createCache(cm, com.magnus.domain.BudgetTemplate.class.getName());
            createCache(cm, com.magnus.domain.BudgetTemplate.class.getName() + ".budgets");
            createCache(cm, com.magnus.domain.WeeklyPlan.class.getName());
            createCache(cm, com.magnus.domain.WeeklyPlan.class.getName() + ".budgets");
            createCache(cm, com.magnus.domain.WeeklyPlan.class.getName() + ".shoppingItems");
            createCache(cm, com.magnus.domain.WeeklyPlan.class.getName() + ".tasks");
            createCache(cm, com.magnus.domain.Budget.class.getName());
            createCache(cm, com.magnus.domain.Budget.class.getName() + ".budgetItems");
            createCache(cm, com.magnus.domain.Budget.class.getName() + ".payments");
            createCache(cm, com.magnus.domain.Budget.class.getName() + ".tasks");
            createCache(cm, com.magnus.domain.Budget.class.getName() + ".transportAssignments");
            createCache(cm, com.magnus.domain.Budget.class.getName() + ".cookingSchedules");
            createCache(cm, com.magnus.domain.BudgetItem.class.getName());
            createCache(cm, com.magnus.domain.Payment.class.getName());
            createCache(cm, com.magnus.domain.Task.class.getName());
            createCache(cm, com.magnus.domain.Task.class.getName() + ".needs");
            createCache(cm, com.magnus.domain.Task.class.getName() + ".productRequirements");
            createCache(cm, com.magnus.domain.Task.class.getName() + ".taskDependencies");
            createCache(cm, com.magnus.domain.TaskDependency.class.getName());
            createCache(cm, com.magnus.domain.ShoppingItem.class.getName());
            createCache(cm, com.magnus.domain.CookingSchedule.class.getName());
            createCache(cm, com.magnus.domain.CookingSchedule.class.getName() + ".cookingIngredients");
            createCache(cm, com.magnus.domain.CookingIngredient.class.getName());
            createCache(cm, com.magnus.domain.Product.class.getName());
            createCache(cm, com.magnus.domain.ProductRequirement.class.getName());
            createCache(cm, com.magnus.domain.Menu.class.getName());
            createCache(cm, com.magnus.domain.Menu.class.getName() + ".menuItems");
            createCache(cm, com.magnus.domain.Menu.class.getName() + ".includedFoodItems");
            createCache(cm, com.magnus.domain.MenuItem.class.getName());
            createCache(cm, com.magnus.domain.FoodItem.class.getName());
            createCache(cm, com.magnus.domain.FoodItem.class.getName() + ".productRequirements");
            createCache(cm, com.magnus.domain.FoodItem.class.getName() + ".availableMenus");
            createCache(cm, com.magnus.domain.Activity.class.getName());
            createCache(cm, com.magnus.domain.Activity.class.getName() + ".productRequirements");
            createCache(cm, com.magnus.domain.Activity.class.getName() + ".transportAssignments");
            createCache(cm, com.magnus.domain.Accommodation.class.getName());
            createCache(cm, com.magnus.domain.Transport.class.getName());
            createCache(cm, com.magnus.domain.TransportAssignment.class.getName());
            createCache(cm, com.magnus.domain.Need.class.getName());
            createCache(cm, com.magnus.domain.Notification.class.getName());
            createCache(cm, com.magnus.domain.SystemConfig.class.getName());
            createCache(cm, com.magnus.domain.AuditLog.class.getName());
            createCache(cm, com.magnus.domain.WorkflowTrigger.class.getName());
            createCache(cm, com.magnus.domain.ConflictResolution.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
