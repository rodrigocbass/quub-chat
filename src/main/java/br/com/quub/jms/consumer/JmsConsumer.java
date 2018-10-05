package br.com.quub.jms.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import br.com.quub.model.Chat;
import br.com.quub.model.User;
import br.com.quub.model.storage.ChatStorage;
import br.com.quub.service.RegisterUserService;

@Component
public class JmsConsumer {

	@Autowired
	JmsTemplate jmsTemplate;

	@Autowired
	private ChatStorage chatStorage;

	@Autowired
	private RegisterUserService registerUserService;

	@JmsListener(destination = "${quub.activemq.queue.chat}", containerFactory = "jsaFactory")
	public void receive(Chat chat) {
		chatStorage.add(chat);
	}

	@JmsListener(destination = "${quub.activemq.queue.chat.login}")
	public void receiveRegisterLogin(User user) {
		try {
			registerUserService.registraUsuario(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@JmsListener(destination = "${quub.activemq.queue.chat.logout}")
	public void receiveRegisterLogout(User user) {
		try {
			registerUserService.excluiRegistroUsuario(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}