package com.luiz.todosimple.services;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luiz.todosimple.models.Task;
import com.luiz.todosimple.models.User;
import com.luiz.todosimple.repositories.TaskRepository;

@Service
public class TaskService {
    
    @Autowired
    TaskRepository tr;
    @Autowired
    UserService us;


    public Task findById(long id){
        Optional<Task> task = tr.findById(id);
        return task.orElseThrow(() -> new RuntimeException("Não foi encontrada a task com esse id"));
    }

    
    @Transactional
    public Task createTask(Task tk){
        User user = us.findById(tk.getUser().getId());
        user.setId(null);
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

    public void delete(long id){
        findById(id);
        try{
            tr.deleteById(id);
                }
                catch(Exception e){
                    throw new RuntimeException("Não foi possível deletar");
                }
    }


}
