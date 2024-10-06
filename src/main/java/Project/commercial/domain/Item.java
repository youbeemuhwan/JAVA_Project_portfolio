package Project.commercial.domain;


import Project.commercial.dto.item.ResponseUpdateItemDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @NotNull
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "detail_category_id")
    @NotNull
    private DetailCategory detailCategory;

    @NotNull
    private String itemName;

    @NotNull
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "color_id")
    private Color color;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "size_id")
    private Size size;

    @NotNull
    private Integer price;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetailImage> detailImage = new ArrayList<>();

    @OneToOne(mappedBy = "item", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private ThumbnailImage thumbnailImage;

    @Builder
    public Item(Long id, Category category, DetailCategory detailCategory, String itemName, String description, Color color, Size size, Integer price, List<DetailImage> detailImage, ThumbnailImage thumbnailImage) {
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

    public void updateItem(ResponseUpdateItemDto responseUpdateItemDto){
        this.detailCategory = responseUpdateItemDto.getDetailCategory();
        this.category = responseUpdateItemDto.getCategory();
        this.itemName = responseUpdateItemDto.getItemName();
        this.description = responseUpdateItemDto.getDescription();
        this.color = responseUpdateItemDto.getColor();
        this.size = responseUpdateItemDto.getSize();
        this.price = (responseUpdateItemDto.getPrice());
    }


}

