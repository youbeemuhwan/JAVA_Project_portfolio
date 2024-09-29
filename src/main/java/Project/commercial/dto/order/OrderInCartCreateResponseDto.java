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

    private Long orderId;
    private String orderNumber;
    private LocalDateTime createdAt;
    private List<CartAndOrderItemDto> itemList;
    private String address;
    private String totalPrice;
    private PaymentMethod paymentMethod;
    private OrderStatus orderStatus;

    public OrderInCartCreateResponseDto(Long orderId, String order_number, LocalDateTime createdAt, List<CartAndOrderItemDto> itemList, String address, String totalPrice, PaymentMethod paymentMethod, OrderStatus orderStatus) {
        this.orderId = orderId;
        this.orderNumber = order_number;
        this.createdAt = createdAt;
        this.itemList = itemList;
        this.address = address;
        this.totalPrice = totalPrice;
        this.paymentMethod = paymentMethod;
        this.orderStatus = orderStatus;
    }
}
