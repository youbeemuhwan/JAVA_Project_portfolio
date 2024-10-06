package Project.commercial.dto.cart;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateCartItemDto {

    private Long cartItemId;

    private Integer quantity;

    @Builder
    public UpdateCartItemDto(Long cartItemId, Integer quantity) {
        this.cartItemId = cartItemId;
        this.quantity = quantity;
    }
}
