package br.com.quub.model.storage;

import java.util.Date;
import java.util.HashSet;

import org.springframework.stereotype.Component;

import br.com.quub.model.RegisterUser;

@Component
public class RegisterUserStorage {
	private HashSet<RegisterUser> registerUsers = new HashSet<RegisterUser>();

	public void add(RegisterUser registerUser) {
		registerUser.setDataConexao(new Date());
		registerUsers.add(registerUser);
	}

	public void clear() {
		registerUsers.clear();
	}

	public HashSet<RegisterUser> getAll() {
		return registerUsers;
	}
}