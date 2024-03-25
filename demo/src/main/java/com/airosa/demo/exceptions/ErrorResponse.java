package com.airosa.demo.exceptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;
import lombok.RequiredArgsConstructor;

/*
 *  Modelo de tratamento de Error Response.
 * 
 *  Vai ser a response/mensagem que vai aparecer no log do browser,
 *  indicanto o tipo de erro que aconteceu.
 * 
 *  Essa classe é somente o RESPONSE, o Handler que vai capturar as 
 *  exceções estara em outra classe.
 */
@Getter
@Setter
// Constructor com todos atributos requeridos com o FINAL
@RequiredArgsConstructor 
/*
 * É usada para controlar como propriedades nulas de objetos Java são tratadas 
 * durante a conversão para JSON usando a biblioteca Jackson. Aqui está o que
 * ela faz:
 * 
 * @JsonInclude: É uma anotação do Jackson que especifica como incluir 
 * propriedades durante a serialização para JSON.
 * 
 * JsonInclude.Include.NON_NULL: Este valor da enumeração Include significa que 
 * apenas propriedades não nulas serão incluídas no JSON resultante. Ou seja, 
 * se uma propriedade de um objeto Java for nula, ela não será incluída no JSON.
 * 
 * So inclui o que nao for NULO.
 * 
 * Resumo: Na hora de mostrar no console os erros,
 * exemplo "stackTrace": null, "errors": null -> e eles forem NULOS, entao
 * nem mostre eles. No caso como eles aparecerao no console como NULL 
 * e nao vao dar  mais detalhamento sobre o tipo de erro e aonde aconteceu, 
 * entao eles nao irao aparecer no console. 
 * 
 * Eles vao aparecer NULOS pois mudamos o valor do
 * server.error.include-exception=false -> (contido no arquivo 
 * aplicacion.properties) como false, a fim de se proteger contra hackers de
 * INJECTION.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    /*
	 * 200 -> ok
	 * 400... -> algum erro do usuario
	 * 500... -> problema interno do servidor
	 * 
	 * 	Exception Handler -> Capturador de eventos de execeções
	 */
    private final int status; // <- 200
    private final String message; // <- ok
    // Trace: Caso acontece-sa um erro aparecera uma pilha de erro
    // para informar o tipo de erro e aonde aconteceu.
    private String stackTrace; 
    private List<ValidationError> errors;

    @Getter
    @Setter
    @RequiredArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private static class ValidationError {
        private final String field;
        private final String message;
    }

    // Para adicionar os dados ao atributo de erros,
    // temos que fazer uma pequena validação antes
    public void addValidationError(String field, String message) {
        // Se esse objeto ERRORS é vazio, cria-se um novo
        if (Objects.isNull(errors)) {
            this.errors = new ArrayList<>();
        }
        // Se o objeto errors acima for vazio cria-se um novo
        // caso contrario pula essa etapa, e entao adiciona
        // uma validação de erro no objeto ERRORS
        this.errors.add(new ValidationError(field, message));
    }

}
