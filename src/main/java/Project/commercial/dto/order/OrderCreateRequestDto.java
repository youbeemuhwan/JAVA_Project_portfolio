package Project.commercial.dto.order;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class OrderCreateRequestDto {

    private String orderNumber;

    private Long Item_id;

    private Integer quantity;

    private String address;

    private Long paymentMethod_id;

    private Long orderStatus_id;

    private LocalDateTime created_at;

    @Builder
    public OrderCreateRequestDto(String orderNumber, Long item_id, Integer quantity, String address, Long paymentMethod_id, Long orderStatus_id, LocalDateTime created_at) {
        this.orderNumber = orderNumber;
        Item_id = item_id;
        this.quantity = quantity;
        this.address = address;
        this.paymentMethod_id = paymentMethod_id;
        this.orderStatus_id = orderStatus_id;
        this.created_at = created_at;
    }

}
