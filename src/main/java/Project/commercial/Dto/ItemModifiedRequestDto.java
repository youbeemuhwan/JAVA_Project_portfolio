package Project.commercial.Dto;


import Project.commercial.domain.DetailImage;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.File;
import java.util.List;

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
