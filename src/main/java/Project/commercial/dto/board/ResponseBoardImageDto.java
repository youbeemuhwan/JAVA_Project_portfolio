package Project.commercial.dto.board;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class ResponseBoardImageDto {

    private Long id;

    private String ImageName;

    public ResponseBoardImageDto(Long id, String imageName) {
        this.id = id;
        ImageName = imageName;
    }
}
