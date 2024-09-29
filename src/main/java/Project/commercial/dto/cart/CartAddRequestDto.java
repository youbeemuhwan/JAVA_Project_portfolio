package Project.commercial.dto.cart;


import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CartAddRequestDto {

    private Long itemId;

    private Integer quantity;
}
