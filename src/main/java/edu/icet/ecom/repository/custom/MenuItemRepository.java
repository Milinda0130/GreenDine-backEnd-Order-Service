package edu.icet.ecom.repository.custom;

import edu.icet.ecom.entity.MenuItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItemEntity, Long> {
    List<MenuItemEntity> findByCategory(String category);
    List<MenuItemEntity> findByAvailableTrue();
    List<MenuItemEntity> findByNameContainingIgnoreCase(String name);
}