package Project.commercial.dto.board;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BoardModifiedRequestDto {

    private Long id;

    private String title;

    private String content;

    private Integer star_rate;

    private LocalDateTime modified_at;
}
