package motherson.crm.v3.models;

import java.time.LocalDateTime;

import org.springframework.boot.autoconfigure.pulsar.PulsarProperties.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import motherson.crm.v3.services.CustomUserDetails;

@Entity
public class Country {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	@NotBlank(message="country name is required")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "Country name must contain only letters and spaces")

	private String countryname;
    @Pattern(regexp = "^[A-Z]+$", message = "Country code must be in uppercase letters only")

	@NotBlank(message="country code name is required")
	private String cCode;
    @Pattern(regexp = "\\d+", message = "Postal code must be numeric only")
	@NotBlank(message="country pcode name is required")

	private String pcode;
    
    private Boolean status;
    
    private Boolean isdelete;
    
    @Column(name = "created_by")
	private String createdby;

	//@NotBlank(message = "Modified by is required.")
   @Column(name = "modified_by")
	private String modifiedby;

	
	
	@Column(name="created_date")

	private LocalDateTime createddate;
    
	@Column(name="modified_date")
	private LocalDateTime modifieddate;
	
	
	@PrePersist
    protected void onCreate() {
//        this.createdby = SecurityContextHolder.getContext().getAuthentication().getName();
		org.springframework.security.core.Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails customUser = (CustomUserDetails) authentication.getPrincipal();
		String username = customUser.getUsername();
		String name  = customUser.name();
		this.createdby=name;

        this.modifiedby = this.createdby;
        createddate = LocalDateTime.now();
		modifieddate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.modifiedby = SecurityContextHolder.getContext().getAuthentication().getName();
        modifieddate = LocalDateTime.now();
        
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

	public Boolean getIsdelete() {
		return isdelete;
	}
	public void setIsdelete(Boolean isdelete) {
		this.isdelete = isdelete;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public String getCountryname() {
		return countryname;
	}
	public void setCountryname(String countryname) {
		this.countryname = countryname;
	}
	public String getcCode() {
		return cCode;
	}
	public void setcCode(String cCode) {
		this.cCode = cCode;
	}
	public String getPcode() {
		return pcode;
	}
	public void setPcode(String pcode) {
		this.pcode = pcode;
	}

	

}
