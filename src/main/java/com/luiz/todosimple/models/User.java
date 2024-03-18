package com.luiz.todosimple.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;

import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
@Table(name = "user")
public class User {

    @OneToMany(mappedBy = "user")
    private List<Task> listaTask = new ArrayList<Task>();
    
    public List<Task> getListaTask() {
        return listaTask;
    }

    public void setListaTask(List<Task> listaTask) {
        this.listaTask = listaTask;
    }

    public interface CreateUser{

    }

    public interface UpdateUser{

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    
   
    @Column(name = "username", unique=true, nullable = false, length = 60)
    @NotNull(groups = CreateUser.class)
    @NotEmpty(groups = CreateUser.class)
    @Size(groups = CreateUser.class, min = 3, max = 60)
    private String username;

    @JsonProperty(access = Access.WRITE_ONLY)
    @Column(name="senha", nullable = false, length = 60)
    @NotNull(groups = {CreateUser.class, UpdateUser.class})
    @NotEmpty(groups = {CreateUser.class,UpdateUser.class})
    @Size(groups = {CreateUser.class,UpdateUser.class} ,min = 8, max = 60)
    private String senha;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public User() {
    }

    public User(Long id, String username, String senha) {
        this.id = id;
        this.username = username;
        this.senha = senha;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : this.id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this)
        return true;
        if(obj == null)
            return false;
        if(!(obj instanceof User))
        return false;
        User other = (User) obj;
        if(this.id == null)
          if(other.id != null)
            return false;
            else if(!this.id.equals(other.id))
                return false;
            return Objects.equals(this.id, other.id) && Objects.equals(this.username, other.username) && Objects.equals(this.senha, other.senha);
            
        
    }

   

    

    

}
