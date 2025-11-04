package attestation.attestation03.repository;

import attestation.attestation03.entity.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PizzaRepository extends JpaRepository<Pizza, Long> {

    // Найти все пиццы, которые не удалены
    @Query("SELECT p FROM Pizza p WHERE p.isDeleted = false")
    List<Pizza> findAllActive();

    // Найти пиццу по ID, если не удалена
    @Query("SELECT p FROM Pizza p WHERE p.id = :id AND p.isDeleted = false")
    Optional<Pizza> findByIdActive(Long id);

    // Найти пиццы по названию (не удаленные)
    @Query("SELECT p FROM Pizza p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) AND p.isDeleted = false")
    List<Pizza> findByNameContainingIgnoreCaseAndNotDeleted(String name);
}