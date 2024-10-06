package Project.commercial.dto.item;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateItemDto {
    private Long categoryId;

    private Long detailCategoryId;

    private String itemName;

    private String description;

    private Long colorId;

    private Long sizeId;

    private Integer price;

}
