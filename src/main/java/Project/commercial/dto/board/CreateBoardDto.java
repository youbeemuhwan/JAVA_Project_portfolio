package Project.commercial.dto.board;

import Project.commercial.domain.Board;
import Project.commercial.domain.Member;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class CreateBoardDto {

    private String title;

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    private Integer starRate;

    private Member member;

    public Board toEntity(CreateBoardDto createBoardDto){
        return Board.builder()
                .title(createBoardDto.getTitle())
                .content(createBoardDto.getContent())
                .starRate(createBoardDto.getStarRate())
                .createdAt(createBoardDto.getCreatedAt())
                .member(createBoardDto.getMember())
                .build();
    }



}
