package Project.commercial.dto.board;

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

    private Integer starRate;

    private LocalDateTime modifiedAt;

    public BoardModifiedResponseDto(Long id, String title, String content, Integer starRate, LocalDateTime modifiedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.starRate = starRate;
        this.modifiedAt = modifiedAt;
    }
}
