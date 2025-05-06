package motherson.crm.v3.models;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import motherson.crm.v3.services.CustomUserDetails;

@Entity
public class State {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	
	@NotBlank(message="state name is required")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "state  name must contain only letters and spaces")

	private String statename;
    @Pattern(regexp = "^[A-Z]+$", message = "Country code must be in uppercase letters only")

	@NotBlank(message="state code name is required")
	private String cCode;
    
	
    
    private Boolean status;
    
    private Boolean isdelete;
    
    @Column(name = "created_by")
	private String createdby;

	//@NotBlank(message = "Modified by is required.")
   @Column(name = "modified_by")
	private String modifiedby;

	private String ipaddress;
	
	@Column(name="created_date")

	private LocalDateTime createddate;
    
	@Column(name="modified_date")
	private LocalDateTime modifieddate;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = false)  // Foreign key to Country
	@JsonBackReference
    private Country country;
	
	@ElementCollection
	private List<String> imageUrls;
	

	public List<String> getImageUrls() {
		return imageUrls;
	}

	public void setImageUrls(List<String> imageUrls) {
		this.imageUrls = imageUrls;
	}

	public String getIpaddress() {
		return ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}
	

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
    	org.springframework.security.core.Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails customUser = (CustomUserDetails) authentication.getPrincipal();
		String username = customUser.getUsername();
		String name  = customUser.name();
        this.modifiedby = name;
        modifieddate = LocalDateTime.now();
        
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	public String getStatename() {
		return statename;
	}

	public void setStatename(String statename) {
		this.statename = statename;
	}

	public String getcCode() {
		return cCode;
	}

	public void setcCode(String cCode) {
		this.cCode = cCode;
	}

	

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Boolean getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(Boolean isdelete) {
		this.isdelete = isdelete;
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
    
    
    
}
