package com.luiz.todosimple.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
@SuppressWarnings("deprecation")
public class WebConfig extends WebMvcConfigurerAdapter{
    public void addCorsMappings(CorsRegistry registery){
        registery.addMapping("/**");
    }
}
