package com.luiz.todosimple.services;

import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luiz.todosimple.models.Task;

import com.luiz.todosimple.models.Enums.ProfileEnum;
import com.luiz.todosimple.repositories.TaskRepository;
import com.luiz.todosimple.security.UserSpringSecurity;

@Service
public class TaskService {
    
    @Autowired
    TaskRepository tr;
    @Autowired
    UserService us;



    public Task findById(Long id){
        Task task = tr.findById(id).orElseThrow(() -> new ObjectNotFoundException("Não foi encontrada a task com esse id"));
        UserSpringSecurity uss = UserService.authenticated();
        if(!Objects.nonNull(uss) && !userHasTask(uss, task) && !uss.hasRole(ProfileEnum.ADMIN)){
            throw new RuntimeException("Acesso negado");
        }
        return task;
    }

    public List<Task> findAllByUserId(){
        UserSpringSecurity uss = UserService.authenticated();
        if(Objects.isNull(uss))
        throw new RuntimeException("Acesso negado");
        List<Task> lista = tr.findByUser_Id(uss.getId());
        return lista;
    }

    
    @Transactional
    public Task createTask(Task tk){
        UserSpringSecurity uss = UserService.authenticated();
        if(!Objects.nonNull(uss))
        throw new RuntimeException("Acesso negado");
        us.findById(uss.getId()); 
        tk = tr.save(tk);
        return tk;      
    }

    @Transactional
    public Task editTask(Task tk){
        Task newTk = findById(tk.getId());
        newTk.setDescription(tk.getDescription());
        newTk = tr.save(newTk);
        return newTk;
    }

    public void delete(Long id){
        findById(id);
        try{
            tr.deleteById(id);
                }
                catch(Exception e){
                    throw new DataBindingViolationException();
                }
    }

    public boolean userHasTask(UserSpringSecurity userSpringSecurity, Task task){
        return task.getUser().getId().equals(userSpringSecurity.getId());
    }
}
