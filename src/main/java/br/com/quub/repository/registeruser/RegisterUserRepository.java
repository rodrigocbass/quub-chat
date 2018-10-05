package br.com.quub.repository.registeruser;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.quub.model.RegisterUser;

public interface RegisterUserRepository extends JpaRepository<RegisterUser, Long>, RegisterUserRepositoryQuery {

}
