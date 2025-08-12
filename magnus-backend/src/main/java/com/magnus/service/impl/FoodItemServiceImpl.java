package com.magnus.service.impl;

import com.magnus.domain.FoodItem;
import com.magnus.repository.FoodItemRepository;
import com.magnus.service.FoodItemService;
import com.magnus.service.dto.FoodItemDTO;
import com.magnus.service.mapper.FoodItemMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.magnus.domain.FoodItem}.
 */
@Service
@Transactional
public class FoodItemServiceImpl implements FoodItemService {

    private static final Logger LOG = LoggerFactory.getLogger(FoodItemServiceImpl.class);

    private final FoodItemRepository foodItemRepository;

    private final FoodItemMapper foodItemMapper;

    public FoodItemServiceImpl(FoodItemRepository foodItemRepository, FoodItemMapper foodItemMapper) {
        this.foodItemRepository = foodItemRepository;
        this.foodItemMapper = foodItemMapper;
    }

    @Override
    public FoodItemDTO save(FoodItemDTO foodItemDTO) {
        LOG.debug("Request to save FoodItem : {}", foodItemDTO);
        FoodItem foodItem = foodItemMapper.toEntity(foodItemDTO);
        foodItem = foodItemRepository.save(foodItem);
        return foodItemMapper.toDto(foodItem);
    }

    @Override
    public FoodItemDTO update(FoodItemDTO foodItemDTO) {
        LOG.debug("Request to update FoodItem : {}", foodItemDTO);
        FoodItem foodItem = foodItemMapper.toEntity(foodItemDTO);
        foodItem = foodItemRepository.save(foodItem);
        return foodItemMapper.toDto(foodItem);
    }

    @Override
    public Optional<FoodItemDTO> partialUpdate(FoodItemDTO foodItemDTO) {
        LOG.debug("Request to partially update FoodItem : {}", foodItemDTO);

        return foodItemRepository
            .findById(foodItemDTO.getId())
            .map(existingFoodItem -> {
                foodItemMapper.partialUpdate(existingFoodItem, foodItemDTO);

                return existingFoodItem;
            })
            .map(foodItemRepository::save)
            .map(foodItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FoodItemDTO> findAll() {
        LOG.debug("Request to get all FoodItems");
        return foodItemRepository.findAll().stream().map(foodItemMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FoodItemDTO> findOne(Long id) {
        LOG.debug("Request to get FoodItem : {}", id);
        return foodItemRepository.findById(id).map(foodItemMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete FoodItem : {}", id);
        foodItemRepository.deleteById(id);
    }
}
