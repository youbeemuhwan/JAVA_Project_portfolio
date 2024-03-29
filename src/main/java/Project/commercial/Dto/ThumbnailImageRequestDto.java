package Project.commercial.Dto;

import Project.commercial.domain.Item;
import Project.commercial.domain.ThumbnailImage;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ThumbnailImageRequestDto {

    private String uploadImageName;

    private String storeImageName;

    private Long fileSize;

    private Item item;

    public ThumbnailImageRequestDto(String uploadImageName, String storeImageName, Long fileSize, Item item) {
        this.uploadImageName = uploadImageName;
        this.storeImageName = storeImageName;
        this.fileSize = fileSize;
        this.item = item;
    }

    public ThumbnailImage toEntity(ThumbnailImageRequestDto thumbnailImageRequestDto){
        return ThumbnailImage.builder()
                .uploadImageName(thumbnailImageRequestDto.getUploadImageName())
                .storeImageName(thumbnailImageRequestDto.getStoreImageName())
                .fileSize(thumbnailImageRequestDto.getFileSize())
                .item(thumbnailImageRequestDto.getItem())
                .build();

    }
}
