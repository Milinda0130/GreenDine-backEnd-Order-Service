package edu.icet.ecom.service;

import edu.icet.ecom.dto.MenuItemDTO;
import java.util.List;

public interface MenuItemService {
    MenuItemDTO createMenuItem(MenuItemDTO menuItemDTO);
    MenuItemDTO getMenuItemById(Long id);
    List<MenuItemDTO> getAllMenuItems();
    List<MenuItemDTO> getMenuItemsByCategory(String category);
    List<MenuItemDTO> getAvailableMenuItems();
    MenuItemDTO updateMenuItem(Long id, MenuItemDTO menuItemDTO);
    void deleteMenuItem(Long id);
    List<MenuItemDTO> searchMenuItemsByName(String name);
}