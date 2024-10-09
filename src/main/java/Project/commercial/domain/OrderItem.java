package Project.commercial.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity
@Getter
@NoArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders_id")
    @JsonIgnore
    private Order order;

    @Column(name = "order_id", insertable = false, updatable = false)
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(name = "item_id", insertable = false, updatable = false)
    private Long itemId;

    @Min(1)
    @Max(99)
    private Integer quantity;

    @Builder
    public OrderItem(Long id, Order order, Long orderId, Item item, Long itemId, Integer quantity) {
        this.id = id;
        this.order = order;
        this.orderId = orderId;
        this.item = item;
        this.itemId = itemId;
        this.quantity = quantity;
    }
}
