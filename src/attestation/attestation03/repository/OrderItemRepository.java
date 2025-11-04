package attestation.attestation03.repository;

import attestation.attestation03.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    // Базовый функционал от JpaRepository достаточен
}