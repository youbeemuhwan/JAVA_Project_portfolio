package Project.commercial.dto.item;

import Project.commercial.domain.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


import java.util.List;

@Getter
@Setter
@Builder
public class ResponseUpdateItemDto {

    private Long id;

    private Category category;

    private DetailCategory detailCategory;

    private String itemName;

    private String description;

    private Long colorId;

    private Long sizeId;

    private Integer price;

    private List<DetailImage> detailImages;

    private ThumbnailImage thumbnailImage;

    public ResponseUpdateItemDto(Long id, Category category, DetailCategory detailCategory, String itemName, String description, Long colorId, Long sizeId, Integer price, List<DetailImage> detailImages, ThumbnailImage thumbnailImage) {
        this.id = id;
        this.category = category;
        this.detailCategory = detailCategory;
        this.itemName = itemName;
        this.description = description;
        this.colorId = colorId;
        this.sizeId = sizeId;
        this.price = price;
        this.detailImages = detailImages;
        this.thumbnailImage = thumbnailImage;
    }
}
