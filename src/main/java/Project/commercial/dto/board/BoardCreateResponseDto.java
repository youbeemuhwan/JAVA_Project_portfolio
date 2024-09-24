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
public class BoardCreateResponseDto {

    private Long id;

    private String title;

    private String content;

    private Integer star_rate;

    private LocalDateTime created_at;

    private LocalDateTime modified_at;

    private String username;

    private List<BoardImage> boardImageList;

    public BoardCreateResponseDto(Long id, String title, String content, Integer star_rate, LocalDateTime created_at, LocalDateTime modified_at, String username, List<BoardImage> boardImageList) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.star_rate = star_rate;
        this.created_at = created_at;
        this.modified_at = modified_at;
        this.username = username;
        this.boardImageList = boardImageList;
    }
}
