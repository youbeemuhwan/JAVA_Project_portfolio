package Project.commercial.domain;

import Project.commercial.Dto.BoardModifiedRequestDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String content;


    private LocalDateTime created_at;


    private LocalDateTime modified_at;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", updatable = false)
    private Member member;


    @Builder
    public Board(String title, String content, LocalDateTime created_at, LocalDateTime modified_at, Member member) {
        this.title = title;
        this.content = content;
        this.created_at = created_at;
        this.modified_at = modified_at;
        this.member = member;
    }

    public void updateBoard(BoardModifiedRequestDto modifiedRequestDto){
        this.title = modifiedRequestDto.getTitle();
        this.content = modifiedRequestDto.getContent();
        this.modified_at = modifiedRequestDto.getModified_at();
    }
}
