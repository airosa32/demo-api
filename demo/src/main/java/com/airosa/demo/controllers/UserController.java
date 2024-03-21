package com.airosa.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

import com.airosa.demo.models.User;
import com.airosa.demo.models.User.CreateUser;
import com.airosa.demo.models.User.UpdateUser;
import com.airosa.demo.services.UserService;

import java.net.URI;

import jakarta.validation.Valid;

// O Controller tem uma anotação propia para API REST
@RestController

// Delemita a rota base dessa classe, tudo que forem fazer vai te 
// que chamar user/ todos metodos, parametros e querys que forem  
// usar vai te que chamar tbem user/...
@RequestMapping("/user")

// Como no modelo de User, colocamos algumas validações como
// @NotNull e @NotEmpty, vai te que definir esse controler
// como tipo de validação tambem
@Validated
public class UserController {
    
    // Pra nao precisar usar construtor aqui, utilza o da 
    // propia classe UserService
    @Autowired
    private UserService userService;

    /*
     *  Cria uma função para receber os dados do front-end,
     *  API vai chegar aqui no controller, quando no front
     *  faz uma busca pelo ID, ela vai cair aqui nessa função,
     *  entao tem que avisar a aplicação que ele é um GET, e
     *  que recebe uma variavel do tipo ID.
     *  
     *  localhost:8080/user/ <-- Não é o ID, e sim o numero do
     *  ID{0,1,2...}
     */
    @GetMapping("/{id}")
    
    /*
     *  Utiliza ResponseEntity -> para trata a resposta para
     *  o usuario no front-end, com o tipo selecionado <>
     *  No caso quando busca pelo ID, a response sera todos
     *  os dados do usuario menos a senha é claro, como 
     *  definimos na regra de negocio.
     */
    public ResponseEntity<User> findById(@PathVariable Long id) {
        // @PathVariable -> inidca que o ID do getmapping esta
        // relacionado com id da entrada da função findById

        User obj = this.userService.findById(id);
        // .body() monta a estrutura para enviar resposta ao  
        // front-end + os dados da aplicação EXP: arquivo JSon
        return ResponseEntity.ok().body(obj); 
    }

    
    // PostManpping -> indica que iremos inserir/criar um dado
    @PostMapping 
    // Usa a interface que criamos com as regras de validação
    // na hora de criar/inserir os dados iniciais, tal que
    // esta adicionada ao grupo dos @, nos atributos
    @Validated(CreateUser.class) 
    // @ResquestBody, indica que esta vindo dados do front-end
    public ResponseEntity<Void> create(@Valid @RequestBody User obj) {
        this.userService.create(obj);

        // Qual rota para encontrar esse usuario
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/id").buildAndExpand(obj.getId()).toUri(); 
        // .build() monta a estrutura para enviar resposta ao 
        // front-end, como .ok.build() = 200.
        return ResponseEntity.created(uri).build();
    }


    // PutMapping -> indica para atualizar dados / inserir no BD
    @PutMapping("/{id}")
    @Validated(UpdateUser.class)
    public ResponseEntity<Void> update(@Valid @RequestBody User obj, 
    @PathVariable Long id) {
        obj.setId(id);
        obj = this.userService.update(obj);
        // noContent -> indica que nao esta retornando nem um dado
        return ResponseEntity.noContent().build();
    }


    // Deletar um usario tem que ter seu ID, para saber quem vai
    // ser deletado
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.userService.delete(id);
        return ResponseEntity.noContent().build();
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
