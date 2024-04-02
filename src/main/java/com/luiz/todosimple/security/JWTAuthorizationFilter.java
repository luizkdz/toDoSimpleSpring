package com.luiz.todosimple.security;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter{

    private JWTUtil jwt;

    private UserDetailsService userDetailsService;
    



public JWTAuthorizationFilter(AuthenticationManager authenticationManager , JWTUtil jwtUtil, UserDetailsService userDetailsService){
        super(authenticationManager);
        this.jwt = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterchain) 
throws IOException,ServletException{
    String authorization = request.getHeader("Authorization");
    if(Objects.nonNull(authorization) && authorization.startsWith("Bearer ")){
        String token = authorization.substring(7);
        UsernamePasswordAuthenticationToken auth = getAuthentication(token);
        if(Objects.nonNull(auth))
            SecurityContextHolder.getContext().setAuthentication(auth);
    }
    filterchain.doFilter(request, response);


}

private UsernamePasswordAuthenticationToken getAuthentication(String token){
    if(this.jwt.isValidToken(token)){
        String username = this.jwt.getUsername(token);
        UserDetails detalhes = this.userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken upat =
         new UsernamePasswordAuthenticationToken(detalhes, null, detalhes.getAuthorities());
        return upat;
    }
    return null;
}




}