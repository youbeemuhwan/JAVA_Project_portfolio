package Project.commercial.Dto;

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
@Builder
public class BoardModifiedResponseDto {

    private Long id;

    private String title;

    private String content;

    private LocalDateTime modified_at;

    public BoardModifiedResponseDto(Long id, String title, String content, LocalDateTime modified_at) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.modified_at = modified_at;
    }
}
