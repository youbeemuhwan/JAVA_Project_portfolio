package Project.commercial.repository;

import Project.commercial.domain.ThumbnailImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Optional;

public interface ThumbnailImageRepository extends JpaRepository<ThumbnailImage, Long> {


    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "delete from thumbnail_image t where t.item_id=:item_id", nativeQuery = true)
    void deleteByItem_id(@Param("item_id") Long item_id);

    Optional<ThumbnailImage> findByItem_id(Long item_id);
}
