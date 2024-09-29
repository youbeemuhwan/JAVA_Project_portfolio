package Project.commercial.dto.cart;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class CartItemListDto {

    private List<CartAndOrderItemDto> cartItemList;

    private String totalPrice;

    public CartItemListDto(List<CartAndOrderItemDto> cartItemList, String totalPrice) {
        this.cartItemList = cartItemList;
        this.totalPrice = totalPrice;
    }
}

