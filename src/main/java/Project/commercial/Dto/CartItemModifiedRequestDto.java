package Project.commercial.Dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class CartItemModifiedRequestDto {

    private Long cart_item_id;

    private Integer quantity;



    public CartItemModifiedRequestDto(Long cart_item_id, Integer quantity) {
        this.cart_item_id = cart_item_id;
        this.quantity = quantity;
    }
}
