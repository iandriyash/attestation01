package attestation_finalProject.repository;

import attestation_finalProject.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o from Order o where o.isDeleted = false")
    List<Order> findAllActive();

    @Query("select o from Order o where o.id = :id and o.isDeleted = false")
    Optional<Order> findByIdActive(@Param("id") Long id);

    @Query("""
           select o from Order o
           where o.isDeleted = false
             and (:status is null or o.status = :status)
             and (:phone is null or o.customerPhone = :phone)
           order by o.createdAt desc
           """)
    List<Order> findAllFiltered(@Param("status") String status, @Param("phone") String phone);
}
