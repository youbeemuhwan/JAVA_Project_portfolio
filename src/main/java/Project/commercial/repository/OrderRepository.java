package Project.commercial.repository;

import Project.commercial.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findAllByMember_id(Long member_id, Pageable pageable);
    @Query(value = "select * from orders o where o.member_id =:member_id and o.order_status_id =:order_status_id", nativeQuery = true)
    Page<Order> findAllByMember_idAndOrderStatus_id(@Param("member_id") Long member_id,
                                                    @Param("order_status_id") Long order_status_id,
                                                    Pageable pageable);



}
