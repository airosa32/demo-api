package com.airosa.demo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Objects;

// Um USER tera N TASK
/*
 * Tabela Task no campo user_id -> referencia o campo id da 
 * tabela User, campo user_id -> aceita o mesmo id do User,
 * indicando que o User pode ter N task, e cada task tera 
 * seu id unico
 */
@Entity
@Table(name = Task.TABLE_NAME)
public class Task {
    public static final String TABLE_NAME = "task";

    @Id // Representa o PK do BD
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    // Representa Muitos para UM
    // Muitas TASK para um USER
    @ManyToOne 
    // Para fazer um Join na Coluna la no BD
    // "user_id" -> faz referencia a chave PK do USER
    // Associa uma tabela "Task" com o "user_id" de outra Tabela
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user; //ç Dono do mapeamento to @OnetoMany, o quem for usalo

    @NotNull
    @NotEmpty
    @Size(min = 1, max = 255)
    @Column(name = "description", length = 255, nullable = false)
    private String description;

    /*
     *  Todos esses metodos getter e setter e constructor e etc, podem
     *  sem trocador pelo LOMBOK -> pois ele gera tudo isso automaticamente.
     * 
     *  Para usar o LOMBOK, usa o:
     *  @NoArgsConstructor <- para informa o springboot, nao utiliza cons...
     *  @AllArgsConstructor <- para informa o uso do LOMBOK, autocria cons..
     *  @Getter
     *  @Setter
     *  @EqualsAndHashCode
     *  Ou subistitui tudo acima por @Data
     * 
     *  E tambem implementar ele nas dependencias do pom.xml, e apagar 
     *  tudo que esta aqui em baixo, pois nao ira precisar mais, pois 
     *  internamente ele tera implementado os getter... para todas a 
     *  classes dessa aplicação que utiliza a  dependencia LOMBOK contida 
     *  no arquivo pom.xml
     */

    public Task() {
    }

    public Task(Long id, User user, String description) {
        this.id = id;
        this.user = user;
        this.description = description;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Task id(Long id) {
        setId(id);
        return this;
    }

    public Task user(User user) {
        setUser(user);
        return this;
    }

    public Task description(String description) {
        setDescription(description);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true; // Objeto = This, OBJ = nulo, OBJ != instance OBJ
        if (o == null || !(o instanceof Task)) return false;

        Task other = (Task) o;

        if (this.id == null) {
            if (other.id!= null) return false;
            else if (!this.id.equals(other.id)) return false;
        }
        return Objects.equals(this.id, other.id) && Objects.equals(this.user, other.user) 
        && Objects.equals(this.description, other.description);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null)? 0 : this.id.hashCode());
        return result;
    }

}
