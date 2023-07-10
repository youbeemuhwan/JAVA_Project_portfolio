package Project.commercial.Dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DetailImageDto {

    private Long id;

    private String storeImageName;

    @Builder
    public DetailImageDto(Long id,String storeImageName) {
        this.id = id;
        this.storeImageName = storeImageName;
    }


}
