package Project.commercial.dto.item;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ResponseThumbnailImageDto {



    private String storeImageName;
    private Long itemId;

    public ResponseThumbnailImageDto(String storeImageName, Long itemId) {
        this.storeImageName = storeImageName;
        this.itemId = itemId;
    }
}

