package Project.commercial.dto.board;

import Project.commercial.domain.Board;
import Project.commercial.domain.BoardImage;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateBoardImagesDto {

    private String uploadImageName;

    private String storeImageName;

    private Board board;

    public CreateBoardImagesDto(String uploadImageName, String storeImageName, Board board) {
        this.uploadImageName = uploadImageName;
        this.storeImageName = storeImageName;
        this.board = board;
    }

    public BoardImage toEntity(CreateBoardImagesDto createBoardImagesDto) {
       return BoardImage.builder()
                .uploadImageName(createBoardImagesDto.getUploadImageName())
                .storeImageName(createBoardImagesDto.getStoreImageName())
                .board(createBoardImagesDto.getBoard())
                .build();

    }
}
