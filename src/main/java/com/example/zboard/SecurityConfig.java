package com.example.zboard;

import org.springframework.context.annotation.*;
import org.springframework.security.authentication.dao.*;
import org.springframework.security.config.annotation.method.configuration.*;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.crypto.password.*;

import com.example.zboard.security.*;

import lombok.*;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
//@PreAuthorize - 인증을 확인 , @Secured -권한을 확인
//일반 고객이 접근할 수 있는경로 - 외부에 공개
//관리자가 접근할 수 있느 ㄴ경로 - 스프링 시큐리티 등의 보안 프레임 워크 + 외부 접근을 막는다.(intranet)
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private final PasswordEncoder passwordEncoder;  
	private final CustomUserDetailsService service;
	private final LoginSuccessHandler loginSuccessHandler;
	private final LoginFailureHandler loginFailureHandler;
	 
	@Bean
	public DaoAuthenticationProvider getAuthenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(service);
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
		return daoAuthenticationProvider;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.formLogin().loginPage("/member/login").loginProcessingUrl("/member/login").usernameParameter("username")
			.passwordParameter("password").successHandler(loginSuccessHandler).failureHandler(loginFailureHandler);
		http.logout().logoutUrl("/member/logout").logoutSuccessUrl("/");
	}
}