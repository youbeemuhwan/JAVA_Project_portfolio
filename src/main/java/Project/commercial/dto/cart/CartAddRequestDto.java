package Project.commercial.dto.cart;


import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CartAddRequestDto {

    private Long item_id;

    private Integer quantity;
}
