package Project.commercial.Dto;

import Project.commercial.domain.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.ArrayList;
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

    private List<DetailImage> detailImage = new ArrayList<>();

    private ThumbnailImage thumbnailImage;

    public ItemModifiedResponseDto(Long id, Category category, DetailCategory detailCategory, String itemName, String description, Color color, Size size, Integer price, List<DetailImage> detailImage, ThumbnailImage thumbnailImage) {
        this.id = id;
        this.category = category;
        this.detailCategory = detailCategory;
        this.itemName = itemName;
        this.description = description;
        this.color = color;
        this.size = size;
        this.price = price;
        this.detailImage = detailImage;
        this.thumbnailImage = thumbnailImage;
    }
}
