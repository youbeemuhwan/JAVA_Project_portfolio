package Project.commercial.Dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class CartItemListDto {

    private List<CartAndOrderItemDto> CartItemList;

    private String total_price;

    public CartItemListDto(List<CartAndOrderItemDto> cartItemList, String total_price) {
        CartItemList = cartItemList;
        this.total_price = total_price;
    }
}

