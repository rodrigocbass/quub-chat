package br.com.quub.repository.user;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.com.quub.model.User;

public class UserRepositoryImpl implements UserRepositoryQuery {

	@PersistenceContext
	private EntityManager manager;

	@Override
	public User validaUsuario(String email, String cpf) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<User> criteria = builder.createQuery(User.class);
		Root<User> root = criteria.from(User.class);
		Predicate[] predicates = criarRestricoes(email, cpf, builder, root);
		criteria.where(predicates);

		List<User> lista =  manager.createQuery(criteria).getResultList();
		return (lista != null && !lista.isEmpty()) ? lista.get(0) : null;
	}

	private Predicate[] criarRestricoes(String email, String cpf, CriteriaBuilder builder, Root<User> root) {
		List<Predicate> predicates = new ArrayList<>();

		predicates.add(builder.equal(root.get("email"), email));
		predicates.add(builder.equal(root.get("cpf"), cpf));

		return predicates.toArray(new Predicate[predicates.size()]);
	}

}