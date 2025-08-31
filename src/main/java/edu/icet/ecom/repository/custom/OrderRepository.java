package edu.icet.ecom.repository.custom;

import edu.icet.ecom.entity.OrderEntity;
import edu.icet.ecom.util.Status;
import edu.icet.ecom.util.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findAllByOrderByTimestampDesc();
    List<OrderEntity> findByCustomerIdOrderByTimestampDesc(Long customerId);
    List<OrderEntity> findByStatusOrderByTimestampDesc(Status status);
    List<OrderEntity> findByTypeOrderByTimestampDesc(Type type);
    List<OrderEntity> findByTableNumberOrderByTimestampDesc(Integer tableNumber);
    List<OrderEntity> findByTimestampBetweenOrderByTimestampDesc(Instant startDate, Instant endDate);
    Long countByStatus(Status status);

    @Query("SELECT SUM(o.total) FROM OrderEntity o WHERE o.timestamp BETWEEN :startDate AND :endDate AND o.status != 'CANCELLED'")
    Double getTotalRevenueByDateRange(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);
}