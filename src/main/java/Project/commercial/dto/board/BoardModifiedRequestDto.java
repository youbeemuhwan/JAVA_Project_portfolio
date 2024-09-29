package Project.commercial.dto.board;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class BoardModifiedRequestDto {

    private Long id;

    private String title;

    private String content;

    private Integer starRate;

    private LocalDateTime modified_at;

    public BoardModifiedRequestDto(Long id, String title, String content, Integer starRate, LocalDateTime modified_at) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.starRate = starRate;
        this.modified_at = modified_at;
    }
}
