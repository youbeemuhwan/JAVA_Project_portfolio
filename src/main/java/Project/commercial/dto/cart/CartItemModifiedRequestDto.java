package Project.commercial.dto.cart;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CartItemModifiedRequestDto {

    private Long cart_item_id;

    private Integer quantity;

    @Builder
    public CartItemModifiedRequestDto(Long cart_item_id, Integer quantity) {
        this.cart_item_id = cart_item_id;
        this.quantity = quantity;
    }
}
