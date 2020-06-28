package com.subway.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.authorizeRequests()
		 .antMatchers("/design", "/orders")
		  .access("hasRole('ROLE_USER')")
		 .antMatchers("/", "/**")
		  .access("permitAll")
		.and()
		 .formLogin()
		  .loginPage("/login")
		.and()
		 .logout()
		   .logoutSuccessUrl("/")
		.and()
		  .csrf();
		/*
		 * .authorizeRequests()
			.antMatchers("/", "/**")
			.permitAll();
			.antMatchers("/design", "/orders")
			.hasRole("ROLE_USER")
			이렇게 antMatchers의 순서가 바뀌면 모든 요청의 사용자가 접근가능해지고 
			/design, /orders의 요청은 효력이 없어진다.
			 * 150p확인
			 */
	}

	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	DataSource dataSource;
	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService)
			.passwordEncoder(encoder());
	}
}
	

