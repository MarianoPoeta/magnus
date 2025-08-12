package com.magnus.service.impl;

import com.magnus.domain.ShoppingItem;
import com.magnus.repository.ShoppingItemRepository;
import com.magnus.service.ShoppingItemService;
import com.magnus.service.dto.ShoppingItemDTO;
import com.magnus.service.mapper.ShoppingItemMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.magnus.domain.ShoppingItem}.
 */
@Service
@Transactional
public class ShoppingItemServiceImpl implements ShoppingItemService {

    private static final Logger LOG = LoggerFactory.getLogger(ShoppingItemServiceImpl.class);

    private final ShoppingItemRepository shoppingItemRepository;

    private final ShoppingItemMapper shoppingItemMapper;

    public ShoppingItemServiceImpl(ShoppingItemRepository shoppingItemRepository, ShoppingItemMapper shoppingItemMapper) {
        this.shoppingItemRepository = shoppingItemRepository;
        this.shoppingItemMapper = shoppingItemMapper;
    }

    @Override
    public ShoppingItemDTO save(ShoppingItemDTO shoppingItemDTO) {
        LOG.debug("Request to save ShoppingItem : {}", shoppingItemDTO);
        ShoppingItem shoppingItem = shoppingItemMapper.toEntity(shoppingItemDTO);
        shoppingItem = shoppingItemRepository.save(shoppingItem);
        return shoppingItemMapper.toDto(shoppingItem);
    }

    @Override
    public ShoppingItemDTO update(ShoppingItemDTO shoppingItemDTO) {
        LOG.debug("Request to update ShoppingItem : {}", shoppingItemDTO);
        ShoppingItem shoppingItem = shoppingItemMapper.toEntity(shoppingItemDTO);
        shoppingItem = shoppingItemRepository.save(shoppingItem);
        return shoppingItemMapper.toDto(shoppingItem);
    }

    @Override
    public Optional<ShoppingItemDTO> partialUpdate(ShoppingItemDTO shoppingItemDTO) {
        LOG.debug("Request to partially update ShoppingItem : {}", shoppingItemDTO);

        return shoppingItemRepository
            .findById(shoppingItemDTO.getId())
            .map(existingShoppingItem -> {
                shoppingItemMapper.partialUpdate(existingShoppingItem, shoppingItemDTO);

                return existingShoppingItem;
            })
            .map(shoppingItemRepository::save)
            .map(shoppingItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ShoppingItemDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ShoppingItems");
        return shoppingItemRepository.findAll(pageable).map(shoppingItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ShoppingItemDTO> findOne(Long id) {
        LOG.debug("Request to get ShoppingItem : {}", id);
        return shoppingItemRepository.findById(id).map(shoppingItemMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ShoppingItem : {}", id);
        shoppingItemRepository.deleteById(id);
    }
}
