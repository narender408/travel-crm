package motherson.crm.v3.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import motherson.crm.v3.config.JwtUtils;
import motherson.crm.v3.customexception.CustomException;
import motherson.crm.v3.customexception.CustomMessageAuth;
import motherson.crm.v3.customexception.Response;
import motherson.crm.v3.dto.AuthRequest;
import motherson.crm.v3.dto.AuthResponse;
import motherson.crm.v3.models.User;
import motherson.crm.v3.repository.UserRepository;

@RestController
@RequestMapping("Motherson/crm/v3")
public class AuthController {
    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private JwtUtils jwtUtils;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
		
    
   
		
    @PostMapping("/register")
//		@Operation(security = @SecurityRequirement(name = ""))
    public ResponseEntity<?> register(@Valid @RequestBody User user, BindingResult bindingResult,HttpServletRequest request) {
    	
    	
    	 // ✅ Check for validation errors
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
            );

            return ResponseEntity.badRequest().body(new Response<>("Failed", "Validation failed", errors));
        }

        if (userRepository.findByUserName(user.getUserName()).isPresent()) {
        	
        	CustomMessageAuth response =new CustomMessageAuth();
        	response.setMessage("user already exist!!!!");
        	response.setSucess(false);
        	
        	 return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        //System.out.println(SecurityContextHolder.getContext().getAuthentication().getDetails());
        // Fetch and set IP address
        String clientIpAddress = getClientIp(request);
        user.setIpaddress(clientIpAddress);
        
        
        userRepository.save(user);
        CustomMessageAuth response =new CustomMessageAuth();
        response.setMessage("User registered successfully");
        response.setSucess(true);
        
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    
    


//    @PostMapping("/login")
//    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
//        Authentication auth = authManager.authenticate(
//                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
//
//        String token = jwtUtils.generateToken(request.getUsername());
//        return ResponseEntity.ok(new AuthResponse(token));
//    }
    
    @PostMapping("/login")
	//@Operation(security = @SecurityRequirement(name = ""))
public ResponseEntity<?> login(@RequestBody AuthRequest request) {
    User existingUser = null;
	try {
		existingUser = userRepository.findByUserName(request.getUsername())
		        .orElseThrow(() -> new CustomException("User not found","404"));
	} catch (CustomException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    if (!passwordEncoder.matches(request.getPassword(), existingUser.getPassword())) {
    	 throw new CustomException("Invalid credentials","401");
    }
    String token = jwtUtils.generateToken(existingUser.getUserName());
  //  System.out.println("Generated JWT Token: " + token); // For debugging
    return ResponseEntity.ok(token);
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
