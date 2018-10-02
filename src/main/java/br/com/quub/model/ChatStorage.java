package br.com.quub.model;

import java.util.ArrayList;
import java.util.List;

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