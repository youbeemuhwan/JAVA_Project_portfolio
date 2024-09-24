package Project.commercial.domain;

import Project.commercial.dto.board.BoardModifiedRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

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

    @Min(value = 1, message = "리뷰 별점은 최소 1 이상 이어야 합니다.")
    @Max(value = 5, message = "리뷰 별점은 최대 5 이하 이어야 합니다.")
    private Integer star_rate;

    private LocalDateTime created_at;
    private LocalDateTime modified_at;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", updatable = false)
    @JsonIgnore
    private Member member;

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardImage> boardImageList;

    @Builder
    public Board(String title, String content,Integer star_rate, LocalDateTime created_at, LocalDateTime modified_at, Member member) {
        this.title = title;
        this.content = content;
        this.star_rate = star_rate;
        this.created_at = created_at;
        this.modified_at = modified_at;
        this.member = member;
    }

    public void updateBoard(BoardModifiedRequestDto modifiedRequestDto){
        this.title = modifiedRequestDto.getTitle();
        this.content = modifiedRequestDto.getContent();
        this.star_rate = modifiedRequestDto.getStar_rate();
        this.modified_at = modifiedRequestDto.getModified_at();
    }
}
