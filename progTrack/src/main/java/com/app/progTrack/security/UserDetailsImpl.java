package com.app.progTrack.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.app.progTrack.entity.User;

public class UserDetailsImpl implements UserDetails {
	
	private final User user;
	private final Collection<GrantedAuthority> authorities;
	
	public UserDetailsImpl (User user, Collection<GrantedAuthority> authorities) {
		this.user = user;
		this.authorities = authorities;
	}
	
	public User getUser() {
		return user;
	}
	
	public String getName() {
		return user.getUserName();
	}
	
	public Integer getId() {
		return user.getUserId();
	}
	
	@Override
	public String getPassword() {
		return user.getPassword();
	}
	
	@Override
	public String getUsername() {
		return user.getEmail();
	}
	
	// ユーザーが有効であればtrueを返す
	@Override
	public boolean isEnabled() {
		return user.getEnabled();
	}
	
	// ユーザーの権限を返す
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}
	
}
