package com.example.registration.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.*;
import org.springframework.security.config.annotation.authentication.builders.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.registration.filter.JwtRequestFilter;
import com.example.registration.filter.MyJwtRequestFilter;
import com.example.registration.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter	 {
	
	@Autowired
	JwtRequestFilter jwtRequestFilter;
	
	@Autowired
	MyJwtRequestFilter myJwtRequestFilter;
	
	@Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }
     
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
     
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
         
        return authProvider;
    }
 
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }
 
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        	.csrf().disable()
        	.authorizeRequests()
        	.antMatchers("/deleteblog/**").hasAnyAuthority("ADMIN" , "USER")
        	.antMatchers("/new").hasAnyAuthority("ADMIN", "USER")
        	.antMatchers("/editblog/**").hasAnyAuthority("ADMIN", "USER")
        	.antMatchers("/**").permitAll()
        	.antMatchers("/registeruser").permitAll()
        	.antMatchers("/token").permitAll()
           
            .anyRequest().authenticated()
            .and()
            .formLogin().permitAll()
            .defaultSuccessUrl("/token", true)
            .and()
            .logout().permitAll()
            .and()
            //.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            //.and()
            .exceptionHandling().accessDeniedPage("/403")
            ;
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(myJwtRequestFilter, jwtRequestFilter.getClass());
    }
    
    @Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
}
