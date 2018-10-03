package br.com.quub.repository;

import br.com.quub.model.User;

public interface UserRepositoryQuery {
	public User validaUsuario(String email, String cpf);

}
