package Project.commercial.dto.item;

import Project.commercial.domain.DetailImage;
import Project.commercial.domain.Item;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
@Builder
public class DetailImageRequestDto {

    private String uploadImageName;

    private String storeImageName;

    private Long fileSize;

    private Item item;

    public DetailImageRequestDto(String uploadImageName, String storeImageName, Long fileSize ,Item item) {
        this.uploadImageName = uploadImageName;
        this.storeImageName = storeImageName;
        this.fileSize = fileSize;
        this.item = item;
    }

    public DetailImage toEntity(DetailImageRequestDto detailImageRequestDto){
        return DetailImage.builder()
                .uploadImageName(detailImageRequestDto.getUploadImageName())
                .storeImageName(detailImageRequestDto.getStoreImageName())
                .fileSize(detailImageRequestDto.getFileSize())
                .item(detailImageRequestDto.getItem())
                .build();
    }
}
