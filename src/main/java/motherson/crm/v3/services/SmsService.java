package motherson.crm.v3.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import jakarta.annotation.PostConstruct;

@Service
public class SmsService {

	@Value("${twilio.account-sid}")
    private String accountSid;

    @Value("${twilio.auth-token}")
    private String authToken;

    @Value("${twilio.phone-number}")
    private String fromPhoneNumber;
    @PostConstruct
    public void init() {
        Twilio.init(accountSid, authToken);
    }

    // Send OTP via SMS
    public void sendOtp(String toPhoneNumber, String otp) {
        try {
            Message.creator(
                    new PhoneNumber(toPhoneNumber),
                    new PhoneNumber(fromPhoneNumber),
                    "Your OTP is: " + otp
            ).create();
            System.out.println("OTP sent successfully");
        } catch (Exception e) {
        	System.err.println("Failed to send OTP: " + e.getMessage());
            throw new RuntimeException("Failed to send OTP: " + e.getMessage());
        }
    }

}
