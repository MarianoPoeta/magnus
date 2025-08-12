package com.magnus.service.mapper;

import com.magnus.domain.Activity;
import com.magnus.domain.FoodItem;
import com.magnus.domain.Product;
import com.magnus.domain.ProductRequirement;
import com.magnus.domain.Task;
import com.magnus.service.dto.ActivityDTO;
import com.magnus.service.dto.FoodItemDTO;
import com.magnus.service.dto.ProductDTO;
import com.magnus.service.dto.ProductRequirementDTO;
import com.magnus.service.dto.TaskDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductRequirement} and its DTO {@link ProductRequirementDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductRequirementMapper extends EntityMapper<ProductRequirementDTO, ProductRequirement> {
    @Mapping(target = "product", source = "product", qualifiedByName = "productId")
    @Mapping(target = "relatedTask", source = "relatedTask", qualifiedByName = "taskId")
    @Mapping(target = "foodItem", source = "foodItem", qualifiedByName = "foodItemId")
    @Mapping(target = "activity", source = "activity", qualifiedByName = "activityId")
    ProductRequirementDTO toDto(ProductRequirement s);

    @Named("productId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductDTO toDtoProductId(Product product);

    @Named("taskId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TaskDTO toDtoTaskId(Task task);

    @Named("foodItemId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FoodItemDTO toDtoFoodItemId(FoodItem foodItem);

    @Named("activityId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ActivityDTO toDtoActivityId(Activity activity);
}
