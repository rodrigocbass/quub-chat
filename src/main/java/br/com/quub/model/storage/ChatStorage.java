package br.com.quub.model.storage;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import br.com.quub.model.Chat;

@Component
public class ChatStorage {
	private List<Chat> chats = new ArrayList<Chat>();

	public void add(Chat chat) {
		chats.add(chat);
	}

	public void clear() {
		chats.clear();
	}

	public List<Chat> getAll() {
		return chats;
	}
}