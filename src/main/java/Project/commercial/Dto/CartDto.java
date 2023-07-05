package Project.commercial.Dto;

import Project.commercial.domain.CartItem;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class CartDto {

    private Long cart_id;
    private List<CartItem> cartItemList;

}
