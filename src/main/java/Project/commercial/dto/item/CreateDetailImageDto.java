package Project.commercial.dto.item;

import Project.commercial.domain.DetailImage;
import Project.commercial.domain.Item;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
@Builder
public class CreateDetailImageDto {

    private String uploadImageName;

    private String storeImageName;

    private Long fileSize;

    private Item item;

    public CreateDetailImageDto(String uploadImageName, String storeImageName, Long fileSize , Item item) {
        this.uploadImageName = uploadImageName;
        this.storeImageName = storeImageName;
        this.fileSize = fileSize;
        this.item = item;
    }

    public DetailImage toEntity(CreateDetailImageDto createDetailImageDto){
        return DetailImage.builder()
                .uploadImageName(createDetailImageDto.getUploadImageName())
                .storeImageName(createDetailImageDto.getStoreImageName())
                .fileSize(createDetailImageDto.getFileSize())
                .item(createDetailImageDto.getItem())
                .build();
    }
}
