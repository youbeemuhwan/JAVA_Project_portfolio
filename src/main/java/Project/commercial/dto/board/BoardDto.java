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

    private Integer starRate;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    private MemberDto member;

    private List<BoardImage> boardImages;

    public BoardDto(Long id, String title, String content, Integer starRate, LocalDateTime createdAt, LocalDateTime modifiedAt, MemberDto member, List<BoardImage> boardImages) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.starRate = starRate;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.member = member;
        this.boardImages = boardImages;
    }
}
