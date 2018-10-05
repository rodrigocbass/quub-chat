package br.com.quub.repository.registeruser;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import br.com.quub.model.RegisterUser;

public class RegisterUserRepositoryImpl implements RegisterUserRepositoryQuery {

	@PersistenceContext
	private EntityManager manager;

	@Override
	public RegisterUser searchRegisterUserByIdUser(Long userId) {
		if (userId == null || userId.longValue() == 0) {
			return null;
		}
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT obj ");
		sql.append(" FROM RegisterUser obj");
		sql.append(" WHERE 1 = 1 ");

		if (userId != null) {
			sql.append(" AND obj.user.id = :userId ");
		}

		Query query = manager.createQuery(sql.toString());

		if (userId != null) {
			query.setParameter("userId", userId);
		}
		List<RegisterUser> lista = query.getResultList();
		return (lista != null && !lista.isEmpty()) ? lista.get(0) : null;

	}

}