package motherson.crm.v3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import motherson.crm.v3.models.State;;
@Repository
public interface StateRepository extends JpaRepository<State,Long> {
  Boolean existsByStatename(String name);
}
