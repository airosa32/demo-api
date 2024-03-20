package com.airosa.demo.models;

// Importa da mesma biblioteca para nao dar erro
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


// Entity diz para tratar ele como uma tabela
// Table indica que é uma tabela do BD
@Entity
// ou @Table(name = "user") / define nome da tabela
@Table(name = User.TABLE_NAME) 
public class User {
    // Criara uma TABELA com o nome = User.TABLE_NAME --> "user"
    public static final String TABLE_NAME = "user";  
 

    public interface CreateUser {}
    public interface UpdateUser {}
    

    // Id define que sera a PK da tabela
    @Id

    // Define uma estrategia para gera o numero no BD = Autoincrement
    // Usuario um e gerado o numero 1 no bd o User dois...
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    // Define as propiedades da coluna, nullable = false -> nao aceita 
    // valores nulos no campo
    @Column(name = "id", unique = true)
    private Long id;

    // Usar biblioteca de validação, foi adicionado no pomp.xml
    // NotBlank -> validação, utiliza os dois de uma vez aqui em baixo
    // nao precisando usar os dois, somente o, @NotBlank
    @NotNull(groups = CreateUser.class) // Nao pode ser nulo
    @NotEmpty(groups = CreateUser.class) // Nao pode ser uam string vazia "", "" = a algo
    @Size(groups = CreateUser.class, min = 2, max = 100) // Define o tamanho total do campo
    @Column(name = "username", unique = true, length = 100, nullable = false)
    private String username;

    // Garante que a senha seja somente de escrita, e nao de leitura,  
    // nao retorna pro front-end no JSON a senha
    @JsonProperty(access = Access.WRITE_ONLY) 
    @NotNull(groups = {CreateUser.class, UpdateUser.class}) // Nao pode ser nulo
    @NotEmpty(groups = {CreateUser.class, UpdateUser.class}) // Nao pode ser uam string vazia "", "" = a algo  
    @Size(groups = {CreateUser.class, UpdateUser.class}, min = 6, max = 60) // Define o tamanho total do campo  
    @Column(name = "password", length = 60, nullable = false)
    private String password;

    /*
     * OBS:
     *      Colum definde a arquiterua da coluna.
     * 
     *      Os anotation(@NotNull...) serve pra validar quando se insere 
     *      valores ao usar a classe User, antes dela enviar os dados ao 
     *      BD, ele analiza os dados e valida e depois que insere os 
     *      dados no BD.
     * 
     *      @Column(updatable = false), deixa atualizar os valores da
     *      coluna depois de criado, EXP: troca a senha...
     * 
     *      groups = CreateUser.class -> definde a que grupo ela pertence
     * 
     *      Ao utilizar a inteface exp: CreateUser, tudo a onde esta definido
     *      com groups = CreateUser.class -> ele usara esses validadores. O
     *      mesmo vale para o UpdateUser.class -> que no caso so valida o
     *      campo password, eo campo username nao valida pois nao ira mudar
     *      o username, somente a password.
     */

    // Um usuario tem uma lista de tarefas a se fazer
    // vai ser mapeado pelo user, da classe Task
    @OneToMany(mappedBy = "user") // Quem é o dono dessa classe TASK
    private List<Task> tasks = new ArrayList<Task>();


    public User() {}

    public User(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }


    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Anotação que serve para que quando no controller
    // quiser busca o USER pelo seu ID, nao retorna 
    // todas a task dele, entao meio que bloqueia esse 
    // metodo, a nao ser se utilizar ele diretamente
    @JsonIgnore
    public List<Task> getTasks() {
        return this.tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }


    // Springboot usara para fazer umas verificações do objeto por baixo
    // dos panos, o equals e hashcode.

    /*
     *  equals(Object o): Ao implementá-lo em uma classe, você está 
     *  definindo como os objetos dessa classe devem ser comparados para 
     *  verificar se são iguais.
     *
     *  Este método é usado para comparar se dois objetos são iguais.
     * 
     *  Primeiro, ele verifica se o objeto que está sendo comparado é o 
     *  mesmo objeto (this == o), e se for, retorna true imediatamente, 
     *  pois um objeto sempre é igual a ele mesmo.
     * 
     *  Em seguida, verifica se o objeto é nulo ou se não pertence à 
     *  mesma classe, retornando false nestes casos.
     * 
     *  Em seguida, faz um downcast do objeto para a classe User e 
     *  compara os campos id, username e password usando Objects.equals, 
     *  que é uma maneira segura de comparar objetos, evitando 
     *  NullPointerException.
     * 
     *  Se todos os campos forem iguais, retorna true; caso contrário, 
     *  retorna false
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) return true; // Objeto = This, OBJ = nulo, OBJ != instance OBJ
        if (o == null || !(o instanceof User)) return false;

        User other = (User) o;

        if (this.id == null) {
            if (other.id!= null) return false;
            else if (!this.id.equals(other.id)) return false;
        }
        return Objects.equals(this.id, other.id) && Objects.equals(this.username, other.username) 
        && Objects.equals(this.password, other.password);
    }

    /*
     *  hashCode(): É usado para calcular um valor numérico (o código hash) 
     *  que representa de forma única o estado de um objeto. Isso é útil em 
     *  várias situações, especialmente ao trabalhar com estruturas de dados 
     *  que dependem de códigos hash para organizar e acessar elementos de 
     *  maneira eficiente, como HashMaps, HashSets, entre outros.
     * 
     *  Cada uma dessas instâncias tem um estado diferente. pessoa1 tem o 
     *  nome "João" e idade 30, enquanto pessoa2 tem o nome "Maria" 
     *  e idade 25.
     *
     *  Este método é usado para calcular o código hash do objeto, que é 
     *  um número inteiro que representa o estado do objeto.
     * 
     *  O código hash é usado principalmente em coleções como HashSet e 
     *  HashMap para distribuir os objetos de forma eficiente. Estruturas
     *  de dados
     * 
     *  No método hashCode, chamamos Objects.hash para calcular o hash 
     *  com base nos campos id, username e password.
     * 
     *  O resultado é um valor inteiro que representa o estado combinado 
     *  desses campos.
     * 
     *  Hashcode é uma função que gera um numero, através dos  valores 
     *  do atributos dessa classe nesse exemplo, e esse numero gerado o 
     *  "hashcode" servira para identificar a onde será armazenado na 
     *  memoria, e também para acessa-lo na memoria de forma eficiente.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null)? 0 : this.id.hashCode());
        return result;
    }

}
