package Project.commercial.dto.item;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Getter
@Setter
@Builder
public class ItemSearchConditionDto {

    @Nullable
    private Long categoryId;

    @Nullable
    private Long detailCategoryId;

    @Nullable
    private String itemName;

    @Nullable
    private Long colorId;

    @Nullable
    private Long sizeId;

    @Nullable
    private Integer minimumAmount;

    @Nullable
    private Integer maxAmount;


    public ItemSearchConditionDto(Long categoryId, Long detailCategoryId, String itemName, Long colorId, Long sizeId, Integer minimumAmount, Integer maxAmount) {
        this.categoryId = categoryId;
        this.detailCategoryId = detailCategoryId;
        this.itemName = itemName;
        this.colorId = colorId;
        this.sizeId = sizeId;
        this.minimumAmount = minimumAmount;
        this.maxAmount = maxAmount;
    }
}
