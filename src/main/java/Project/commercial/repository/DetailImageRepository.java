package Project.commercial.repository;

import Project.commercial.domain.DetailImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DetailImageRepository extends JpaRepository<DetailImage, Long> {
    List<DetailImage> findAllByItem_id(Long item_id);
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "delete from detail_image d where d.item_id =:item_id", nativeQuery = true)
    void deleteByItem_id(@Param("item_id") Long item_id);
}
