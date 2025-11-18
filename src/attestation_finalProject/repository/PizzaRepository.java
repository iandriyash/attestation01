package attestation_finalProject.repository;

import attestation_finalProject.entity.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PizzaRepository extends JpaRepository<Pizza, Long> {

    // все не удалённые
    @Query("select p from Pizza p where p.isDeleted = false")
    List<Pizza> findAllActive();

    // по id и не удалённые
    @Query("select p from Pizza p where p.id = :id and p.isDeleted = false")
    Optional<Pizza> findByIdActive(@Param("id") Long id);

    // поиск по имени среди не удалённых
    @Query("""
           select p from Pizza p
           where lower(p.name) like lower(concat('%', :name, '%'))
             and p.isDeleted = false
           """)
    List<Pizza> findByNameContainingIgnoreCaseAndNotDeleted(@Param("name") String name);
}
