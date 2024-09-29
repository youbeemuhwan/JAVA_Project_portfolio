package Project.commercial.dto.board;

import Project.commercial.domain.Board;
import Project.commercial.domain.Member;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class BoardCreateRequestDto {

    private String title;

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    private Integer starRate;

    private Member member;

    public Board toEntity(BoardCreateRequestDto boardCreateRequestDto){
        return Board.builder()
                .title(boardCreateRequestDto.getTitle())
                .content(boardCreateRequestDto.getContent())
                .starRate(boardCreateRequestDto.getStarRate())
                .createdAt(boardCreateRequestDto.getCreatedAt())
                .member(boardCreateRequestDto.getMember())
                .build();
    }



}
