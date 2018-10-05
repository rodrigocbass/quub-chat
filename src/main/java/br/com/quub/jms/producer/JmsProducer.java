package br.com.quub.jms.producer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import br.com.quub.model.Chat;
import br.com.quub.model.RegisterUser;

@Component
public class JmsProducer {
	@Autowired
	JmsTemplate jmsTemplate;

	@Value("${quub.activemq.queue.chat}")
	String queueChat;

	@Value("${quub.activemq.queue.user.enables}")
	String usersEnables;

	public void send(Chat chat) {
		jmsTemplate.convertAndSend(queueChat, chat);
	}

	public void send(List<RegisterUser> list) {
		if (!list.isEmpty()) {
			Gson gson = new Gson();
			for(RegisterUser registro : list) {
				registro.setDataConexao(null);
			}
			String msgEnvio = gson.toJson(list);
			jmsTemplate.convertAndSend(usersEnables, msgEnvio);
		}
	}

}