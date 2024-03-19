package com.luiz.todosimple.controller;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.luiz.todosimple.models.User;
import com.luiz.todosimple.models.User.CreateUser;
import com.luiz.todosimple.services.UserService;
import com.luiz.todosimple.models.User.UpdateUser;


@RestController
@RequestMapping(value = "/user")
@Validated
public class UserController {
    
    
    @Autowired
    private UserService us;


    
    @GetMapping(value = "/{id}")
    public ResponseEntity<User> findById(@PathVariable long id){
        User obj = us.findById(id);
        return ResponseEntity.ok().body(obj);
    }
    
    
    
    @Validated(CreateUser.class)
    @PostMapping()
    public ResponseEntity<Void> createUser(@Valid @RequestBody User user){
        user.setId(null);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user).toUri();
        us.cadastraUser(user);
        return ResponseEntity.created(uri).build();

    }

    @Validated(UpdateUser.class)
    @PutMapping(value = "/{id}")
    public ResponseEntity<Void> alteraUser(@Valid @RequestBody User user, @PathVariable long id){
        user.setId(id);
        us.alteraUser(user); 
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deletaUser(@PathVariable long id){
        
        us.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

















}
