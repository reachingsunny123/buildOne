package com.learn2gether.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.learn2gether.security.jwt.JWTAuthorizationFilter;
import com.learn2gether.security.jwt.JWTLoginFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
        // disable caching
        //http.headers().cacheControl();
        http
        	.cors()
        	.and()
        	.csrf().disable() //disable csrf for our requests.
            .authorizeRequests()
            .antMatchers("/").permitAll()
            .antMatchers(HttpMethod.POST, "/auth/login").permitAll()
            //.antMatchers(HttpMethod.POST, "/courses").hasRole("ADMIN")
            //.anyRequest().authenticated()
            .and()
            .addFilterBefore(new JWTLoginFilter("/auth/login", authenticationManager()),UsernamePasswordAuthenticationFilter.class)
            .addFilter(new JWTAuthorizationFilter(authenticationManager()));
        
    }
	
	@Override
	 protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	     // Create a default account
	     auth.inMemoryAuthentication()
	         .withUser("anirban")
	         .password("password")
	         .roles("USER","ADMIN")
	     .and()
		     .withUser("user")
	         .password("password")
	         .roles("USER");
	 }
	
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
		configuration.setAllowedMethods(Arrays.asList("GET","POST"));
		configuration.setExposedHeaders(Arrays.asList("Authorization")); 		//You need to explicitly expose the headers
		configuration.setAllowedHeaders(Arrays.asList("*"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

}
