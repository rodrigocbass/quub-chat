package br.com.quub.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.quub.jms.producer.JmsProducer;
import br.com.quub.model.RegisterUser;
import br.com.quub.model.User;
import br.com.quub.repository.registeruser.RegisterUserRepository;

@Service
public class RegisterUserService {

	@Autowired
	private RegisterUserRepository registerUserRepository;

	@Autowired
	private JmsProducer jmsProducer;

	public void registraUsuario(User user) throws Exception {

		if (user != null && user.getId() != null && user.getId() != 0 && this.searchRegisterUserByUser(user) == null) {
			RegisterUser register = new RegisterUser();
			register.setUser(user);
			register.setDataConexao(new Date());
			register.setAtivo(true);

			// persiste registro
			registerUserRepository.save(register);
		}

		// Envia nova lista
		sendNewList();
	}

	public void excluiRegistroUsuario(User user) throws Exception {
		if (user != null && user.getId() != null && user.getId() != 0) {
			RegisterUser registroSalvo = this.searchRegisterUserByUser(user);

			if (registroSalvo != null)
				registerUserRepository.delete(registroSalvo.getId());
		}

		// Envia nova lista
		sendNewList();
	}

	private void sendNewList() {
		jmsProducer.send(registerUserRepository.findAll());
	}

	public RegisterUser searchRegisterUserByUser(User user) {
		if (user != null && user.getId() != null) {
			return this.registerUserRepository.searchRegisterUserByIdUser(user.getId());
		}

		return null;
	}

}
