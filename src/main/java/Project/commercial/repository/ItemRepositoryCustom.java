package Project.commercial.repository;

import Project.commercial.Dto.ItemSearchConditionDto;
import Project.commercial.domain.Item;
import com.querydsl.core.QueryResults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepositoryCustom {

    List<Item> searchItem(ItemSearchConditionDto itemSearchConditionDto, Pageable pageable);
}
