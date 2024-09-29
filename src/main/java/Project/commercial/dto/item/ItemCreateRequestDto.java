package Project.commercial.dto.item;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ItemCreateRequestDto {

    private Long categoryId;

    private Long detailCategoryId;

    private String itemName;

    private String description;

    private Long colorId;

    private Long sizeId;

    private Integer price;

    @Builder
    public ItemCreateRequestDto(Long categoryId, Long detailCategoryId, String itemName, String description, Long colorId, Long sizeId, Integer price) {
        this.categoryId = categoryId;
        this.detailCategoryId = detailCategoryId;
        this.itemName = itemName;
        this.description = description;
        this.colorId = colorId;
        this.sizeId = sizeId;
        this.price = price;
    }
}
