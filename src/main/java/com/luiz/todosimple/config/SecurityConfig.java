package com.luiz.todosimple.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.luiz.todosimple.security.JWTAuthenticationFilter;
import com.luiz.todosimple.security.JWTAuthorizationFilter;
import com.luiz.todosimple.security.JWTUtil;



@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    UserDetailsService userDetailsService;
    

    private static final String[] public_Matchers = {  
    "/"
};
    private static final String[] post_Matchers = {
        "/login",
        "/user"
    };

    @Autowired
    private AuthenticationManager am;

    private JWTUtil jwt;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        

        AuthenticationManagerBuilder amb = http.getSharedObject(AuthenticationManagerBuilder.class);
        amb.userDetailsService(this.userDetailsService).passwordEncoder(bCryptPasswordEncoder());

        this.am = amb.build();

        http.csrf(csrf -> csrf.disable());

        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeRequests(requests -> requests.
                antMatchers(HttpMethod.POST, public_Matchers).permitAll().

                antMatchers(post_Matchers).permitAll().anyRequest().authenticated());

        
        http.addFilter(new JWTAuthenticationFilter(this.am, this.jwt));
        http.addFilter(new JWTAuthorizationFilter(am, jwt, userDetailsService));
        
                return http.build();    
   
        
   
    } 


    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
        configuration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
