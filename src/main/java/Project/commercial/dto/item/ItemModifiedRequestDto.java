package Project.commercial.dto.item;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemModifiedRequestDto {

    private Long id;

    private Long category_id;

    private Long detailCategory_id;

    private String itemName;

    private String description;

    private Long color_id;

    private Long size_id;

    private Integer price;

}
