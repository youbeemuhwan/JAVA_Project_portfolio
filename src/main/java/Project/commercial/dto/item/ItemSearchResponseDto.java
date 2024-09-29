package Project.commercial.dto.item;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


import java.util.List;

@Getter
@Setter
@Builder
public class ItemSearchResponseDto {

    private List<ItemDto> item;

    private long totalCount;

    public ItemSearchResponseDto(List<ItemDto> item, long totalCount) {
        this.item = item;
        this.totalCount = totalCount;
    }
}
