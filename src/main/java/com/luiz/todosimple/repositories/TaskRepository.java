package com.luiz.todosimple.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.luiz.todosimple.models.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long>{
    
    List<Task> findByUser_Id(Long id);
  
    /*  @Query(value = "SELECT * FROM task t where t.user_id = :id",nativeQuery = true)
    List<Task> findByUser_Id(long id);
    */
    
}
