package Project.commercial.dto.cart;

import Project.commercial.domain.Cart;
import Project.commercial.domain.CartItem;
import Project.commercial.domain.Item;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateCartItemDto {

    private Cart cart;

    private Item item;

    private Integer quantity;
    public CartItem toEntity(CreateCartItemDto createCartItemDto){
        return CartItem.builder()
                .item(createCartItemDto.getItem())
                .cart(createCartItemDto.getCart())
                .quantity(createCartItemDto.getQuantity())
                .build();
    }

}
