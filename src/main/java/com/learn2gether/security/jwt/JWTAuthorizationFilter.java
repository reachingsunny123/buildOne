package com.learn2gether.security.jwt;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter  {
    
	private TokenAuthenticationService tokenAuthenticationService;
	
		public JWTAuthorizationFilter(AuthenticationManager authManager) {
	        super(authManager);
	        tokenAuthenticationService = new TokenAuthenticationService();
	    }
	
	    @Override
	    protected void doFilterInternal(HttpServletRequest req,
	                                    HttpServletResponse res,
	                                    FilterChain chain) throws IOException, ServletException {
		    System.out.println("Next Filter");
	        
	        if (tokenAuthenticationService.tokenNotFound(req)) {
	        	System.out.println("No authorization Header ..");
	            chain.doFilter(req, res);
	            return;
	        }
	        UsernamePasswordAuthenticationToken authentication = tokenAuthenticationService.getAuthentication(req);
	        SecurityContextHolder.getContext().setAuthentication(authentication);
	        chain.doFilter(req, res);
	    }
	 
	 
	 
	 
 
}
