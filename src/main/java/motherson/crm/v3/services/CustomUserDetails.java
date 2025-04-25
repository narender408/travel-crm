package motherson.crm.v3.services;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import motherson.crm.v3.models.User;



public class CustomUserDetails implements UserDetails  {
	
	
	private User user;
	
	public CustomUserDetails(User user) {
		super();
		this.user = user;
	}
	

	
	
	 
	 
//	    public Collection<? extends GrantedAuthority> getAuthorities() {
//	        Set<GrantedAuthority> authorities = new HashSet<>();
//	        for (Role role : user.getRoles()) {
//	            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
//	            authorities.addAll(role.getPermissions().stream()
//	                .map(p -> new SimpleGrantedAuthority(p.getName()))
//	                .collect(Collectors.toSet()));
//	        }
//	        return authorities;
//	    }
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return user.getUserName();
	}
	
	public String name()
	{
		return user.getName();
		
	}

}
