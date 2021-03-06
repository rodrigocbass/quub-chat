package br.com.quub.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.quub.jms.producer.JmsProducer;
import br.com.quub.model.Chat;
import br.com.quub.model.PalavraRestrita;
import br.com.quub.model.RegisterUser;
import br.com.quub.model.User;
import br.com.quub.model.storage.ChatStorage;
import br.com.quub.repository.palavrarestrita.PalavraRestritaRepository;
import br.com.quub.repository.registeruser.RegisterUserRepository;
import br.com.quub.service.UserService;

@CrossOrigin(origins = "*")
@RestController
public class RestAPIs {

	@Autowired
	JmsProducer jmsProducer;

	@Autowired
	private PalavraRestritaRepository palavraRestritaRepository;

	@Autowired
	private RegisterUserRepository registerUserRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private ChatStorage chatStorage;

	@PostMapping(value = "/api/user")
	public ResponseEntity<User> postCustomer(@RequestBody User user) {
		User userReturn = null;
		try {
			userReturn = userService.validarUsuario(user);
			return ResponseEntity.ok(userReturn);
		} catch (Exception e) {
			User userError = new User();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(userError);
		}
	}

	@GetMapping(value = "/api/restricoes")
	public List<PalavraRestrita> getListaRestricoes() {
		return palavraRestritaRepository.findAll();
	}

	@GetMapping(value = "/api/usersconnect")
	public List<RegisterUser> getUsuariosConectados() {
		return registerUserRepository.findAll();
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
