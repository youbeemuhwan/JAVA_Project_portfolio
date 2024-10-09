package Project.commercial.repository;

import Project.commercial.domain.OrderStatus;
import Project.commercial.enums.OrderStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderStatusRepository extends JpaRepository<OrderStatus, Long> {
    Optional<OrderStatus> findByStatus(OrderStatusEnum orderStatus);
}
