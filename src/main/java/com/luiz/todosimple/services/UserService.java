package com.luiz.todosimple.services;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luiz.todosimple.models.User;
import com.luiz.todosimple.repositories.TaskRepository;
import com.luiz.todosimple.repositories.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository ur;
    @Autowired
    private TaskRepository tr;

    public UserService(UserRepository ur, TaskRepository tr) {
        this.ur = ur;
        this.tr = tr;
    }

    public User findById(Long id){
        Optional<User> user = ur.findById(id);
        return user.orElseThrow(() -> new RuntimeException("Não é do tipo User"));
    }

    @Transactional
    public User cadastraUser(User usuario){
        usuario.setId(null);
        usuario = ur.save(usuario);
        return usuario;
    }

    @Transactional
    public User alteraUser(User usuario){
        User newUsuario = findById(usuario.getId());
        newUsuario.setSenha(usuario.getSenha());
        return ur.save(newUsuario);


    }

    public void deleteUser(Long id){
        findById(id);
        try{
            ur.deleteById(id);
        }
        catch(Exception e) {
            throw new RuntimeException("O id está vinculado a outras entidades");
        }
    }
    public UserService() {
        
    }
}
