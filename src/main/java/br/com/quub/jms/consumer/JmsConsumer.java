package br.com.quub.jms.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import br.com.quub.model.Chat;
import br.com.quub.model.ChatStorage;

@Component
public class JmsConsumer {
	@Autowired
	private ChatStorage chatStorage;

	@JmsListener(destination = "${quub.activemq.queue.chat}", containerFactory = "jsaFactory")
	public void receive(Chat chat) {
		chatStorage.add(chat);
	}
}