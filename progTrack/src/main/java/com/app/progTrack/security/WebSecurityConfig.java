package com.app.progTrack.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		// カスタムフィルタの追加
		http.addFilterBefore(new CustomRedirectFilter(), UsernamePasswordAuthenticationFilter.class);
		
		http.authorizeHttpRequests((requests) -> requests
			// 全てのユーザーにアクセスを許可するURLを設定できる
			.requestMatchers("/css/**").permitAll()
			.requestMatchers("/").permitAll()
			.requestMatchers("/images/**").permitAll()
			.requestMatchers("/signup").permitAll()
			.anyRequest().authenticated()
		)
		
		.formLogin((form) -> form.loginPage("/login")
			.loginProcessingUrl("/login")
			.defaultSuccessUrl("/studytime")
			.failureUrl("/login?error")
			.permitAll()
		)
		
		.logout((logout) -> logout
			.logoutSuccessUrl("/login?loggedOut")
			.permitAll()
		)
		
		// ログインしている場合のリダイレクト設定
		.exceptionHandling(exception -> exception
			.accessDeniedPage("/access-denied")
		)
		
		.sessionManagement(session -> session
			.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
		)
		
		.csrf(csrf -> csrf
			.disable()
		);
		
		return http.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
