package edu.icet.ecom.service.impl;

import edu.icet.ecom.dto.OrderDTO;
import edu.icet.ecom.dto.OrderItemDTO;
import edu.icet.ecom.entity.OrderEntity;
import edu.icet.ecom.entity.OrderItemEntity;
import edu.icet.ecom.repository.custom.OrderRepository;
import edu.icet.ecom.service.OrderItemService;
import edu.icet.ecom.service.OrderService;
import edu.icet.ecom.util.Status;
import edu.icet.ecom.util.Type;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemService orderItemService;
    private final ModelMapper modelMapper;

    @Override
    public OrderDTO createOrder(OrderDTO orderDTO) {
        // Set timestamp if not provided
        if (orderDTO.getTimestamp() == null) {
            orderDTO.setTimestamp(Instant.now());
        }

        // Set default status if not provided
        if (orderDTO.getStatus() == null) {
            orderDTO.setStatus(Status.PENDING);
        }

        OrderEntity orderEntity = modelMapper.map(orderDTO, OrderEntity.class);

        // Handle order items before saving the order
        if (orderDTO.getOrderItems() != null && !orderDTO.getOrderItems().isEmpty()) {
            List<OrderItemEntity> orderItemEntities = orderDTO.getOrderItems().stream()
                    .map(itemDTO -> {
                        OrderItemEntity itemEntity = modelMapper.map(itemDTO, OrderItemEntity.class);
                        itemEntity.setOrder(orderEntity); // Set the parent OrderEntity
                        return itemEntity;
                    })
                    .collect(Collectors.toList());
            orderEntity.setOrderItems(orderItemEntities); // Set the list on the OrderEntity
        }

        OrderEntity savedEntity = orderRepository.save(orderEntity);

        // Calculate and update total (now that order items are saved via cascade)
        Double total = calculateOrderTotal(savedEntity.getId());
        savedEntity.setTotal(total);
        savedEntity = orderRepository.save(savedEntity); // Save again to update total

        // Retrieve order items from the saved entity (they should be loaded now)
        OrderDTO resultDTO = modelMapper.map(savedEntity, OrderDTO.class);
        // The orderItems should now be populated via the cascade and lazy loading (if accessed)
        // However, to ensure they are in the DTO, we can still explicitly set them if needed,
        // or rely on ModelMapper to map the now-populated collection.
        // Let's keep the explicit setting for now to be safe.
        resultDTO.setOrderItems(orderItemService.getOrderItemsByOrderId(savedEntity.getId()));
        return resultDTO;
    }

    @Override
    public OrderDTO getOrderById(Long id) {
        OrderEntity orderEntity = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        orderEntity.getOrderItems().size(); // Initialize the collection

        OrderDTO orderDTO = modelMapper.map(orderEntity, OrderDTO.class);
        orderDTO.setOrderItems(orderItemService.getOrderItemsByOrderId(id));
        return orderDTO;
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        List<OrderEntity> orderEntities = orderRepository.findAllByOrderByTimestampDesc();
        return orderEntities.stream()
                .map(entity -> {
                    entity.getOrderItems().size(); // Initialize the collection
                    OrderDTO dto = modelMapper.map(entity, OrderDTO.class);
                    dto.setOrderItems(orderItemService.getOrderItemsByOrderId(entity.getId()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getOrdersByCustomerId(Long customerId) {
        List<OrderEntity> orderEntities = orderRepository.findByCustomerIdOrderByTimestampDesc(customerId);
        return orderEntities.stream()
                .map(entity -> {
                    entity.getOrderItems().size(); // Initialize the collection
                    OrderDTO dto = modelMapper.map(entity, OrderDTO.class);
                    dto.setOrderItems(orderItemService.getOrderItemsByOrderId(entity.getId()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getOrdersByStatus(Status status) {
        List<OrderEntity> orderEntities = orderRepository.findByStatusOrderByTimestampDesc(status);
        return orderEntities.stream()
                .map(entity -> {
                    entity.getOrderItems().size(); // Initialize the collection
                    OrderDTO dto = modelMapper.map(entity, OrderDTO.class);
                    dto.setOrderItems(orderItemService.getOrderItemsByOrderId(entity.getId()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getOrdersByType(Type type) {
        List<OrderEntity> orderEntities = orderRepository.findByTypeOrderByTimestampDesc(type);
        return orderEntities.stream()
                .map(entity -> {
                    entity.getOrderItems().size(); // Initialize the collection
                    OrderDTO dto = modelMapper.map(entity, OrderDTO.class);
                    dto.setOrderItems(orderItemService.getOrderItemsByOrderId(entity.getId()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getOrdersByTableNumber(Integer tableNumber) {
        List<OrderEntity> orderEntities = orderRepository.findByTableNumberOrderByTimestampDesc(tableNumber);
        return orderEntities.stream()
                .map(entity -> {
                    entity.getOrderItems().size(); // Initialize the collection
                    OrderDTO dto = modelMapper.map(entity, OrderDTO.class);
                    dto.setOrderItems(orderItemService.getOrderItemsByOrderId(entity.getId()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getOrdersByDateRange(Instant startDate, Instant endDate) {
        List<OrderEntity> orderEntities = orderRepository.findByTimestampBetweenOrderByTimestampDesc(startDate, endDate);
        return orderEntities.stream()
                .map(entity -> {
                    entity.getOrderItems().size(); // Initialize the collection
                    OrderDTO dto = modelMapper.map(entity, OrderDTO.class);
                    dto.setOrderItems(orderItemService.getOrderItemsByOrderId(entity.getId()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO updateOrder(Long id, OrderDTO orderDTO) {
        OrderEntity existingEntity = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        existingEntity.getOrderItems().size(); // Initialize the collection

        existingEntity.setCustomerId(orderDTO.getCustomerId());
        existingEntity.setCustomerName(orderDTO.getCustomerName());
        existingEntity.setStatus(orderDTO.getStatus());
        existingEntity.setType(orderDTO.getType());
        existingEntity.setTableNumber(orderDTO.getTableNumber());
        existingEntity.setNotes(orderDTO.getNotes());

        OrderEntity updatedEntity = orderRepository.save(existingEntity);

        OrderDTO resultDTO = modelMapper.map(updatedEntity, OrderDTO.class);
        resultDTO.setOrderItems(orderItemService.getOrderItemsByOrderId(id));
        return resultDTO;
    }

    @Override
    public OrderDTO updateOrderStatus(Long id, Status status) {
        OrderEntity existingEntity = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        existingEntity.getOrderItems().size(); // Initialize the collection

        existingEntity.setStatus(status);
        OrderEntity updatedEntity = orderRepository.save(existingEntity);

        OrderDTO resultDTO = modelMapper.map(updatedEntity, OrderDTO.class);
        resultDTO.setOrderItems(orderItemService.getOrderItemsByOrderId(id));
        return resultDTO;
    }

    @Override
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("Order not found with id: " + id);
        }

        // Delete associated order items first
        orderItemService.deleteOrderItemsByOrderId(id);
        orderRepository.deleteById(id);
    }

    @Override
    public Double calculateOrderTotal(Long orderId) {
        List<OrderItemDTO> orderItems = orderItemService.getOrderItemsByOrderId(orderId);
        return orderItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    @Override
    public List<OrderDTO> getTodaysOrders() {
        LocalDate today = LocalDate.now();
        Instant startOfDay = today.atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant endOfDay = today.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC);

        return getOrdersByDateRange(startOfDay, endOfDay);
    }

    @Override
    public List<OrderDTO> getPendingOrders() {
        return getOrdersByStatus(Status.PENDING);
    }

    @Override
    public Long getOrderCountByStatus(Status status) {
        return orderRepository.countByStatus(status);
    }

    @Override
    public Double getTotalRevenueByDateRange(Instant startDate, Instant endDate) {
        return orderRepository.getTotalRevenueByDateRange(startDate, endDate);
    }
}