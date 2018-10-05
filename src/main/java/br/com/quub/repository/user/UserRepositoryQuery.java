package br.com.quub.repository.user;

import br.com.quub.model.User;

public interface UserRepositoryQuery {
	public User validaUsuario(String email, String cpf);

}
