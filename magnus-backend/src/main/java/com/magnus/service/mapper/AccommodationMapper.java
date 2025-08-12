package com.magnus.service.mapper;

import com.magnus.domain.Accommodation;
import com.magnus.service.dto.AccommodationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Accommodation} and its DTO {@link AccommodationDTO}.
 */
@Mapper(componentModel = "spring")
public interface AccommodationMapper extends EntityMapper<AccommodationDTO, Accommodation> {}
