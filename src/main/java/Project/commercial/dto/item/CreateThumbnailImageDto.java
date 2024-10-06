package Project.commercial.dto.item;

import Project.commercial.domain.Item;
import Project.commercial.domain.ThumbnailImage;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateThumbnailImageDto {

    private String uploadImageName;

    private String storeImageName;

    private Long fileSize;

    private Item item;

    public CreateThumbnailImageDto(String uploadImageName, String storeImageName, Long fileSize, Item item) {
        this.uploadImageName = uploadImageName;
        this.storeImageName = storeImageName;
        this.fileSize = fileSize;
        this.item = item;
    }

    public ThumbnailImage toEntity(CreateThumbnailImageDto createThumbnailImageDto){
        return ThumbnailImage.builder()
                .uploadImageName(createThumbnailImageDto.getUploadImageName())
                .storeImageName(createThumbnailImageDto.getStoreImageName())
                .fileSize(createThumbnailImageDto.getFileSize())
                .item(createThumbnailImageDto.getItem())
                .build();

    }
}


