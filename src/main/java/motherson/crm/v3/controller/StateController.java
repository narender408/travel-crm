package motherson.crm.v3.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

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
	@Autowired
    private Cloudinary cloudinary;
	
	@PostMapping("/create")
    public ResponseEntity<?> createState(
            @Valid @ModelAttribute State state,
            BindingResult result,
            @RequestParam(value = "image", required = false) MultipartFile[] files,
            HttpServletRequest request) {

        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
            );
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        if (stateservice.existByStateName(state.getStatename())) {
            return ResponseEntity.ok(new Response<>("failed", "State name already exists", null));
        }
try {
        String clientIpAddress = getClientIp(request);
	    state.setIpaddress(clientIpAddress);

        // Handle optional image upload
        if (files != null && files.length > 0) {
            List<String> imageUrls = new ArrayList<>();

            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    try {
                        String type = file.getContentType();
                        if (!("image/png".equals(type) || "image/jpeg".equals(type))) {
                             throw new ResourceNotFoundException("png and jpg image allow only"); 
                        }

                        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
                        System.out.println("Upload Result: " + uploadResult);

                        String imageUrl = (String) uploadResult.get("secure_url");
                        imageUrls.add(imageUrl);
                    } catch (Exception ex) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Image upload failed: " + ex.getMessage());
                    }
                }
            }

            state.setImageUrls(imageUrls);
        }

        State savedState = stateservice.createState(state);
        return ResponseEntity.ok(new Response<>("success", "State created successfully", savedState));
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
