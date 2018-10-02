package br.com.quub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.quub.model.User;
import br.com.quub.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public User validarUsuario(User user) throws Exception {
		User userSalvo = userRepository.validaUsuario(user.getEmail(), user.getCpf());

		if (userSalvo != null) {
			return userSalvo;
		} else {
			throw new Exception("usuario.usuario.nao.existente");
		}
	}

}
