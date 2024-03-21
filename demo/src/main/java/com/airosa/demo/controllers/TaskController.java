package com.airosa.demo.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
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

import com.airosa.demo.models.Task;
import com.airosa.demo.services.TaskService;
import com.airosa.demo.services.UserService;

import jakarta.validation.Valid;

import java.net.URI;
import java.util.List;

/*
 *  @RequestMapping("path", method=RequestMethod.GET)
    public String requestMethodName(@RequestParam String param) {
        return new String();
    }
 */

@RestController
@RequestMapping("/task")
@Validated
public class TaskController {
    
    @Autowired
    private TaskService taskService;

    @GetMapping("/{id}")
    public ResponseEntity<Task> findById(@PathVariable Long id) {
        Task obj = this.taskService.findById(id);
        return ResponseEntity.ok(obj);
    }

    @PostMapping
    @Validated
    public ResponseEntity<Void> create(@Valid @RequestBody Task obj) {
        this.taskService.create(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/id").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    @Validated
    public ResponseEntity<Void> update(@Valid @RequestBody Task obj, 
    @PathVariable Long id) {
        obj.setId(id);
        this.taskService.update(obj);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Validated
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.taskService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Se der um GET na lista de task de um USER, que não
    // existe, entao, nao retorne uma lista vazia, e sim um erro.
    @Autowired
    public UserService userService; 

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Task>> findAllByUserId(
        @PathVariable Long userId) {
            userService.findById(userId); //<---- Return erro aqui.
            List<Task> tasks = this.taskService.findAllByUserId(userId);
            return ResponseEntity.ok().body(tasks);
    }

    /*
     *  OBS: So passa os dados no BODY, do create e updade.
     *       Get e delete, nao se passa o dado visualmente.
     * 
     *  1: @ResquestBody, indica que esta vindo dados do front-end.
     * 
     *  2: .build() monta a estrutura para enviar resposta ao 
     *  front-end, como .ok.build() = 200.
     * 
     *  3: .body() monta a estrutura para enviar resposta ao front-end 
     *  + os dados da aplicação EXP: arquivo JSon
     * 
     *  4: .noContent() é usado para indicar que uma solicitação foi 
     *  processada com sucesso, mas não há conteúdo para retornar como 
     *  resposta. 
     */

}
