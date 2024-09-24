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

    private LocalDateTime created_at;

    private List<CartAndOrderItemDto> orderItem;

    private Integer totalPrice;

    @Nullable
    private PaymentMethod  paymentMethod;

    @Nullable
    private OrderStatus orderStatus;

    public OrderListDto(Long id, String orderNumber, LocalDateTime created_at, List<CartAndOrderItemDto> orderItem, Integer totalPrice, @Nullable PaymentMethod paymentMethod, @Nullable OrderStatus orderStatus) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.created_at = created_at;
        this.orderItem = orderItem;
        this.totalPrice = totalPrice;
        this.paymentMethod = paymentMethod;
        this.orderStatus = orderStatus;
    }
}
