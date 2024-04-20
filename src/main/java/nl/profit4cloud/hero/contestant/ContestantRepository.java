package nl.profit4cloud.hero.contestant;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface ContestantRepository extends CrudRepository<Contestant, Long>, ListCrudRepository<Contestant, Long> {

    Optional<Contestant> findByUid(String name);

    boolean existsByUid(String name);

    Optional<Contestant> findBySessionId(String sessionId);

    Optional<Contestant> findBySessionIdAndUid(String sessionId, String uid);
}
