package Project.commercial.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String uploadImageName;

    @NotNull
    private String storeImageName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", updatable = false)
    @JsonIgnore
    private Board board;

    @Builder
    public BoardImage(Long id, String uploadImageName, String storeImageName, Board board) {
        this.id = id;
        this.uploadImageName = uploadImageName;
        this.storeImageName = storeImageName;
        this.board = board;
    }
}
