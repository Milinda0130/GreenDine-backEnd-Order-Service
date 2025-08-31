package edu.icet.ecom.dto;

import edu.icet.ecom.util.Status;
import edu.icet.ecom.util.Type;
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
public class OrderDTO {
        private Long id;
        private Long customerId;
        private String customerName;
        private Double total;
        private Status status;
        private Instant timestamp;
        private Type type;
        private Integer tableNumber;
        private String notes;
        private List<OrderItemDTO> orderItems;
}