package Project.commercial.Dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ItemCreateRequestDto {

    private Long category_id;

    private Long detailCategory_id;

    private String itemName;

    private String description;

    private Long color_id;

    private Long size_id;

    private Integer price;

    public ItemCreateRequestDto() {
    }

    public ItemCreateRequestDto(Long category_id, Long detailCategory_id, String itemName, String description, Long color_id, Long size_id, Integer price) {
        this.category_id = category_id;
        this.detailCategory_id = detailCategory_id;
        this.itemName = itemName;
        this.description = description;
        this.color_id = color_id;
        this.size_id = size_id;
        this.price = price;
    }
}
