package Project.commercial.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.sql.In;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", updatable = false)
    @NotNull
    private Item item;

    @Min(1)
    @Max(99)
    @NotNull
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", updatable = false)
    @NotNull
    @JsonIgnore
    private Cart cart;

    @Builder
    public CartItem(Item item, Integer quantity, Cart cart) {
        this.item = item;
        this.quantity = quantity;
        this.cart = cart;
    }
}
