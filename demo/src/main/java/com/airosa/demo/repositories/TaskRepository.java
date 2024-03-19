package com.airosa.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.airosa.demo.models.Task;
import java.util.List;


public interface TaskRepository extends JpaRepository<Task, Long> {
    /*
     * Busca a Task pelo seu Id
     * Optional<Task> fList<Task> findById(Long id);
     * 
     * Optional ->  Forma de tratar qualquer coisa que é nula,
     * para nao dar erro NullPointerExecption
     */

    
    /*
     * Segunda forma de busca uma lista de Task de um Usuario
     * 
     * O findByUser pode ser escrito como User_id, UserId...,
     * tanto faz pois nao ira mais se utilizar mais dessa regra
     * 
     * // t.user.id -> dentro da Task acessa o user, dentro do user
     * // acessa o id
     * // @Query(value = "SELECT t FROM Task t where t.user.id = :id")
     * // List<Task> xyz(@Param("id") Long id);// Tanto faz o nome da FC
     */

    /*
     * Usando SQL puro
     * 
     * // user_id -> realemnte existe esse campo no BD, t -> apelido de Task
     * // @Query(value = "SELECT * FROM task t where t.user_id = :id",
     *           nativeQuery = True)
     * // List<Task> xyz(@Param("id") Long id);// Tanto faz o nome da FC
     */
    
    
    /*
     * Busca uma lista de Task de um Usuario
     * Podemos usar o SQL puro ou usar o metodo abaixo

     * findby -> Dentro do atributo User contido na classe Task
     * findbyUser_Id -> seleciona o ID, que no caso ira redirecionar
     * ao ID da classe User.
     * 
     * _Id -> indica para selecionar o id contido no atributo user
     * da classe Task, que no caso o atributo user contem o id
     * da classe User dentro dele.
     */
    List<Task> findByUser_Id(Long id);

}
