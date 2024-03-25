package com.airosa.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.airosa.demo.models.Task;
import com.airosa.demo.models.User;
import com.airosa.demo.repositories.TaskRepository;
import com.airosa.demo.services.exceptions.DataBindingViolationException;
import com.airosa.demo.services.exceptions.ObjectNotFoundException;

import jakarta.transaction.Transactional;

@Service
public class TaskService {
    
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserService userService;

    public Task findById(Long id) {
        Optional<Task> task = this.taskRepository.findById(id);
        /*
         * Troca RuntimeException() para o ObjectNotFoundException.java,
         * classe personalizada para tratar esse erro que criamos.
         */
        return task.orElseThrow(() -> new ObjectNotFoundException(
            "Tarefa não encontrada Id: " + id + ", Tipo: " + Task.class.getName()
        ));
    }

    public List<Task> findAllByUserId(Long userId) {
        List<Task> tasks = this.taskRepository.findByUser_Id(userId);
        return tasks; // Retorna uma lista de Tasks.
    }

    @Transactional
    public Task create(Task obj) {
        User user = this.userService.findById(obj.getUser().getId());
        obj.setId(null);
        obj.setUser(user);
        obj = this.taskRepository.save(obj);
        return obj;
    }

    @Transactional
    public Task update(Task obj) {
        Task newObj = findById(obj.getId());
        newObj.setDescription(obj.getDescription());
        return this.taskRepository.save(newObj);
    }

    public void delete(Long id) {
        findById(id);
        try {
            this.taskRepository.deleteById(id);
        } catch (Exception e) {
            /*
             * Troca RuntimeException() para o DataBindingViolationException.java,
             * classe personalizada para tratar esse erro que criamos.
             */
            throw new DataBindingViolationException("Não é possivel exluir pois" +
            " há entidades relacionadas!");
        }
    }
}
