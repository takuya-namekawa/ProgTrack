package com.app.progTrack.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.app.progTrack.entity.User;
import com.app.progTrack.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	private final UserRepository userRepository;
	
	public UserDetailsServiceImpl (UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public UserDetails loadUserByUsername(String eamil) throws UsernameNotFoundException {
		
		try {
			
			User user = userRepository.findByEmail(eamil);
			String userRoleName = user.getRole().getRoleName();
			Collection<GrantedAuthority> authorities = new ArrayList<>();
			authorities.add(new SimpleGrantedAuthority(userRoleName));
			return new UserDetailsImpl(user, authorities);
			
		} catch (Exception e) {
			
			throw new UsernameNotFoundException("ユーザーが見つかりませんでした");
		}
	}
}
