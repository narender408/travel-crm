package motherson.crm.v3.models;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
public class User {
	
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	 //userName like email 
	 
	 @NotBlank(message="username is required")
	 @Email(message = "Email should be valid")
	    private String userName;
	 
	 @NotBlank(message="name is required")
	    private String name;
	 
	 @NotBlank(message = "Contact number is required")
	    @Pattern(regexp = "\\d+", message = "Contact number must be numeric")
	    @Size(min = 10, max = 15, message = "Contact number must be between 10 and 15 digits")
	    private String mobNumber;
	    
	    @NotBlank(message="password  is required")
	    @Size(min = 3, message = "Password must be at least 3 characters")
	    private String password;
	    
	    private String ipaddress;
	    
	    private String createdby;
	    private String modifiedby;
	    
	    

		public String getName() {
			return name;
		}
		
		private boolean isdelete;
		
		@Column(name="created_date")

		private LocalDateTime createddate;
	    
		@Column(name="modified_date")
		private LocalDateTime modifieddate;
		
		
		@PrePersist
	    protected void onCreate() {
	        this.createdby = SecurityContextHolder.getContext().getAuthentication().getName();
	        this.modifiedby = this.createdby;
	        createddate = LocalDateTime.now();
			modifieddate = LocalDateTime.now();
	    }

	    @PreUpdate
	    protected void onUpdate() {
	        this.modifiedby = SecurityContextHolder.getContext().getAuthentication().getName();
	        modifieddate = LocalDateTime.now();
	        
	    }
	    
		public String getIpaddress() {
			return ipaddress;
		}

		public void setIpaddress(String ipaddress) {
			this.ipaddress = ipaddress;
		}

		public String getCreatedby() {
			return createdby;
		}

		public void setCreatedby(String createdby) {
			this.createdby = createdby;
		}

		public String getModifiedby() {
			return modifiedby;
		}

		public void setModifiedby(String modifiedby) {
			this.modifiedby = modifiedby;
		}

		public boolean isIsdelete() {
			return isdelete;
		}

		public void setIsdelete(boolean isdelete) {
			this.isdelete = isdelete;
		}

		public LocalDateTime getCreateddate() {
			return createddate;
		}

		public void setCreateddate(LocalDateTime createddate) {
			this.createddate = createddate;
		}

		public LocalDateTime getModifieddate() {
			return modifieddate;
		}

		public void setModifieddate(LocalDateTime modifieddate) {
			this.modifieddate = modifieddate;
		}

		public void setName(String name) {
			this.name = name;
		}
		public String getMobNumber() {
			return mobNumber;
		}
		public void setMobNumber(String mobNumber) {
			this.mobNumber = mobNumber;
		}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
	    

}
