package attestation.attestation03.repository;

import attestation.attestation03.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Найти все заказы, которые не удалены
    @Query("SELECT o FROM Order o WHERE o.isDeleted = false")
    List<Order> findAllActive();

    // Найти заказ по ID, если не удален
    @Query("SELECT o FROM Order o WHERE o.id = :id AND o.isDeleted = false")
    Optional<Order> findByIdActive(Long id);

    // Найти заказы по статусу (не удаленные)
    @Query("SELECT o FROM Order o WHERE o.status = :status AND o.isDeleted = false")
    List<Order> findByStatusAndNotDeleted(String status);

    // Найти заказы клиента по телефону
    @Query("SELECT o FROM Order o WHERE o.customerPhone = :phone AND o.isDeleted = false")
    List<Order> findByCustomerPhoneAndNotDeleted(String phone);
}