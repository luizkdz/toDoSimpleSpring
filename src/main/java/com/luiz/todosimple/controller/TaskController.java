package com.luiz.todosimple.controller;

import java.net.URI;
import java.util.List;

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

import com.luiz.todosimple.models.Task;
import com.luiz.todosimple.repositories.TaskRepository;
import com.luiz.todosimple.services.TaskService;

@RestController
@RequestMapping(value = "/tasks")
public class TaskController {
        @Autowired   
        TaskService ts; 

        TaskRepository tr;

    
        @GetMapping(value = "/{id}")
        public ResponseEntity<Task> findById(@PathVariable Long id){
            Task tk = ts.findById(id);
            return ResponseEntity.ok().body(tk);
    }

    @GetMapping(value = "/user/{userId}")
    public ResponseEntity<List<Task>> allTasksFromUserId(@PathVariable Long user_Id){
        List<Task> lista = tr.findByUser_Id(user_Id);
        return ResponseEntity.ok().body(lista);
        }
        
    @PostMapping(value = "/{id}")
    @Validated
    public ResponseEntity<Void> createTask( @RequestBody Task task){
        task.setId(null);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(task.getId()).toUri();
        task = ts.createTask(task);
        return ResponseEntity.created(uri).build();

    }

    @PutMapping(value = "/{id}")
    @Validated
    public ResponseEntity<Void> updateTask(@Valid @RequestBody Task task, @PathVariable Long id){
        task.setId(id);
        task = ts.editTask(task);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id){
        ts.delete(id);
        return ResponseEntity.noContent().build();
    }
}
