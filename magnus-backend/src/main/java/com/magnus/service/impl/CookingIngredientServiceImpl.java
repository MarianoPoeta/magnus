package com.magnus.service.impl;

import com.magnus.domain.CookingIngredient;
import com.magnus.repository.CookingIngredientRepository;
import com.magnus.service.CookingIngredientService;
import com.magnus.service.dto.CookingIngredientDTO;
import com.magnus.service.mapper.CookingIngredientMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.magnus.domain.CookingIngredient}.
 */
@Service
@Transactional
public class CookingIngredientServiceImpl implements CookingIngredientService {

    private static final Logger LOG = LoggerFactory.getLogger(CookingIngredientServiceImpl.class);

    private final CookingIngredientRepository cookingIngredientRepository;

    private final CookingIngredientMapper cookingIngredientMapper;

    public CookingIngredientServiceImpl(
        CookingIngredientRepository cookingIngredientRepository,
        CookingIngredientMapper cookingIngredientMapper
    ) {
        this.cookingIngredientRepository = cookingIngredientRepository;
        this.cookingIngredientMapper = cookingIngredientMapper;
    }

    @Override
    public CookingIngredientDTO save(CookingIngredientDTO cookingIngredientDTO) {
        LOG.debug("Request to save CookingIngredient : {}", cookingIngredientDTO);
        CookingIngredient cookingIngredient = cookingIngredientMapper.toEntity(cookingIngredientDTO);
        cookingIngredient = cookingIngredientRepository.save(cookingIngredient);
        return cookingIngredientMapper.toDto(cookingIngredient);
    }

    @Override
    public CookingIngredientDTO update(CookingIngredientDTO cookingIngredientDTO) {
        LOG.debug("Request to update CookingIngredient : {}", cookingIngredientDTO);
        CookingIngredient cookingIngredient = cookingIngredientMapper.toEntity(cookingIngredientDTO);
        cookingIngredient = cookingIngredientRepository.save(cookingIngredient);
        return cookingIngredientMapper.toDto(cookingIngredient);
    }

    @Override
    public Optional<CookingIngredientDTO> partialUpdate(CookingIngredientDTO cookingIngredientDTO) {
        LOG.debug("Request to partially update CookingIngredient : {}", cookingIngredientDTO);

        return cookingIngredientRepository
            .findById(cookingIngredientDTO.getId())
            .map(existingCookingIngredient -> {
                cookingIngredientMapper.partialUpdate(existingCookingIngredient, cookingIngredientDTO);

                return existingCookingIngredient;
            })
            .map(cookingIngredientRepository::save)
            .map(cookingIngredientMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CookingIngredientDTO> findAll() {
        LOG.debug("Request to get all CookingIngredients");
        return cookingIngredientRepository
            .findAll()
            .stream()
            .map(cookingIngredientMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CookingIngredientDTO> findOne(Long id) {
        LOG.debug("Request to get CookingIngredient : {}", id);
        return cookingIngredientRepository.findById(id).map(cookingIngredientMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete CookingIngredient : {}", id);
        cookingIngredientRepository.deleteById(id);
    }
}
