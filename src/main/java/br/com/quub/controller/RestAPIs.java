package br.com.quub.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.quub.jms.producer.JmsProducer;
import br.com.quub.model.Chat;
import br.com.quub.model.ChatStorage;
import br.com.quub.model.User;
import br.com.quub.service.UserService;

@CrossOrigin(origins = "*")
@RestController
public class RestAPIs {

	@Autowired
	JmsProducer jmsProducer;

	@Autowired
	private UserService userService;

	@Autowired
	private ChatStorage chatStorage;

	@PostMapping(value = "/api/user")
	public Chat postCustomer(@RequestBody User user) {
		Chat chat = new Chat();
		try {
			User userSalvo = userService.validarUsuario(user);
			chat.setUser(userSalvo);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// jmsProducer.send(user);
		return chat;
	}

	@GetMapping(value = "/api/chats")
	public List<Chat> getMensagens() {
		List<Chat> chats = new ArrayList<Chat>(chatStorage.getAll());
		// chatStorage.clear();
		return chats;
	}

	@PostMapping(value = "/api/message")
	public Chat postMessage(@RequestBody Chat chat) {
		jmsProducer.send(chat);
		return chat;
	}
}
