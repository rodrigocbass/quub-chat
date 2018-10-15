package br.com.quub.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import br.com.quub.model.User;
import br.com.quub.repository.user.UserRepository;
import br.com.quub.utils.EmailUtils;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private EmailUtils emailUtils;

	public User validarUsuario(User user) throws Exception {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();

		if (!validaCamposObrigatorios(user)) {
			throw new Exception("usuario.usuario.nao.existente");
		}

		user.setCpf(trataCpf(user.getCpf()));
		User userSave = userRepository.validaUsuario(user.getEmail(), user.getCpf());

		if (userSave != null) {
			userSave.setNickName(user.getNickName());
			userSave.setCodAcesso(userSave.hashCode());
			return userSave;
		} else {
			// REGISTRA USUÁRIO PRIMEIRO LOGIN
			User userNew = new User();
			userNew.setEmail(user.getEmail());
			userNew.setCpf(user.getCpf());

			if (!StringUtils.isNotBlank(user.getNickName())) {
				userNew.setNome("Convidado_" + userNew.hashCode());
			} else {
				userNew.setNome(user.getNickName());
			}

			// GRAVA NOVO USUÁRIO
			userSave = userRepository.save(userNew);

			// Preenche ip publico do usuário e codacesso
			userSave.setIpPublico(user.getIpPublico());
			userSave.setCodAcesso(userSave.hashCode());

			emailUtils.enviaEmail(request, montaCorpo(userSave), userSave.getEmail(), "contato@quub.com.br",
					"Bem Vindo", "MONTREAL");

			return userSave;

		}
	}

	private boolean validaCamposObrigatorios(User user) {
		if (!StringUtils.isNotBlank(user.getCpf())) {
			return false;
		}

		if (!StringUtils.isNotBlank(user.getEmail())) {
			return false;
		}

		return true;
	}

	private String montaCorpo(User user) {
		// Registra data/hora login
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date dataHoraAtual = Calendar.getInstance().getTime();
		String dataLogin = df.format(dataHoraAtual);

		StringBuilder corpo = new StringBuilder();
		corpo.append("<!DOCTYPE HTML PUBLIC '-//W3C//DTD HTML 4.0 Transitional//EN'>");
		corpo.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
		corpo.append("<head>");
		corpo.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
		corpo.append("<title>Teste</title>");
		corpo.append("</head>");
		corpo.append("<body style=\"background-color: #A4D1FF;\">");
		corpo.append("<p>Olá" + user.getNome() + "</p>");
		corpo.append("<p>Ip do usuário:" + user.getIpPublico() + "</p>");
		corpo.append("<p>Horário de acesso:" + dataLogin + "</p>");
		corpo.append("</body></html>");

		return corpo.toString();
	}

	private String trataCpf(String cpf) {
		String cpfTratado = null;
		if (cpf != null) {
			cpfTratado = cpf.replaceAll("[^a-zZ-Z0-9 ]", "");
		}
		return cpfTratado;
	}

}
