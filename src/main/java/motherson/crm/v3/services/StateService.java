package motherson.crm.v3.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import motherson.crm.v3.customexception.CustomException;
import motherson.crm.v3.models.Country;
import motherson.crm.v3.models.State;
import motherson.crm.v3.repository.StateRepository;


@Service
public class StateService {

	@Autowired
	private StateRepository staterepository;
	public State createState(State state)
	{
		 State save=staterepository.save(state);
		return save;
		
	}
	
	public Boolean existByStateName(String name)
	{
		return staterepository.existsByStatename(name);
		
	}
	
	public List<State> getAllState()
	{
		
	 List<State> findallstate=	staterepository.findAll();
		return findallstate;
		
	}
	
	public String deleteStateById(Long id)
	{
		
		 State existc= staterepository.findById(id).orElseThrow(() -> new CustomException("state not found", "404"));
		 
		 existc.setIsdelete(true);
		 staterepository.save(existc);
		return "data deleted";
		
	}
	
	
	public State getALllStateById(Long id)
	{
		
	State state=staterepository.findById(id).orElseThrow(() -> new CustomException("state not found", "404"));
		return state;
		
	}
}
