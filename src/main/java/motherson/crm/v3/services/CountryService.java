package motherson.crm.v3.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import motherson.crm.v3.customexception.CustomException;
import motherson.crm.v3.models.Country;
import motherson.crm.v3.repository.CountryRepository;

@Service
public class CountryService {
	@Autowired
	private CountryRepository countryrepository;

	public Country CreateCountry( Country country)
	{
		
	  Country save=	countryrepository.save(country);
		
		return save;
		
		
	}
	
	public Boolean existsByContryName(String name)
	{
		
	Boolean cname=	countryrepository.existsByCountryname(name); 
		return cname;
		
	}

	
	public List<Country>getAllCountry()
	{
	List<Country> getall=	countryrepository.findAll();
		return getall;
		
	}
	
	public Optional<Country> getAllCountryBYId( Long id)
	{
		
			Optional<Country> countryById=countryrepository.findById(id);
		
		return countryById;
		
	}
	
	public String deleteCountryById(Long id)
	{
		
		 Country existc= countryrepository.findById(id).orElseThrow(() -> new CustomException("Country not found", "404"));
		 
		 existc.setIsdelete(true);
		 countryrepository.save(existc);
		return "data deleted";
		
	}
	
	public Country updateCountrys(Long id ,Country country)
	{
		 Country countryexist=countryrepository.findById(id).orElseThrow( ()->new CustomException("Country not found thid id","404"));
		
		 countryexist.setCountryname(country.getCountryname());
		 countryexist.setPcode(country.getPcode());
		 countryexist.setcCode(country.getcCode());
		 Country uodated=countryrepository.save(countryexist);
		return uodated;
		
	}
}
