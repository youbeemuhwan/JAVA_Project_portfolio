package Project.commercial.dto.order;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CreateOrderDto {

    private String orderNumber;

    private Long itemId;

    private Integer quantity;

    private String address;

    private Long paymentMethodId;

    private Long orderStatusId;

    private LocalDateTime createdAt;

    @Builder
    public CreateOrderDto(String orderNumber, Long itemId, Integer quantity, String address, Long paymentMethodId, Long orderStatusId, LocalDateTime createdAt) {
        this.orderNumber = orderNumber;
        this.itemId = itemId;
        this.quantity = quantity;
        this.address = address;
        this.paymentMethodId = paymentMethodId;
        this.orderStatusId = orderStatusId;
        this.createdAt = createdAt;
    }
}


