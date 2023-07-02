package Project.commercial.Dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DetailImageListDto {

    private Long id;



    private String storeImageName;

    public DetailImageListDto(Long id, String storeImageName) {
        this.id = id;
        this.storeImageName = storeImageName;
    }

    public DetailImageListDto() {

    }
}
