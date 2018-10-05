package br.com.quub.model;

import java.io.Serializable;
import java.util.Date;

public class Chat implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String msg;
	private User user;
	private User userDestination;
	private Date datePost;

	public Chat() {
	}

	public Chat(Long id, String msg, User user) {
		this.id = id;
		this.msg = msg;
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getUserDestination() {
		return userDestination;
	}

	public void setUserDestination(User userDestination) {
		this.userDestination = userDestination;
	}

	public Date getDatePost() {
		return datePost;
	}

	public void setDatePost(Date datePost) {
		this.datePost = datePost;
	}

}
