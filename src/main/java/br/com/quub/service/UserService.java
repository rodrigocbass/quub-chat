package br.com.quub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.quub.model.User;
import br.com.quub.repository.user.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public User validarUsuario(User user) throws Exception {
		user.setCpf(trataCpf(user.getCpf()));
		User userSalvo = userRepository.validaUsuario(user.getEmail(), user.getCpf());

		if (userSalvo != null) {
			userSalvo.setNickName(user.getNickName());
			userSalvo.setCodAcesso(userSalvo.hashCode());
			return userSalvo;
		} else {
			throw new Exception("usuario.usuario.nao.existente");
			/*
			 * User usuarioConvidado = new User();
			 * usuarioConvidado.setNickName(user.getNickName());
			 * usuarioConvidado.setNome("Convidado");
			 * usuarioConvidado.setEmail("convidado@quub.com.br");
			 * 
			 * usuarioConvidado.setCodAcesso(usuarioConvidado.hashCode());
			 * 
			 * return usuarioConvidado;
			 */
		}
	}

	private String trataCpf(String cpf) {
		String cpfTratado = null;
		if (cpf != null) {
			cpfTratado = cpf.replaceAll("[^a-zZ-Z0-9 ]", "");
		}
		return cpfTratado;
	}

}
