package com.magnus.service.mapper;

import com.magnus.domain.AppUser;
import com.magnus.domain.Product;
import com.magnus.service.dto.AppUserDTO;
import com.magnus.service.dto.ProductDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "appUserId")
    ProductDTO toDto(Product s);

    @Named("appUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AppUserDTO toDtoAppUserId(AppUser appUser);
}
