package com.luiz.todosimple.security;


import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luiz.todosimple.exceptions.GlobalExceptionHandler;
import com.luiz.todosimple.models.User;


public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtil jwt;


    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtutil) {
        setAuthenticationFailureHandler(new GlobalExceptionHandler());
        this.authenticationManager = authenticationManager;
        this.jwt = jwtutil;
    }

    
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
            try{
                User userCredentials = new ObjectMapper().readValue(request.getInputStream(), User.class);

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userCredentials.getUsername(), userCredentials.getSenha());
          
                Authentication auth = this.authenticationManager.authenticate(authToken);
                return auth;
          
            }


            catch(Exception e){
                throw new RuntimeException();           
             }
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request,
            HttpServletResponse response, FilterChain filterChain, Authentication authentication)
            throws IOException, ServletException {
        UserSpringSecurity userSpringSecurity = (UserSpringSecurity) authentication.getPrincipal();
        String username = userSpringSecurity.getUsername();
        String token = jwt.generateToken(username);
        response.addHeader("Authorization", "Bearer " + token);
        response.addHeader("access-control-expose-headers", "Authorization");
    }
    


        
    }

    
