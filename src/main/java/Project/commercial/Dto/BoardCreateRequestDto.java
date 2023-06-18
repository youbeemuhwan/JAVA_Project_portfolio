package Project.commercial.Dto;

import Project.commercial.domain.Board;
import Project.commercial.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
public class BoardCreateRequestDto {



    private String title;

    private String content;

   private LocalDateTime created_at;

   private LocalDateTime modified_at;

    private Member member;


    public Board toEntity(BoardCreateRequestDto boardCreateRequestDto){
        return Board.builder()
                .title(boardCreateRequestDto.getTitle())
                .content(boardCreateRequestDto.getContent())
                .created_at(boardCreateRequestDto.getCreated_at())
                .member(boardCreateRequestDto.getMember())
                .build();
    }



}
