package Project.commercial.Dto;


import Project.commercial.domain.Category;
import Project.commercial.domain.Color;
import Project.commercial.domain.DetailCategory;
import Project.commercial.domain.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Getter
@Setter
@Builder
public class ItemSearchConditionDto {

    @Nullable
    private Long category_id;

    @Nullable
    private Long detailCategory_id;

    @Nullable
    private String itemName;

    @Nullable
    private Long color_id;

    @Nullable
    private Long size_id;

    @Nullable
    private Integer minimum_amount;

    @Nullable
    private Integer max_amount;

    public ItemSearchConditionDto() {
    }

    public ItemSearchConditionDto(Long category_id, Long detailCategory_id, String itemName, Long color_id, Long size_id, Integer minimum_amount, Integer max_amount) {
        this.category_id = category_id;
        this.detailCategory_id = detailCategory_id;
        this.itemName = itemName;
        this.color_id = color_id;
        this.size_id = size_id;
        this.minimum_amount = minimum_amount;
        this.max_amount = max_amount;
    }
}
