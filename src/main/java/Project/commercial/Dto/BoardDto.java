package Project.commercial.Dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class BoardDto {


    private Long id;


    private String title;


    private String content;


    private LocalDateTime created_at;


    private LocalDateTime modified_at;

    private MemberDto member;

    public BoardDto(Long id, String title, String content, LocalDateTime created_at, LocalDateTime modified_at, MemberDto member) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.created_at = created_at;
        this.modified_at = modified_at;
        this.member = member;
    }
}
