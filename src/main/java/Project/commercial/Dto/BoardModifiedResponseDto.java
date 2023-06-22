package Project.commercial.Dto;

import Project.commercial.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class BoardModifiedResponseDto {

    private Long id;

    private String title;

    private String content;

    private Integer star_rate;

    private LocalDateTime modified_at;

    public BoardModifiedResponseDto(Long id, String title, String content,Integer star_rate ,LocalDateTime modified_at) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.star_rate = star_rate;
        this.modified_at = modified_at;
    }
}
