package com.airosa.demo.exceptions;

/* Imports para Autenticação */
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.authentication.AuthenticationFailureHandler;


/* Imports Padrao */
import org.springframework.beans.factory.annotation.Value;
// vvvv Tem que adicionar essa biblioteca "apache.commons.lang3" nas dependencia do pom.xml vvvv
import org.apache.commons.lang3.exception.ExceptionUtils;
// Serve para integridade
import org.springframework.dao.DataIntegrityViolationException;
// serve para validação
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


/* Logs... */
import lombok.extern.slf4j.Slf4j;


/* Imports de Classes para personalizar mensagens e tratamentos de Exceções */
import com.airosa.demo.services.exceptions.ObjectNotFoundException; // Para 404
import com.airosa.demo.services.exceptions.DataBindingViolationException; // Para 409

/*
 *  Essa classe vai ser o Handler -> Capturador de erros e exceções.
 * 
 *  O propio JAVA e Spring tem o Handler deles.
 * 
 *  Temos que extender Handler do Spring, e fazer algumas modificações
 *  e tambem fazewr uma anotação @Rest...  para avisar o spring.
 * 
 *  handleAccessDeniedException -> tera um para cada tipo de erro 400 500...
 */
@RestControllerAdvice

/*
 *  Essa anotação abaixo é do LOMBOK, é um LOG de
 * 
 *  Log: Ele sempre vai printar no console as anotações da classe. 
 *  TOPIC = serve para quando printar no terminal, mostra que esta 
 *  vindo dessa classe.
 */
@Slf4j(topic = "GLOBAL_EXCEPTION_HANDLER")
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler /*implements AuthenticationFailureHandler*/ {
    /*
     *  TRACE: Ele serve para mostra no terminal o erro, mas as vezes nem
     *  sempre é bom mostra isso no terminal, pois hackers podem ter acesso
     *  ao tipo de erro, e fazer um inject, entao usa o BOOLEAN para ver se
     *  retorna ou não.
     * 
     *  VALUE: indica para pegar anotação do aplication.properties.
     * 
     *  Entao ele vai pegar o VALUE, e "server.error.include-exception" ->
     *  vai estar no aplication.properties
     * 
     *  A anotação server.error.include-exception -> vai capturar o erro
     *  TRACE, la do RESPONSE -> ErrorResponse.java, e vai trazer o print 
     *  do erro.
     */
    @Value("${server.error.include-exception}")
    private boolean printStackTrace;

    /*
     * Captura qualquer tipo de eceção de argumento invalido, e retorna um
     * ReponseEntity -> Unidade de resposta de um tipo objeto. Todas classes
     * do JAVA extends o Object.
     * 
     * Retorno: retorna o erro corretro para o usuario exemplo -> não foi
     * possivel processar porque voce nao inseriu a sua senha.
     */
    //@Override
    /*
     * UNPROCESSABLE_ENTITY: trata o erro de status 422 ->  é usado quando a 
     * requisição do cliente é bem formada, mas não pode ser processada devido 
     * a erros semânticos ou de validação.
     */
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY) 
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException methodArgumentNotValidException,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Validation error. Check 'errors' field for details.");
        for (FieldError fieldError : methodArgumentNotValidException.getBindingResult().getFieldErrors()) {
            errorResponse.addValidationError(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return ResponseEntity.unprocessableEntity().body(errorResponse);
    }

    /*
     * ExceptionUtils -> Ussa essa classe para pegar as exceções que teremos, e
     * envia ela para o errorReponse, que podera ser utilizada por outro metodo
     * a fim de tratar o erro capturado(stauts 400 ...).
     * 
     * Esse metodo retorna os erros capturados pego por ExceptionUtils, o retorno
     * desse metodo podera ser usado para manipular o erro capturado e printar uma
     * mensagem de erro da forma que lhe for mais util.
     */
    private ResponseEntity<Object> buildErrorResponse(
            Exception exception,
            String message,
            HttpStatus httpStatus,
            WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), message);
        if (this.printStackTrace) {
            // Usa metodo da biblioteca add no pom.xml
            errorResponse.setStackTrace(ExceptionUtils.getStackTrace(exception));
        }
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }
    /*
     * Esse é igual o de cima so que nao tem o parametro de MENSAGEM
     */
    private ResponseEntity<Object> buildErrorResponse(
            Exception exception,
            HttpStatus httpStatus,
            WebRequest request) {
        return buildErrorResponse(exception, exception.getMessage(), httpStatus, request);
    }

    /*
     * ExceptionHandler -> Define a classe ObjectNotFoundException, como um
     * tratador desse tipo erro.
     * 
     * Erro capturado pelo metodo handleObjectNotFoundException 
     * dara um return para o metodo buildResponse() que utiliza o ExceptionUtils, 
     * 
     * User.java usara essa classe ObjectNotFoundException.java para enviar
     * a ela a mensagem de erro, e ela ira enviar ao handleObjectNotFoundException,
     * o handel... usara o metodo buildErrorReponse, e enviara os dados a ele, entao
     * ele fara que a mensagen seje printada no console.
     * 
     * NOT_FOUND: trata o erro de status 404 -> é utilizado para indicar que o 
     * recurso solicitado pelo cliente não foi encontrado no servidor. Isso 
     * significa que o servidor não conseguiu encontrar a URI (Uniform Resource 
     * Identifier) especificada na requisição feita pelo cliente.
     */
    @ExceptionHandler(ObjectNotFoundException.class) // <- Classe criada
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleObjectNotFoundException(
            ObjectNotFoundException objectNotFoundException,
            WebRequest request) {
        log.error("Failed to find the requested element", objectNotFoundException);
        return buildErrorResponse(
                objectNotFoundException,
                HttpStatus.NOT_FOUND,
                request);
    }

    /*
     * Se  o programa gerou alguma exceção e não tratou ela, retorna a
     * errorMessage = ..., e vai fazer o LOG do @Slf4..., e vai printar
     * a mensagem no console ea exceção dela. E depois vai fazer o build
     * do buildErrorResponse passando a exceção a mensagem o codigo(200 400
     * 500 ...)...
     */
    @ExceptionHandler(Exception.class)
    /*
     * INTERNAL_SERVER_ERROR: trata o erro de status 500 status que 
     * indica que ocorreu um erro inesperado no servidor 
     * enquanto tentava processar a requisição, resultando em uma falha no 
     * processamento da requisição do cliente. Esse status é genérico e não 
     * fornece detalhes específicos sobre a natureza do erro, pois é utilizado 
     * para abranger uma ampla gama de situações em que algo deu errado no servidor.
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) 
    public ResponseEntity<Object> handleAllUncaughtException(
            Exception exception,
            WebRequest request) {
        final String errorMessage = "Unknown error occurred"; // <- mensagem
        log.error(errorMessage, exception);
        return buildErrorResponse( // <- build
                exception,
                errorMessage,
                HttpStatus.INTERNAL_SERVER_ERROR,
                request);
    }

    /*
     * ExceptionHandler -> Define a classe DataBindingViolationException, como 
     * um tratador desse tipo erro
     * 
     * Erro capturado pelo metodo handleDataBindingViolationException 
     * dara um return para o metodo buildResponse() que utiliza o ExceptionUtils, 
     * 
     * User.java usara essa classe DataBindingViolationException.java para enviar
     * a ela a mensagem de erro, e ela ira enviar ao 
     * handleDataBindingViolationException, o handle... ele usara o metodo 
     * buildErrorResponse() e enviara ao metodo os dados obtidos, ele fara que a 
     * mensagen seje printada no console.
     * 
     * CONFLICT: trata o erro de status 409 -> enviou um nome de usuario ja 
     * existente no sistema, trata erro de integridade de dados no sistema.
     */

    @ExceptionHandler(DataBindingViolationException.class) // <- Classe criada
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleDataBindingViolationException(
            DataBindingViolationException dataBindingViolationException,
            WebRequest request) {
        log.error("Failed to save entity with associated data", dataBindingViolationException);
        return buildErrorResponse(
                dataBindingViolationException,
                HttpStatus.CONFLICT,
                request);
    }

    /*
     * DataIntegrityViolationException:
     * 
     * É uma exceção que geralmente ocorre em operações de persistência de dados
     * quando há uma violação de integridade dos dados. Ela é frequentemente 
     * associada a operações de banco de dados, como inserção, atualização ou 
     * exclusão de registros, quando alguma restrição de integridade é violada.
     * 
     * CONFLICT: trata o erro de status 409 -> enviou um nome de usuario ja 
     * existente no sistema, trata erro de integridade de dados no sistema.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT) // Conflict = status 409
    public ResponseEntity<Object> handleDataIntegrityViolationException(
            DataIntegrityViolationException dataIntegrityViolationException,
            WebRequest request) {
        String errorMessage = dataIntegrityViolationException.getMostSpecificCause().getMessage();
        log.error("Failed to save entity with integrity problems: " + errorMessage, dataIntegrityViolationException);
        return buildErrorResponse(
                dataIntegrityViolationException,
                errorMessage,
                HttpStatus.CONFLICT,
                request);
    }

    /*
     * ConstraintViolationException:
     * 
     * Em Java é uma exceção lançada quando uma operação falha devido a violações
     * de restrição, principalmente em operações relacionadas a persistência de 
     * dados utilizando frameworks como o Hibernate, que é comumente utilizado em
     * aplicações Java para mapeamento objeto-relacional (ORM).
     * 
     * UNPROCESSABLE_ENTITY: trata o erro de status 422 ->  é usado quando a 
     * requisição do cliente é bem formada, mas não pode ser processada devido 
     * a erros semânticos ou de validação.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<Object> handleConstraintViolationException(
            ConstraintViolationException constraintViolationException,
            WebRequest request) {
        log.error("Failed to validate element", constraintViolationException);
        return buildErrorResponse(
                constraintViolationException,
                HttpStatus.UNPROCESSABLE_ENTITY,
                request);
    }

    
    /* ----- Erros acima sao padroes, o abaixo é menos comun -----
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Object> handleAuthenticationException(
            AuthenticationException authenticationException,
            WebRequest request) {
        log.error("Authentication error ", authenticationException);
        return buildErrorResponse(
                authenticationException,
                HttpStatus.UNAUTHORIZED,
                request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Object> handleAccessDeniedException(
            AccessDeniedException accessDeniedException,
            WebRequest request) {
        log.error("Authorization error ", accessDeniedException);
        return buildErrorResponse(
                accessDeniedException,
                HttpStatus.FORBIDDEN,
                request);
    }

    @ExceptionHandler(AuthorizationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Object> handleAuthorizationException(
            AuthorizationException authorizationException,
            WebRequest request) {
        log.error("Authorization error ", authorizationException);
        return buildErrorResponse(
                authorizationException,
                HttpStatus.FORBIDDEN,
                request);
    } 
    
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        Integer status = HttpStatus.UNAUTHORIZED.value();
        response.setStatus(status);
        response.setContentType("application/json");
        ErrorResponse errorResponse = new ErrorResponse(status, "Username or password are invalid");
        response.getWriter().append(errorResponse.toJson());
    }
    */

}
