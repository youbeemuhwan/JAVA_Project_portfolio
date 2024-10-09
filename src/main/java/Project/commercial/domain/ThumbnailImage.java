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
public class ThumbnailImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String uploadImageName;

    @NotNull
    private String storeImageName;

    private Long fileSize;

    @OneToOne
    @JoinColumn(name = "item_id")
    @JsonIgnore
    private Item item;

    @Column(name = "item_id", insertable = false, updatable = false)
    private Long itemId;

    @Builder
    public ThumbnailImage(Long id, String uploadImageName, String storeImageName, Long fileSize, Item item, Long itemId) {
        this.id = id;
        this.uploadImageName = uploadImageName;
        this.storeImageName = storeImageName;
        this.fileSize = fileSize;
        this.item = item;
        this.itemId = itemId;
    }
}
