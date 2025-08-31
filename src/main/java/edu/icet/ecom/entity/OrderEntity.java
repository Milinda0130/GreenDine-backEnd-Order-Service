package edu.icet.ecom.entity;

import edu.icet.ecom.util.Status;
import edu.icet.ecom.util.Type;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "customer_name")
    private String customerName;

    private Double total;

    @Enumerated(EnumType.STRING)
    private Status status;

    private Instant timestamp;

    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(name = "table_number")
    private Integer tableNumber;

    @Column(length = 2000)
    private String notes;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItemEntity> orderItems;
}