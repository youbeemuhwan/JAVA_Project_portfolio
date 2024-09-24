package Project.commercial.dto.board;


import Project.commercial.domain.BoardImage;
import Project.commercial.dto.member.MemberDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class BoardDto {

    private Long id;

    private String title;

    private String content;

    private Integer star_rate;

    private LocalDateTime created_at;

    private LocalDateTime modified_at;

    private MemberDto member;

    private List<BoardImage> boardImages;

    public BoardDto(Long id, String title, String content, Integer star_rate, LocalDateTime created_at, LocalDateTime modified_at, MemberDto member, List<BoardImage> boardImages) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.star_rate = star_rate;
        this.created_at = created_at;
        this.modified_at = modified_at;
        this.member = member;
        this.boardImages = boardImages;
    }
}
