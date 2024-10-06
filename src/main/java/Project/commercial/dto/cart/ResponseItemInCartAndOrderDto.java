package Project.commercial.dto.cart;

import Project.commercial.domain.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ResponseItemInCartAndOrderDto {

    private Long itemId;

    private String itemName;

    private Color color;

    private Size size;

    private String price;

    private Integer quantity;

    private ThumbnailImage thumbnailImages;



    public ResponseItemInCartAndOrderDto(Long itemId, String itemName, Color color, Size size, String price, Integer quantity, ThumbnailImage thumbnailImages) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.color = color;
        this.size = size;
        this.price = price;
        this.quantity = quantity;
        this.thumbnailImages = thumbnailImages;
    }


}
