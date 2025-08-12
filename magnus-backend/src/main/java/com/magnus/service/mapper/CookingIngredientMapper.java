package com.magnus.service.mapper;

import com.magnus.domain.CookingIngredient;
import com.magnus.domain.CookingSchedule;
import com.magnus.domain.ProductRequirement;
import com.magnus.service.dto.CookingIngredientDTO;
import com.magnus.service.dto.CookingScheduleDTO;
import com.magnus.service.dto.ProductRequirementDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CookingIngredient} and its DTO {@link CookingIngredientDTO}.
 */
@Mapper(componentModel = "spring")
public interface CookingIngredientMapper extends EntityMapper<CookingIngredientDTO, CookingIngredient> {
    @Mapping(target = "productRequirement", source = "productRequirement", qualifiedByName = "productRequirementId")
    @Mapping(target = "cookingSchedule", source = "cookingSchedule", qualifiedByName = "cookingScheduleId")
    CookingIngredientDTO toDto(CookingIngredient s);

    @Named("productRequirementId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductRequirementDTO toDtoProductRequirementId(ProductRequirement productRequirement);

    @Named("cookingScheduleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CookingScheduleDTO toDtoCookingScheduleId(CookingSchedule cookingSchedule);
}
