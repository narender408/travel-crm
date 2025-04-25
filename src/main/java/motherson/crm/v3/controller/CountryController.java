package motherson.crm.v3.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import motherson.crm.v3.customexception.CustomException;
import motherson.crm.v3.customexception.Response;
import motherson.crm.v3.models.Country;
import motherson.crm.v3.services.CountryService;

@RestController
@RequestMapping("Motherson/crm/v3")
public class CountryController {
@Autowired
private CountryService countryservice;
  @PostMapping("/CREATE")
	public ResponseEntity<?> createcountrycon( @Valid @RequestBody Country country,BindingResult bindingResult)
	{
	  if (bindingResult.hasErrors()) {
          // Collect field error messages
          Map<String, String> errors = new HashMap<>();
          bindingResult.getFieldErrors().forEach(error -> {
              errors.put(error.getField(), error.getDefaultMessage());
          });
          return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
      
	  }
	  if(countryservice.existsByContryName(country.getCountryname()))
	  {
		 return ResponseEntity.ok(new Response<>("failed","countryname already exist","failed"));
	  }
	  
	  try {
		  
		  
		Country save=  countryservice.CreateCountry(country);
		return ResponseEntity.ok(new Response<>("sucess","country created sucessfully",save));
	  }catch(Exception e)
	  {
		  throw new CustomException("failed","Internal server error");
	  }
	  
		
		
	}
  
  @GetMapping("/GetAll")
  
  public ResponseEntity<?> getAllContry()
  {
	try {  
	List<Country>getcountrys=  countryservice.getAllCountry();
	return ResponseEntity.ok(new Response<>("Suceess","fetched all data",getcountrys));
	}
	catch(Exception e)
	{
		throw new CustomException("Failed","404");
	}
	  
  }
  @GetMapping("/GetAll/{id}")
  public ResponseEntity<?> getAllCountryBYId(@PathVariable Long id)
  {
	  try {
	Optional<Country>getbyid=  countryservice.getAllCountryBYId(id);
	return ResponseEntity.ok(new Response<Optional<Country>>("sucess","fetched all data sucessfully",getbyid));
	  }catch(Exception e)
	  {
		  throw new CustomException("not found data","404");
	  }
	
	  
  }
  
  @PutMapping("/{id")
  public ResponseEntity<?> updateCountry(@PathVariable Long id,@RequestBody Country country)
  {
	  try {
		Country countryupdate=  countryservice.updateCountrys(id, country);
		return ResponseEntity.ok(new Response<>("country updated sucessfully","sucess","uodated"));
	  }catch(Exception e)
	  {
		  throw new CustomException("not found country in this id","404");
	  }
	
	
	  
  }
  @DeleteMapping("/GetAll/{id}")
public ResponseEntity<?> deleteDataFromId(@PathVariable Long id)
  {
	  try {
	       String count= countryservice.deleteCountryById(id);
	return ResponseEntity.ok(new Response("sucess","datadeleted sucessfully","deleted"));
	  }catch(Exception e)
	  {
		  throw new CustomException("not found data this id","404");
	  }
	  
  }

}
