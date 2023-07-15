package Project.commercial.repository;


import Project.commercial.domain.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {

//    @Query(value = "select * from item i join fetch i.detail_category where i.id =:item_id", nativeQuery = true)
//    Optional<Item> findById(@Param("item_id") Long item_id);
}
