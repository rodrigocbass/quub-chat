package br.com.quub.repository.registeruser;

import br.com.quub.model.RegisterUser;

public interface RegisterUserRepositoryQuery {
	public RegisterUser searchRegisterUserByIdUser(Long userId);

}
