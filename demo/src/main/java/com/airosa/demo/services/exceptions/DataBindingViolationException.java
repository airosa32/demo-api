package com.airosa.demo.services.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
 * Exceção Costumizada.
 * 
 * Gerencia eventos de exceções que se refere a violação de 
 * integridade e persistencia de dados.
 * 
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class DataBindingViolationException extends DataIntegrityViolationException {
    
    public DataBindingViolationException(String message) {
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
