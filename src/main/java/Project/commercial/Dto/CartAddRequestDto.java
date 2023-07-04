package Project.commercial.Dto;


import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CartAddRequestDto {

    private Long item_id;

    private Integer quantity;


//    public Cart toCartEntity(CartAddRequestDto cartAddRequestDto){
//        return Cart.builder()
//                .member(cartAddRequestDto.getMember())
//                .build();
//    }








}
