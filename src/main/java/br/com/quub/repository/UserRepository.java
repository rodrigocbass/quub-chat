package br.com.quub.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.quub.model.User;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryQuery{

	public User validaUsuario(String email, String cpf);

}