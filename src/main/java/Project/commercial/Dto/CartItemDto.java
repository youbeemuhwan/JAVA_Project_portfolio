package Project.commercial.Dto;

import Project.commercial.domain.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CartItemDto {

    private Long id;

    private String itemName;

    private Color color;

    private Size size;

    private String price;

    private Integer quantity;

    private ThumbnailImage thumbnailImages;

    public CartItemDto(Long id, String itemName, Color color, Size size, String price, Integer quantity, ThumbnailImage thumbnailImages) {
        this.id = id;
        this.itemName = itemName;
        this.color = color;
        this.size = size;
        this.price = price;
        this.quantity = quantity;
        this.thumbnailImages = thumbnailImages;
    }
}
