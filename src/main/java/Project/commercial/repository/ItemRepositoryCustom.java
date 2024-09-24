package Project.commercial.repository;

import Project.commercial.dto.item.ItemSearchConditionDto;
import Project.commercial.domain.Item;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemRepositoryCustom {
    List<Item> searchItem(ItemSearchConditionDto itemSearchConditionDto, Pageable pageable);
}
