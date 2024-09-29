package Project.commercial.dto.cart;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CartItemModifiedRequestDto {

    private Long cartItemId;

    private Integer quantity;

    @Builder
    public CartItemModifiedRequestDto(Long cartItemId, Integer quantity) {
        this.cartItemId = cartItemId;
        this.quantity = quantity;
    }
}
