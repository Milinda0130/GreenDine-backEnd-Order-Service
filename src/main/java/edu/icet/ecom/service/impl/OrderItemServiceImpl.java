package edu.icet.ecom.service.impl;

import edu.icet.ecom.dto.OrderItemDTO;
import edu.icet.ecom.entity.OrderItemEntity;
import edu.icet.ecom.repository.custom.OrderItemRepository;
import edu.icet.ecom.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final ModelMapper modelMapper;

    @Override
    public OrderItemDTO createOrderItem(OrderItemDTO orderItemDTO) {
        // Ensure ID is null for new entities to allow database to generate it
        orderItemDTO.setId(null);

        OrderItemEntity orderItemEntity = modelMapper.map(orderItemDTO, OrderItemEntity.class);
        OrderItemEntity savedEntity = orderItemRepository.save(orderItemEntity);
        return modelMapper.map(savedEntity, OrderItemDTO.class);
    }

    @Override
    public OrderItemDTO getOrderItemById(Long id) {
        OrderItemEntity orderItemEntity = orderItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order item not found with id: " + id));
        return modelMapper.map(orderItemEntity, OrderItemDTO.class);
    }

    @Override
    public List<OrderItemDTO> getAllOrderItems() {
        List<OrderItemEntity> orderItemEntities = orderItemRepository.findAll();
        return orderItemEntities.stream()
                .map(entity -> modelMapper.map(entity, OrderItemDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderItemDTO> getOrderItemsByOrderId(Long orderId) {
        List<OrderItemEntity> orderItemEntities = orderItemRepository.findByOrderId(orderId);
        return orderItemEntities.stream()
                .map(entity -> modelMapper.map(entity, OrderItemDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public OrderItemDTO updateOrderItem(Long id, OrderItemDTO orderItemDTO) {
        OrderItemEntity existingEntity = orderItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order item not found with id: " + id));

        existingEntity.setMenuItemId(orderItemDTO.getMenuItemId());
        existingEntity.setName(orderItemDTO.getName());
        existingEntity.setImage(orderItemDTO.getImage());
        existingEntity.setPrice(orderItemDTO.getPrice());
        existingEntity.setQuantity(orderItemDTO.getQuantity());

        OrderItemEntity updatedEntity = orderItemRepository.save(existingEntity);
        return modelMapper.map(updatedEntity, OrderItemDTO.class);
    }

    @Override
    public void deleteOrderItem(Long id) {
        if (!orderItemRepository.existsById(id)) {
            throw new RuntimeException("Order item not found with id: " + id);
        }
        orderItemRepository.deleteById(id);
    }

    @Override
    public void deleteOrderItemsByOrderId(Long orderId) {
        orderItemRepository.deleteByOrderId(orderId);
    }
}