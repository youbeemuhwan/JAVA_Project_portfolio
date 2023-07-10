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
public class CartItemCreateRequestDto {

    private Cart cart;

    private Item item;

    private Integer quantity;
    public CartItem toEntity(CartItemCreateRequestDto cartItemCreateRequestDto){
        return CartItem.builder()
                .item(cartItemCreateRequestDto.getItem())
                .cart(cartItemCreateRequestDto.getCart())
                .quantity(cartItemCreateRequestDto.getQuantity())
                .build();
    }

}
