package Project.commercial.Dto;

import Project.commercial.domain.Board;
import Project.commercial.domain.BoardImage;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
public class BoardImageRequestDto {

    private String uploadImageName;

    private String storeImageName;

    private Board board;

    public BoardImageRequestDto(String uploadImageName, String storeImageName, Board board) {
        this.uploadImageName = uploadImageName;
        this.storeImageName = storeImageName;
        this.board = board;
    }

    public BoardImage toEntity(BoardImageRequestDto boardImageRequestDto) {
       return BoardImage.builder()
                .uploadImageName(boardImageRequestDto.getUploadImageName())
                .storeImageName(boardImageRequestDto.getStoreImageName())
                .board(boardImageRequestDto.getBoard())
                .build();

    }
}
