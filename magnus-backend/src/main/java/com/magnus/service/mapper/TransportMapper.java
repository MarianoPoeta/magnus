package com.magnus.service.mapper;

import com.magnus.domain.Transport;
import com.magnus.service.dto.TransportDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Transport} and its DTO {@link TransportDTO}.
 */
@Mapper(componentModel = "spring")
public interface TransportMapper extends EntityMapper<TransportDTO, Transport> {}
