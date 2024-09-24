package Project.commercial.dto.item;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DetailImageListDto {

    private Long id;

    private String storeImageName;

    @Builder
    public DetailImageListDto(Long id, String storeImageName) {
        this.id = id;
        this.storeImageName = storeImageName;
    }
}
