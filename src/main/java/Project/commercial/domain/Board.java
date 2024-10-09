package Project.commercial.domain;

import Project.commercial.dto.board.UpdateBoardDto;
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
    private Integer starRate;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", updatable = false)
    @JsonIgnore
    private Member member;

    @Column(name = "member_id", insertable = false, updatable = false)
    private Long memberId;

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardImage> boardImageList;
    @Builder
    public Board(Long id, String title, String content, Integer starRate, LocalDateTime createdAt, LocalDateTime modifiedAt, Member member, Long memberId, List<BoardImage> boardImageList) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.starRate = starRate;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.member = member;
        this.memberId = memberId;
        this.boardImageList = boardImageList;
    }

    public void updateBoard(UpdateBoardDto modifiedRequestDto){
        this.title = modifiedRequestDto.getTitle();
        this.content = modifiedRequestDto.getContent();
        this.starRate = modifiedRequestDto.getStarRate();
        this.modifiedAt = modifiedRequestDto.getModifiedAt();
    }
}
