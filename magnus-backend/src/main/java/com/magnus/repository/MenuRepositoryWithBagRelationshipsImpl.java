package com.magnus.repository;

import com.magnus.domain.Menu;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class MenuRepositoryWithBagRelationshipsImpl implements MenuRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String MENUS_PARAMETER = "menus";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Menu> fetchBagRelationships(Optional<Menu> menu) {
        return menu.map(this::fetchIncludedFoodItems);
    }

    @Override
    public Page<Menu> fetchBagRelationships(Page<Menu> menus) {
        return new PageImpl<>(fetchBagRelationships(menus.getContent()), menus.getPageable(), menus.getTotalElements());
    }

    @Override
    public List<Menu> fetchBagRelationships(List<Menu> menus) {
        return Optional.of(menus).map(this::fetchIncludedFoodItems).orElse(Collections.emptyList());
    }

    Menu fetchIncludedFoodItems(Menu result) {
        return entityManager
            .createQuery("select menu from Menu menu left join fetch menu.includedFoodItems where menu.id = :id", Menu.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Menu> fetchIncludedFoodItems(List<Menu> menus) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, menus.size()).forEach(index -> order.put(menus.get(index).getId(), index));
        List<Menu> result = entityManager
            .createQuery("select menu from Menu menu left join fetch menu.includedFoodItems where menu in :menus", Menu.class)
            .setParameter(MENUS_PARAMETER, menus)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
