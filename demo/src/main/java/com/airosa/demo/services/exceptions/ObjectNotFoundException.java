package com.airosa.demo.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import jakarta.persistence.EntityNotFoundException;

/*
 * Exceção Costumizada.
 * 
 * Não encontrou um usuario, entao retorna um 404
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ObjectNotFoundException extends EntityNotFoundException {
    
    public ObjectNotFoundException(String message) {
        /*
         * Gera a exceção.
         * 
         * Retorna uma mensagem personalizada la para o SUPER.
         * 
         * Passa para o EntityNotFoundException a mensagem do erro, que
         * esta la na classe User.java, no metodo finById() -> "Usuario nao 
         * encontrado".
         */ 
        super(message); 
    }

}
