package com.airosa.demo.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.airosa.demo.models.User;
import com.airosa.demo.repositories.TaskRepository;
import com.airosa.demo.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {
    /*
     * Services -> É a camada de negocios, sera util para aumentar
     * a modularidade do projeto, trazendo varios beneficios, um
     * dos principais é a reasubilidade, EXP:  criar uma função dentro
     * do UserService, esse metodo pode ser de buscar um usuario pelo
     * id dele, entao muita das vezes esse metodo pode ser reutilizavel
     * em uma função de deletar um usuario, em que, de vez reescrever o 
     * metodo, pode reutilizar o anterior juntamente com poucas modificações
     * para esse novo metodo de deletar um usuario, querendo ou nao
     * para deletar um usuario tera que ter seu id, id esse obtido por
     * outra função que servira de reusabilidade para o novo metodo deletar.
     * 
     * RESUMO: Ela utiliza as classes contida no REPOSITORIES, que tem
     * metodos de interação com o BD de forma automatica pois extends
     * JpaRepository, e assim faz algumas modificaçoes de metodos ou cria,
     * afima de implementar a nossa regra de negocio, a aplicação ira usar
     * essa pasta SERVICES para interagir com o BD, o SERVICES ira usar
     * o REPOSITORIES, e os REPOSITORIES ira extends JpaRepository
     * 
     * SERVICES = Regra de negocios
     */

    // @Autowired utiliza aquelas anotações do userRepository, para 
    // instanciar e fazer a conexões que precisaremos. Ele é meio que
    // o construtor do nosso sistema
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;

    // Nome da função escolhe conforme a preferencia
    public User findById(Long id) {
        // Optional<> -> Eu vou receber um OBJ, mas tbem nao posso receber
        Optional<User> user = this.userRepository.findById(id);

        // Retorna o user, ou nulo
        // return user.orElse(null);

        // Retorna o user, ou nulo, mas no caso de execessao ele para a 
        // aplicação
        // return user.orElseThrow(() -> new UserService());

        // Retorna o user, ou nulo, mas no caso de execessao ele para 
        // nao para a aplicação
        // (ou uma exception -> Utiliza um RuntimeException) para nao parar
        // a aplicação
        return user.orElseThrow(() -> new RuntimeException(
            "User não encontrado! Id: " + id + ", Tipo: " + User.class.getName()
        ));
    }

    // Utilizar sempre que for fazer uma persistencia no BD
    // Consegue ter um controle melhor do que esta acontecendo dentro
    // da aplicação, ATOMICIDADE(ou salva tudo ou nada, nem 10 ou 50%)
    @Transactional
    public User create(User obj) {
        // Se o usuario criar a conta ja com um ID
        // Deveremos apagar para nao ocasionar erros,
        // e questao de segurança, entao realmente criara
        // uma nova conta, e nao sobrepor uma ja existente
        obj.setId(null);

        obj = this.userRepository.save(obj);

        // Cria a conta já com algumas tasks dentro
        this.taskRepository.saveAll(obj.getTasks());

        return obj;
    }

    @Transactional
    public User update(User obj) {
        // So vai atualizar, o que foi definido na classe User
        // atravez da interface UpdateUser, tal que a interface
        // so aceita o parametro password para modificar, e não
        // deixa muda o username

        // Essa função findById é da classe UserService, a que
        // esta logo acima
        // Precisa garantir que esse usuario existe
        User newOBJ = findById(obj.getId());
        // pega a senha que o usuario passo no parametro obj
        newOBJ.setPassword(obj.getPassword()); 
        return this.userRepository.save(newOBJ);
    }
    
    public void delete(Long id) {
        findById(id);

        // Toma cuidado na hora de deletar, pois uma entidade
        // pode esta relacionada a outra entidade, e com isso
        // ocasionar a um erro.
        try {
            // Se existir no BD e nao tiver relacionamentos,
            // ira executar isso, caso contrario ira para o catch.
            this.userRepository.deleteById(id);
        } catch (Exception e) {
            // Usuario nao pode ter relacionamento na hora de
            // ser deletado
            throw new RuntimeException("Não é possivel excluir pois a entidade relacionada");
        }
    }

}
