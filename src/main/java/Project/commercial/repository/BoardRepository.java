package Project.commercial.repository;

import Project.commercial.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long>{
    Page<Board> findByTitleContaining(String keyword, Pageable pageable);
    Page<Board> findByMember_id(Long id, Pageable pageable);
}
