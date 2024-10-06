package Project.commercial.dto.board;

import Project.commercial.domain.BoardImage;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class ResponseBoardDto {

    private Long id;

    private String title;

    private String content;

    private Integer starRate;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    private String username;

    private List<BoardImage> boardImageList;

    public ResponseBoardDto(Long id, String title, String content, Integer starRate, LocalDateTime createdAt, LocalDateTime modifiedAt, String username, List<BoardImage> boardImageList) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.starRate = starRate;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.username = username;
        this.boardImageList = boardImageList;
    }
}
