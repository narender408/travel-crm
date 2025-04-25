package motherson.crm.v3.config;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import motherson.crm.v3.customexception.CustomMessageAuth;

@Component
public class AuthEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        // Setting response content type
    	 response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    	 response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    //	 we have created jsonand write json
    	 CustomMessageAuth customemessageauth= new CustomMessageAuth();
    	 customemessageauth.setMessage("Invalid Details!!!!!!"+ authException.getMessage());
    	 customemessageauth.setSucess(false);
         
         ObjectMapper mapper = new ObjectMapper();
          String objectstring=  mapper.writeValueAsString(customemessageauth);
          PrintWriter writer = response.getWriter();
          writer.println(objectstring);
          
    }
}
