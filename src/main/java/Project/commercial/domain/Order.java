package Project.commercial.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderNumber;

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "member_id", insertable = false, updatable = false)
    private Long memberId;

    @OneToMany(mappedBy = "orders", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 5)
    private List<OrderItem> orderItem;

    private String address;

    private Integer totalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_method_id")
    private PaymentMethod paymentMethod;

    @Column(name = "payment_method_id", insertable = false, updatable = false)
    private Long paymentMethodId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_status_id")
    private OrderStatus orderStatus;

    @Column(name = "order_status_id", insertable = false, updatable = false)
    private Long orderStatusId;

    @Builder
    public Order(Long id, String orderNumber, LocalDateTime createdAt, Member member, Long memberId, List<OrderItem> orderItem, String address, Integer totalPrice, PaymentMethod paymentMethod, Long paymentMethodId, OrderStatus orderStatus, Long orderStatusId) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.createdAt = createdAt;
        this.member = member;
        this.memberId = memberId;
        this.orderItem = orderItem;
        this.address = address;
        this.totalPrice = totalPrice;
        this.paymentMethod = paymentMethod;
        this.paymentMethodId = paymentMethodId;
        this.orderStatus = orderStatus;
        this.orderStatusId = orderStatusId;
    }


    public void updateOrderStatus(Long orderStatusId){
        this.orderStatusId = orderStatusId;
    }

    public void updateTotalPrice(Integer totalPrice){
        this.totalPrice = totalPrice;
    }
}
