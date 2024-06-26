package com.luiz.todosimple.services;

import java.util.Objects;
import java.util.Optional;

import javax.transaction.Transactional;

import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.luiz.todosimple.models.User;
import com.luiz.todosimple.models.Enums.ProfileEnum;
import com.luiz.todosimple.repositories.UserRepository;
import com.luiz.todosimple.security.UserSpringSecurity;


@Service
public class UserService {
    @Autowired
    private UserRepository ur;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository ur) {
        this.ur = ur;
        
    }

    public User findById(Long id){

        UserSpringSecurity uss = authenticated();
        if(Objects.isNull(uss) || !uss.hasRole(ProfileEnum.ADMIN) || !id.equals(uss.getId()))
        throw new RuntimeException();
        Optional<User> user = ur.findById(id);
        return user.orElseThrow(() -> new ObjectNotFoundException("Não é do tipo User"));
    }

    @Transactional
    public User cadastraUser(User usuario){
        usuario.setId(null);
        usuario.setSenha(this.passwordEncoder.encode(usuario.getSenha()));
        usuario.setProfiles(Stream.of(ProfileEnum.USER.getCode()).collect(Collectors.toSet()));
        usuario = ur.save(usuario);
        return usuario;
    }

    @Transactional
    public User alteraUser(User usuario){
        User newUsuario = findById(usuario.getId());
        newUsuario.setSenha(this.passwordEncoder.encode(usuario.getSenha()));
        return ur.save(newUsuario);


    }

    public void deleteUser(Long id){
        findById(id);
        try{
            ur.deleteById(id);
        }
        catch(Exception e) {
            throw new DataBindingViolationException("Não foi possível deletar o usuario");
        }
    }
    public UserService() {
        
    }

    public static UserSpringSecurity authenticated(){
        try{
            return (UserSpringSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        catch(Exception e){
            return null;
        }
    }
}
