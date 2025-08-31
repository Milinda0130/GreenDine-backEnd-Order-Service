package edu.icet.ecom.service;

import edu.icet.ecom.dto.OrderDTO;
import edu.icet.ecom.util.Status;
import edu.icet.ecom.util.Type;
import java.time.Instant;
import java.util.List;

public interface OrderService {
    OrderDTO createOrder(OrderDTO orderDTO);
    OrderDTO getOrderById(Long id);
    List<OrderDTO> getAllOrders();
    List<OrderDTO> getOrdersByCustomerId(Long customerId);
    List<OrderDTO> getOrdersByStatus(Status status);
    List<OrderDTO> getOrdersByType(Type type);
    List<OrderDTO> getOrdersByTableNumber(Integer tableNumber);
    List<OrderDTO> getOrdersByDateRange(Instant startDate, Instant endDate);
    OrderDTO updateOrder(Long id, OrderDTO orderDTO);
    OrderDTO updateOrderStatus(Long id, Status status);
    void deleteOrder(Long id);
    Double calculateOrderTotal(Long orderId);
    List<OrderDTO> getTodaysOrders();
    List<OrderDTO> getPendingOrders();
    Long getOrderCountByStatus(Status status);
    Double getTotalRevenueByDateRange(Instant startDate, Instant endDate);
}