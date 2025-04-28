package motherson.crm.v3.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
import motherson.crm.v3.dto.AuthRequestotp;
import motherson.crm.v3.dto.AuthResponse;
import motherson.crm.v3.models.User;
import motherson.crm.v3.repository.UserRepository;
import motherson.crm.v3.services.SmsService;

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
    @Autowired
    private SmsService smsService;

    
   
		
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
    
    
    


 // Step 1: Generate OTP after phoneNumber + password validation
    @PostMapping("/generate-otp")
    public ResponseEntity<?> generateOtp(@RequestBody AuthRequestotp request) {
        User user = userRepository.findByPhoneNumber(request.getPhoneNumber())
                .orElseThrow(() -> new CustomException("User not found", "404"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException("Invalid password", "401");
        }

        // Generate 6-digit OTP
        String otp = String.format("%06d", new Random().nextInt(999999));
        user.setOtp(otp);
        user.setOtpRequestedTime(LocalDateTime.now());
        user.setOtpVerified(false);
        userRepository.save(user);

        // ✅ Now SEND OTP via Twilio
        smsService.sendOtp(user.getPhoneNumber(), otp);

        return ResponseEntity.ok("OTP sent successfully to your phone number.");
    }

    
    @PostMapping("/login")
    public ResponseEntity<?> verifyOtp(@RequestBody AuthRequestotp request) {
        User user = userRepository.findByPhoneNumber(request.getPhoneNumber())
                .orElseThrow(() -> new CustomException("User not found", "404"));

        if (!user.getOtp().equals(request.getOtp())) {
            throw new CustomException("Invalid OTP", "400");
        }

        if (user.getOtpRequestedTime().plusMinutes(5).isBefore(LocalDateTime.now())) {
            throw new CustomException("OTP expired", "400");
        }

        // OTP verified successfully
        user.setOtpVerified(true);
        user.setOtp(null); // Clear OTP
        userRepository.save(user);

        // Generate JWT token
        String token = jwtUtils.generateToken(user.getPhoneNumber());

        return ResponseEntity.ok(token);
    }
    
    
    
//    @PostMapping("/login")
//	//@Operation(security = @SecurityRequirement(name = ""))
//public ResponseEntity<?> login(@RequestBody AuthRequest request) {
//    User existingUser = null;
//	try {
//		existingUser = userRepository.findByUserName(request.getUsername())
//		        .orElseThrow(() -> new CustomException("User not found","404"));
//	} catch (CustomException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//    if (!passwordEncoder.matches(request.getPassword(), existingUser.getPassword())) {
//    	 throw new CustomException("Invalid credentials","401");
//    }
//    String token = jwtUtils.generateToken(existingUser.getUserName());
//  //  System.out.println("Generated JWT Token: " + token); // For debugging
//    return ResponseEntity.ok(token);
//}
    // Helper method to fetch client IP address
   private String getClientIp(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }
    
}
