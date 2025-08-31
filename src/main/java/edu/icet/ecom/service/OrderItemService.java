package edu.icet.ecom.service;

import edu.icet.ecom.dto.OrderItemDTO;
import java.util.List;

public interface OrderItemService {
    OrderItemDTO createOrderItem(OrderItemDTO orderItemDTO);
    OrderItemDTO getOrderItemById(Long id);
    List<OrderItemDTO> getAllOrderItems();
    List<OrderItemDTO> getOrderItemsByOrderId(Long orderId);
    OrderItemDTO updateOrderItem(Long id, OrderItemDTO orderItemDTO);
    void deleteOrderItem(Long id);
    void deleteOrderItemsByOrderId(Long orderId);
}