package Project.commercial.Dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class BoardImageResponseDto {

    private Long id;

    private String ImageName;

    public BoardImageResponseDto(Long id, String imageName) {
        this.id = id;
        ImageName = imageName;
    }
}
