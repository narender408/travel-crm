package motherson.crm.v3.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import motherson.crm.v3.customexception.BadRequestException;
import motherson.crm.v3.customexception.CustomException;
import motherson.crm.v3.customexception.ErrorResponse;
import motherson.crm.v3.customexception.ResourceNotFoundException;
import motherson.crm.v3.customexception.Response;
import motherson.crm.v3.models.State;
import motherson.crm.v3.services.StateService;

@RestController
@RequestMapping("Motherson/crm/v3/state")
public class StateController {
	
	@Autowired
	private StateService stateservice;
	
	@PostMapping("/create")
	public ResponseEntity<?> statesavcon(@Valid @RequestBody State state,BindingResult bindingresult,HttpServletRequest request)
	{
		
		if (bindingresult.hasErrors()) {
	          // Collect field error messages
	          Map<String, String> errors = new HashMap<>();
	          bindingresult.getFieldErrors().forEach(error -> {
	              errors.put(error.getField(), error.getDefaultMessage());
	          });
	          return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	      
		  }
		
		if(stateservice.existByStateName(state.getStatename()))
		{
			return ResponseEntity.ok(new Response<>("failed","statename already exist","failed"));	
		}
		
		try {
			 String clientIpAddress = getClientIp(request);
			    state.setIpaddress(clientIpAddress);
			       
		State statesave=stateservice.createState(state);
		return ResponseEntity.ok(new Response<>("sucess","state save sucessfully","created"));
		
		}catch (ResourceNotFoundException ex) {
	        return new ResponseEntity<>(new ErrorResponse(
	                HttpStatus.NOT_FOUND.value(),
	                "Not Found",
	                ex.getMessage()),HttpStatus.BAD_REQUEST);
	                

	    } catch (BadRequestException ex) {
	        return new ResponseEntity<>(new ErrorResponse(
	                HttpStatus.BAD_REQUEST.value(),
	                "Bad Request",
	                ex.getMessage()
	                ), HttpStatus.BAD_REQUEST);
		
	    }catch(DataIntegrityViolationException ex)
		{
	    	return new ResponseEntity<>(new ErrorResponse(
	    			HttpStatus.BAD_REQUEST.value(),
	    			"insert the valid fk",
	    			"insert valid fk bro"),HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/getAll")
	public ResponseEntity<?> getAllStateCon()
	{
		System.out.println("hello bro");
		try {
		List<State> findstate=	stateservice.getAllState();
		return ResponseEntity.ok(new Response<>("sucess","fetched all sucessfully",findstate)) ;
		}catch(Exception e)
		{
			throw new CustomException("state not found ","404");
		}
		
		
	}
	@GetMapping("/getAll/{id}")
	public ResponseEntity<?>getAllStateByIdCon(@PathVariable Long id)
	{
		
		try {
			State getstate=stateservice.getALllStateById(id);
			return ResponseEntity.ok(new Response<>("sucess","fetch state sucessfully",getstate));
		}catch(Exception e)
		{
			throw new ResourceNotFoundException("State with ID " + id + " not found");
		}
		
		
	}
	 // Helper method to fetch client IP address
    private String getClientIp(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }

}
