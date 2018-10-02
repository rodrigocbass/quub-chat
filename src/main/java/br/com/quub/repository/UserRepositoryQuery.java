package br.com.quub.repository;

import java.awt.print.Pageable;

import org.springframework.data.domain.Page;

import br.com.quub.model.User;

public interface UserRepositoryQuery {
	public Page<User> filtrar(User userFilter, Pageable pageable);

}
