package Project.commercial.dto.order;

import Project.commercial.domain.OrderStatus;
import Project.commercial.domain.PaymentMethod;
import Project.commercial.dto.cart.CartAndOrderItemDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Builder
public class OrderInCartCreateResponseDto {

    private Long order_id;
    private String order_number;
    private LocalDateTime created_at;
    private List<CartAndOrderItemDto> item_list;
    private String address;
    private String total_price;
    private PaymentMethod paymentMethod;
    private OrderStatus orderStatus;

    public OrderInCartCreateResponseDto(Long order_id, String order_number, LocalDateTime created_at, List<CartAndOrderItemDto> item_list, String address, String total_price, PaymentMethod paymentMethod, OrderStatus orderStatus) {
        this.order_id = order_id;
        this.order_number = order_number;
        this.created_at = created_at;
        this.item_list = item_list;
        this.address = address;
        this.total_price = total_price;
        this.paymentMethod = paymentMethod;
        this.orderStatus = orderStatus;
    }
}
