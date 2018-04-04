package com.learn2gether.security.jwt;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TokenAuthenticationService {
    private long EXPIRATIONTIME = 1000 * 60 * 60; // 1 day
    private String secret = "ThisIsASecret";
    private String tokenPrefix = "Bearer ";
    private String headerString = "Authorization";
    
    public void addAuthentication(HttpServletResponse response, Authentication authentication) {
    	String username = authentication.getName();
    	System.out.println(username);
	    System.out.println(authentication.getAuthorities());
	    
	    Set<String> roles = authentication.getAuthorities().stream()
	    	     .map(r -> r.getAuthority().replace("ROLE_", "")).collect(Collectors.toSet());
	    System.out.println(roles);
	    
	    Map<String,Object> claims = new HashMap<String,Object>();
	    claims.put("username", username);
	    claims.put("roles", roles);
	    //roles.stream().forEach(role -> claims.put(role.substring(5).toLowerCase(),true));
	    
        // We generate a token now.
        String JWT = Jwts.builder()
            .setSubject(username)
            .setClaims(claims)
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
            .signWith(SignatureAlgorithm.HS512, secret)
            .compact();
        response.addHeader(headerString, tokenPrefix + JWT);
        System.out.println(response.getHeaderNames());
        System.out.println("Authorization :: " + tokenPrefix + JWT);
    }
    
    public UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
    	if(tokenNotFound(request)){
    		return null;
    	}
        String token = request.getHeader(headerString);
        System.out.println("Header :: " + token);
        if (token != null) {
            // parse the token.
        	Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token.replace(tokenPrefix, ""));
        	List<String> roles= (ArrayList<String>)claims.getBody().get("roles");
        	System.out.println(roles);
        	List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
        	roles.stream().forEach(role -> authList.add(new SimpleGrantedAuthority("ROLE_"+role)));
            System.out.println(authList);    
            String user = (String) claims.getBody().get("username");
            System.out.println(user);
            if (user != null) {
                return new UsernamePasswordAuthenticationToken(user, null,authList);
            }
            return null;
        }
        return null;
    }
    
    public boolean tokenNotFound(HttpServletRequest request){
    	String header = request.getHeader(headerString);
        if (header == null || !header.startsWith(tokenPrefix)) {
        	System.out.println("No authorization Header ..");
            return true;
        }
        return false;
    }
}