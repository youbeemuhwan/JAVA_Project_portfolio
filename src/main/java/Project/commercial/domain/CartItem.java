package Project.commercial.domain;


import Project.commercial.dto.cart.UpdateCartItemDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(name = "cart_id", insertable = false, updatable = false)
    private Long cartId;
    @Builder
    public CartItem(Long id, Item item, Integer quantity, Cart cart, Long cartId) {
        this.id = id;
        this.item = item;
        this.quantity = quantity;
        this.cart = cart;
        this.cartId = cartId;
    }

    public void updateCartItem(UpdateCartItemDto updateCartItemDto){
        this.quantity = updateCartItemDto.getQuantity();


    }
}
