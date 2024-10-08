package Project.commercial.dto.item;

import Project.commercial.domain.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ItemDto {

    private Long id;

    private Category category;

    private DetailCategory detailCategory;

    private String itemName;

    private String description;

    private Color color;

    private Size size;

    private String price;

    private List<DetailImageDto> detailImages;

    private ResponseThumbnailImageDto thumbnailImage;

    public ItemDto(Long id, Category category, DetailCategory detailCategory, String itemName, String description, Color color, Size size, String price, List<DetailImageDto> detailImages, ResponseThumbnailImageDto thumbnailImage) {
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
