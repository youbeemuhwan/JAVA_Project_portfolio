package Project.commercial.Dto;

import Project.commercial.domain.OrderStatus;
import Project.commercial.domain.PaymentMethod;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.sql.In;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class OrderCreateResponseDto {
    private Long order_id;
    private String order_number;
    private LocalDateTime created_at;
    private CartAndOrderItemDto item;
    private String address;
    private String total_price;
    private PaymentMethod paymentMethod;
    private OrderStatus orderStatus;


    public OrderCreateResponseDto(Long order_id, String order_number, LocalDateTime created_at, CartAndOrderItemDto item, String address, String total_price, PaymentMethod paymentMethod, OrderStatus orderStatus) {
        this.order_id = order_id;
        this.order_number = order_number;
        this.created_at = created_at;
        this.item = item;
        this.address = address;
        this.total_price = total_price;
        this.paymentMethod = paymentMethod;
        this.orderStatus = orderStatus;
    }
}
