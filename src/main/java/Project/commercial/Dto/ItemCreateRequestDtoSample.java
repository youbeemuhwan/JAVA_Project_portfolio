package Project.commercial.Dto;

import Project.commercial.domain.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ItemCreateRequestDtoSample {

    private Category category;

    private DetailCategory detailCategory;

    private String itemName;

    private String description;

    private Color color;

    private Size size;

    private Integer price;

    public ItemCreateRequestDtoSample() {
    }

    public ItemCreateRequestDtoSample(Category category, DetailCategory detailCategory, String itemName, String description, Color color, Size size, Integer price) {
        this.category = category;
        this.detailCategory = detailCategory;
        this.itemName = itemName;
        this.description = description;
        this.color = color;
        this.size = size;
        this.price = price;
    }

    public Item toEntity(ItemCreateRequestDtoSample itemCreateRequestDtoSample){
        return Item.builder()
                .category(itemCreateRequestDtoSample.getCategory())
                .detailCategory(itemCreateRequestDtoSample.getDetailCategory())
                .itemName(itemCreateRequestDtoSample.getItemName())
                .description(itemCreateRequestDtoSample.getDescription())
                .color(itemCreateRequestDtoSample.getColor())
                .size(itemCreateRequestDtoSample.getSize())
                .price(itemCreateRequestDtoSample.getPrice())
                .build();

    }



}
