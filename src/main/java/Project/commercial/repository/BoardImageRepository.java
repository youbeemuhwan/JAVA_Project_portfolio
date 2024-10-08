package Project.commercial.repository;

import Project.commercial.domain.BoardImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardImageRepository extends JpaRepository<BoardImage, Long> {
    List<BoardImage> findAllByBoardId(Long board_id);
}
