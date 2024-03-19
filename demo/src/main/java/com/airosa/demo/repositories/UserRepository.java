package com.airosa.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.airosa.demo.models.User;

/*
 * De vez montar cada query de forma direta para acessar o BD ou extrair 
 * infos, usa-se metodos propios do springboot, que faz as query de forma
 * automatica, sem a nessecidade de montar cada query.
 */
@Repository
// JpaRepository<User> -> Serve para conectar UserRepository ao User.java
// JpaRepository<User, long> -> long é o tipo do ID=PK da tabela
public interface UserRepository extends JpaRepository<User, Long> {
    
    /*
     * Função para retorna um usuario do BD
     * findBy-> igual ao select
     * findBy -> select by "aqui sao os atributos contidos na classe User"
     * User findByUsername(String username); // passa o username para a FBy
     * Esse metodo e outros sao padrao contido no JpaRepository
     * Para inserir coletar, 1 dado e N dados ou listas e etc no BD
     */

    /*
     * JpaRepository -> extends varias outros classes cada uma serve
     * para interagir com o BD, o mais simples dele é o CrudRepository
     * que tem funcoes simples de CRUD no BD, poderia extender essa
     * classe que estamos usando como CrudRepository de vez a
     * JpaRepository, se fosemos apenas usar o CRUD padrao.
     */
}
