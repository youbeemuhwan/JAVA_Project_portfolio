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
public class DetailImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @JsonIgnore
    private String uploadImageName;

    @NotNull
    private String storeImageName;


    private Long fileSize;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", updatable = false)
    @JsonIgnore
    @NotNull
    private Item item;

    @Builder
    public DetailImage(Long id, String uploadImageName, String storeImageName,Long fileSize, Item item) {
        this.id = id;
        this.uploadImageName = uploadImageName;
        this.storeImageName = storeImageName;
        this.fileSize = fileSize;
        this.item = item;
    }
}
