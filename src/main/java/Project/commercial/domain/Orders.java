package Project.commercial.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderNumber;

    private LocalDateTime created_at;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "orders", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItem;

    private String address;

    private Integer totalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_method_id")

    private PaymentMethod paymentMethod;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_status_id")

    private OrderStatus orderStatus;


    @Builder
    public Orders(String orderNumber, LocalDateTime created_at, Member member, List<OrderItem> orderItem, String address, Integer totalPrice, PaymentMethod paymentMethod, OrderStatus orderStatus) {
        this.orderNumber = orderNumber;
        this.created_at = created_at;
        this.member = member;
        this.orderItem = orderItem;
        this.address = address;
        this.totalPrice = totalPrice;
        this.paymentMethod = paymentMethod;
        this.orderStatus = orderStatus;
    }

    public void updateOrderStatus(OrderStatus orderStatus){
        this.orderStatus = orderStatus;
    }

    public void updateTotalPrice(Integer totalPrice){
        this.totalPrice = totalPrice;
    }
}
