package com.magnus.service.impl;

import com.magnus.domain.MenuItem;
import com.magnus.repository.MenuItemRepository;
import com.magnus.service.MenuItemService;
import com.magnus.service.dto.MenuItemDTO;
import com.magnus.service.mapper.MenuItemMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.magnus.domain.MenuItem}.
 */
@Service
@Transactional
public class MenuItemServiceImpl implements MenuItemService {

    private static final Logger LOG = LoggerFactory.getLogger(MenuItemServiceImpl.class);

    private final MenuItemRepository menuItemRepository;

    private final MenuItemMapper menuItemMapper;

    public MenuItemServiceImpl(MenuItemRepository menuItemRepository, MenuItemMapper menuItemMapper) {
        this.menuItemRepository = menuItemRepository;
        this.menuItemMapper = menuItemMapper;
    }

    @Override
    public MenuItemDTO save(MenuItemDTO menuItemDTO) {
        LOG.debug("Request to save MenuItem : {}", menuItemDTO);
        MenuItem menuItem = menuItemMapper.toEntity(menuItemDTO);
        menuItem = menuItemRepository.save(menuItem);
        return menuItemMapper.toDto(menuItem);
    }

    @Override
    public MenuItemDTO update(MenuItemDTO menuItemDTO) {
        LOG.debug("Request to update MenuItem : {}", menuItemDTO);
        MenuItem menuItem = menuItemMapper.toEntity(menuItemDTO);
        menuItem = menuItemRepository.save(menuItem);
        return menuItemMapper.toDto(menuItem);
    }

    @Override
    public Optional<MenuItemDTO> partialUpdate(MenuItemDTO menuItemDTO) {
        LOG.debug("Request to partially update MenuItem : {}", menuItemDTO);

        return menuItemRepository
            .findById(menuItemDTO.getId())
            .map(existingMenuItem -> {
                menuItemMapper.partialUpdate(existingMenuItem, menuItemDTO);

                return existingMenuItem;
            })
            .map(menuItemRepository::save)
            .map(menuItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuItemDTO> findAll() {
        LOG.debug("Request to get all MenuItems");
        return menuItemRepository.findAll().stream().map(menuItemMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MenuItemDTO> findOne(Long id) {
        LOG.debug("Request to get MenuItem : {}", id);
        return menuItemRepository.findById(id).map(menuItemMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete MenuItem : {}", id);
        menuItemRepository.deleteById(id);
    }
}
