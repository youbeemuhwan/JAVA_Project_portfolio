package Project.commercial.dto.order;

import Project.commercial.domain.OrderStatus;
import Project.commercial.domain.PaymentMethod;
import Project.commercial.dto.cart.CartAndOrderItemDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class OrderListDto {

    private Long id;

    private String orderNumber;

    private LocalDateTime createdAt;

    private List<CartAndOrderItemDto> orderItem;

    private Integer totalPrice;

    @Nullable
    private PaymentMethod  paymentMethod;

    @Nullable
    private OrderStatus orderStatus;

    public OrderListDto(Long id, String orderNumber, LocalDateTime createdAt, List<CartAndOrderItemDto> orderItem, Integer totalPrice, @Nullable PaymentMethod paymentMethod, @Nullable OrderStatus orderStatus) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.createdAt = createdAt;
        this.orderItem = orderItem;
        this.totalPrice = totalPrice;
        this.paymentMethod = paymentMethod;
        this.orderStatus = orderStatus;
    }
}
