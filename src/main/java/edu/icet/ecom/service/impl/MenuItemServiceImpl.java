package edu.icet.ecom.service.impl;

import edu.icet.ecom.dto.MenuItemDTO;
import edu.icet.ecom.entity.MenuItemEntity;
import edu.icet.ecom.repository.custom.MenuItemRepository;
import edu.icet.ecom.service.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuItemServiceImpl implements MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final ModelMapper modelMapper;

    @Override
    public MenuItemDTO createMenuItem(MenuItemDTO menuItemDTO) {
        MenuItemEntity menuItemEntity = modelMapper.map(menuItemDTO, MenuItemEntity.class);
        MenuItemEntity savedEntity = menuItemRepository.save(menuItemEntity);
        return modelMapper.map(savedEntity, MenuItemDTO.class);
    }

    @Override
    public MenuItemDTO getMenuItemById(Long id) {
        MenuItemEntity menuItemEntity = menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu item not found with id: " + id));
        return modelMapper.map(menuItemEntity, MenuItemDTO.class);
    }

    @Override
    public List<MenuItemDTO> getAllMenuItems() {
        List<MenuItemEntity> menuItemEntities = menuItemRepository.findAll();
        return menuItemEntities.stream()
                .map(entity -> modelMapper.map(entity, MenuItemDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<MenuItemDTO> getMenuItemsByCategory(String category) {
        List<MenuItemEntity> menuItemEntities = menuItemRepository.findByCategory(category);
        return menuItemEntities.stream()
                .map(entity -> modelMapper.map(entity, MenuItemDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<MenuItemDTO> getAvailableMenuItems() {
        List<MenuItemEntity> menuItemEntities = menuItemRepository.findByAvailableTrue();
        return menuItemEntities.stream()
                .map(entity -> modelMapper.map(entity, MenuItemDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public MenuItemDTO updateMenuItem(Long id, MenuItemDTO menuItemDTO) {
        MenuItemEntity existingEntity = menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu item not found with id: " + id));

        // Update fields
        existingEntity.setName(menuItemDTO.getName());
        existingEntity.setDescription(menuItemDTO.getDescription());
        existingEntity.setPrice(menuItemDTO.getPrice());
        existingEntity.setCategory(menuItemDTO.getCategory());
        existingEntity.setImage(menuItemDTO.getImage());
        existingEntity.setAvailable(menuItemDTO.getAvailable());
        existingEntity.setDietary(menuItemDTO.getDietary());
        existingEntity.setIngredients(menuItemDTO.getIngredients());

        MenuItemEntity updatedEntity = menuItemRepository.save(existingEntity);
        return modelMapper.map(updatedEntity, MenuItemDTO.class);
    }

    @Override
    public void deleteMenuItem(Long id) {
        if (!menuItemRepository.existsById(id)) {
            throw new RuntimeException("Menu item not found with id: " + id);
        }
        menuItemRepository.deleteById(id);
    }

    @Override
    public List<MenuItemDTO> searchMenuItemsByName(String name) {
        List<MenuItemEntity> menuItemEntities = menuItemRepository.findByNameContainingIgnoreCase(name);
        return menuItemEntities.stream()
                .map(entity -> modelMapper.map(entity, MenuItemDTO.class))
                .collect(Collectors.toList());
    }
}