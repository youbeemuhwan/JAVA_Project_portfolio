package Project.commercial.dto.item;

import Project.commercial.domain.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


import java.util.List;

@Getter
@Setter
@Builder
public class ItemModifiedResponseDto {

    private Long id;

    private Category category;

    private DetailCategory detailCategory;

    private String itemName;

    private String description;

    private Color color;

    private Size size;

    private Integer price;

    private List<DetailImage> detailImages;

    private ThumbnailImage thumbnailImage;

    public ItemModifiedResponseDto(Long id, Category category, DetailCategory detailCategory, String itemName, String description, Color color, Size size, Integer price, List<DetailImage> detailImages, ThumbnailImage thumbnailImage) {
        this.id = id;
        this.category = category;
        this.detailCategory = detailCategory;
        this.itemName = itemName;
        this.description = description;
        this.color = color;
        this.size = size;
        this.price = price;
        this.detailImages = detailImages;
        this.thumbnailImage = thumbnailImage;
    }
}
