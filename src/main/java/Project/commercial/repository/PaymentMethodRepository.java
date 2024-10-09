package Project.commercial.repository;

import Project.commercial.domain.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
    Optional<PaymentMethod> findByMethod(PaymentMethod paymentMethod);
}
