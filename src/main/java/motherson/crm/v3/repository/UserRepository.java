package motherson.crm.v3.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import motherson.crm.v3.models.User;

public interface UserRepository extends JpaRepository<User,Long> {
	
	   Optional<User> findByUserName(String userName);
	   Optional<User> findByPhoneNumber(String phoneNumber);
//	   Optional<User> findByOtp(String otp);

}
