package motherson.crm.v3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import motherson.crm.v3.models.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country,Long>{
	
	Boolean existsByCountryname(String name);

}
