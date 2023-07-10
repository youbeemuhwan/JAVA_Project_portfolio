package Project.commercial.repository;

import Project.commercial.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByCartIdAndItemId(Long cart_id, Long item_id);

    List<CartItem> findAllByCartId(Long cart_id);




}
