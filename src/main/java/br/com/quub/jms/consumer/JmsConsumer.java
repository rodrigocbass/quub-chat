package br.com.quub.jms.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import br.com.quub.model.Chat;
import br.com.quub.model.RegisterUser;
import br.com.quub.model.storage.ChatStorage;
import br.com.quub.model.storage.RegisterUserStorage;

@Component
public class JmsConsumer {
	@Autowired
	private ChatStorage chatStorage;
	
	@Autowired
	private RegisterUserStorage registerUserStorage;

	@JmsListener(destination = "${quub.activemq.queue.chat}", containerFactory = "jsaFactory")
	public void receive(Chat chat) {
		chatStorage.add(chat);
	}
	
	@JmsListener(destination = "${quub.activemq.queue.chat.login}", containerFactory = "jsaFactory")
	public void receiveRegisterLogin(RegisterUser registerUser) {
		registerUserStorage.add(registerUser);
	}
	
	@JmsListener(destination = "${quub.activemq.queue.chat.logout}", containerFactory = "jsaFactory")
	public void receiveRegisterLogout(RegisterUser registerUser) {
		registerUserStorage.add(registerUser);
	}
}