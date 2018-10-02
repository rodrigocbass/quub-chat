package br.com.quub.jms.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import br.com.quub.model.Chat;

@Component
public class JmsProducer {
	@Autowired
	JmsTemplate jmsTemplate;

	@Value("${quub.activemq.queue.chat}")
	String queueChat;

	public void send(Chat chat) {
		jmsTemplate.convertAndSend(queueChat, chat);
	}
}