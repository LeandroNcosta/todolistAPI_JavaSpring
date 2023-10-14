package br.com.leandro.todolist.user;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;




public interface IUserRepository extends JpaRepository<UserModel, UUID> {

  // um metodo para buscar no bd e indicando o retorno
 UserModel findByUsername(String username);
}
