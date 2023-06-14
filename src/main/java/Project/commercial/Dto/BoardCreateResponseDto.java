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
public class BoardCreateResponseDto {

    private Long id;

    private String title;

    private String content;

    private LocalDateTime created_at;

    private LocalDateTime modified_at;

    private String username;

    public BoardCreateResponseDto(Long id, String title, String content, LocalDateTime created_at, LocalDateTime modified_at, String username) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.created_at = created_at;
        this.modified_at = modified_at;
        this.username = username;
    }
}
