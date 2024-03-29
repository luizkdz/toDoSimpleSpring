package com.luiz.todosimple.services;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.luiz.todosimple.models.User;
import com.luiz.todosimple.repositories.UserRepository;
import com.luiz.todosimple.security.UserSpringSecurity;

public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository ur;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = ur.findByUsername(username);
        if(Objects.isNull(user))
        throw new UsernameNotFoundException("O usuário não foi encontrado" + username);
        
        return new UserSpringSecurity(user.getId(), user.getUsername(), user.getSenha(), user.getProfiles());
    }
    
}
