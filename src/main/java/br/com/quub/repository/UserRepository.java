package br.com.quub.repository;

import br.com.quub.model.User;

public interface UserRepository {

	public User validaUsuario(String email, Long cpf);

}