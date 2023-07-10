package Project.commercial.Dto;

import Project.commercial.domain.Member;
import Project.commercial.domain.OrderItem;
import Project.commercial.domain.OrderStatus;
import Project.commercial.domain.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.persistence.*;
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
    private String  paymentMethod;
    @Nullable
    private String orderStatus;

    public OrderListDto(Long id, String orderNumber, LocalDateTime created_at, List<CartAndOrderItemDto> orderItem, Integer totalPrice, @Nullable String paymentMethod, @Nullable String orderStatus) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.created_at = created_at;
        this.orderItem = orderItem;
        this.totalPrice = totalPrice;
        this.paymentMethod = paymentMethod;
        this.orderStatus = orderStatus;
    }
}
