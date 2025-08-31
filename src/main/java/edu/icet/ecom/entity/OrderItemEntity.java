package edu.icet.ecom.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order_items")
public class OrderItemEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "order_id")
        private Long orderId;

        @Column(name = "menu_item_id")
        private Long menuItemId;

        private String name;
        private String image;
        private Double price;
        private Integer quantity;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "order_id", insertable = false, updatable = false)
        private OrderEntity order;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "menu_item_id", insertable = false, updatable = false)
        private MenuItemEntity menuItem;
}