package Project.commercial.Dto;

import Project.commercial.domain.Cart;
import Project.commercial.domain.CartItem;
import Project.commercial.domain.Item;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CartItemDto {

    private Cart cart;

    private Item item;

    private Integer quantity;

    public CartItem toEntity(CartItemDto cartItemDto){
        return CartItem.builder()
                .item(cartItemDto.getItem())
                .cart(cartItemDto.getCart())
                .quantity(cartItemDto.getQuantity())
                .build();
    }

}
