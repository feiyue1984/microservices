package microservices.book.multiplication.repository;

import microservices.book.multiplication.domain.Multiplication;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MultiplicationRepository extends CrudRepository<Multiplication, Long>{
    @Query("select m from Multiplication m where m.factorA = :factorA and m.factorB = :factorB")
    Optional<Multiplication> findByFactorAAndFactorB(@Param("factorA") int factorA, @Param("factorB") int factorB);
}
