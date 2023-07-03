package Project.commercial.Dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DetailImageDto {

    private Long id;



    private String storeImageName;

    public DetailImageDto(Long id,String storeImageName) {
        this.id = id;
        this.storeImageName = storeImageName;
    }

    public DetailImageDto() {

    }
}
