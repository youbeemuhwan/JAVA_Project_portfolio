package Project.commercial.repository;

import Project.commercial.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {


    Optional<Cart> findByMemberId(Long memberId);
    void deleteByMemberId(Long memberId);
}
