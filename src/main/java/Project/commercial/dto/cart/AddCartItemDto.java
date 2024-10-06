package Project.commercial.dto.cart;


import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AddCartItemDto {

    private Long itemId;

    private Integer quantity;
}
