package br.com.quub.utils;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Classe utilitria para envio de email
 * 
 * No transformei essa classe com static para poder ter vrias configuraes de
 * email e envios por bot
 * 
 * @author Rodrigo Cesar
 * @version 1.1
 */

@Component
public class EmailUtils {

	@Value("${mail.smtp.host}")
	String emailSmtpHost;

	@Value("${mail.smtp.porta}")
	String emailSmtpPorta;

	@Value("${mail.user}")
	String emailUser;

	@Value("${mail.password}")
	String emailPassword;

	private static Logger logger = Logger.getLogger(EmailUtils.class);

	/**
	 * Usurio para autenticao
	 */
	private String username;

	/**
	 * Senha para autenticao
	 */
	private String password;

	/**
	 * Classe de autenticador de email
	 */
	private Authenticator auth;

	/**
	 * Instncia singleton da classe
	 */
	private static EmailUtils instance;

	/**
	 * Mapa de configuraes do email, procure preencher isso no incio da aplicao, com
	 * dados advindos do banco ou do web.xml, ou at mesmo um arquivo de properties
	 * na aplicao
	 * 
	 * Exemplo: buscar do web.xml os campos abaixo String servidor =
	 * event.getServletContext().getInitParameter("mail.smtp.host"); String porta =
	 * event.getServletContext().getInitParameter("mail.smtp.porta"); String usaAuth
	 * = event.getServletContext().getInitParameter("mail.auth"); String usuario =
	 * event.getServletContext().getInitParameter("mail.user"); String senha =
	 * event.getServletContext().getInitParameter("mail.password");
	 * 
	 * // Configurar o email EmailUtils.getInstance().configurar(porta, servidor);
	 * 
	 * // Autenticar email if(usaAuth != null && usaAuth.equals("true"))
	 * EmailUtils.getInstance().configurarAutenticacao(usuario, senha);
	 * 
	 *
	 * 
	 */
	private Properties configuracoes;

	private EmailUtils() {
		configuracoes = new Properties();
	}

	/**
	 * Classe interna para criao de um autenticador para a classe de email, nica
	 * para o sistema inteira<br/>
	 * Se precisar de envio de email com servidores smtps ou imaps mltiplos
	 * necessrio evoluir essa classe, e tirar o DP Singleton da mesma.
	 * 
	 * @author Daniel
	 *
	 */
	private class SMTPAuthenticator extends Authenticator {
		private String username;
		private String password;

		public SMTPAuthenticator(String user, String pass) {
			this.username = user;
			this.password = pass;
		}

		public PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(this.username, this.password);
		}
	}

	/**
	 * Mtodo para configurar o email
	 * 
	 * TODO melhorar ou sobreescrever com melhores opes tipo autenticao
	 * 
	 * @param porta
	 * @param servidor
	 * @throws PlcException
	 */
	public void configurar(String porta, String servidor) throws Exception {
		configuracoes.put("mail.smtp.host", servidor);
		configuracoes.put("mail.smtp.ehlo", "true");

		if (porta != null)
			configuracoes.put("mail.smtp.port", porta);
	}

	/**
	 * Mtodo utilizado para configurar a autenticao dessa classe de envio de email,
	 * sempre chamar esse mtodo caso seja necessrio a autenticao
	 * 
	 * @param usuario
	 * @param senha
	 * @throws PlcException
	 */
	public void configurarAutenticacao(String usuario, String senha) throws Exception {
		username = usuario;
		password = senha;

		auth = new SMTPAuthenticator(usuario, senha);

		configuracoes.put("mail.smtp.auth", "true");
	}

	/**
	 * Mtodo para enviar email para os destinatrios citados sem anexo
	 * 
	 * @param remetente     rementente do email a ser enviado
	 * @param destinatarios lista de destinatrios separados por ;
	 * @param assunto       assunto do email a ser enviado, sem isso, provavelmente
	 *                      cai em lista de spam
	 * @param corpo         corpo do email com encoding setado, a mensagem monstada
	 *                      aqui sempre em formato html
	 * 
	 * @deprecated Utilize o mtodo enviaEmail com anexo
	 * 
	 * @throws PlcException
	 * 
	 */
	public void enviaEmail(String tituloRemetente, String remetente, String destinatarios, String assunto, String corpo)
			throws Exception {
		enviaEmail(tituloRemetente, remetente, destinatarios, assunto, corpo, null);
	}

	/**
	 * Metodo utilizado para enviar email que tenha anexos, e esses anexos sao
	 * recuperados diretamente do banco
	 * 
	 * @param remetente         rementente do email a ser enviado
	 * @param destinatarios     lista de destinatarios separados por ;
	 * @param assunto           assunto do email a ser enviado, sem isso,
	 *                          provavelmente cai em lista de spam
	 * @param corpo             corpo do email com encoding setado, a mensagem e
	 *                          monstada aqui sempre em formato html
	 * @param listaByteArquivos lista com Arquivo contendo mime type, byte array do
	 *                          arquivo preenchido e nome
	 * 
	 * @throws PlcException
	 */
	public void enviaEmailAnexoEmMemoria(String tituloRemetente, String remetente, String destinatarios, String assunto,
			String corpo, List listaArquivos) throws Exception {
		enviaEmail(tituloRemetente, remetente, destinatarios, assunto, corpo, listaArquivos);
	}

	/**
	 * Metodo para enviar email para os destinatarios citados
	 * 
	 * @param remetente     rementente do email a ser enviado
	 * @param destinatarios lista de destinatarios separados por ;
	 * @param assunto       assunto do email a ser enviado, sem isso, provavelmente
	 *                      cai em lista de spam
	 * @param corpo         corpo do email com encoding setado, a mensagem e
	 *                      monstada aqui sempre em formato html
	 * @param listaArquivos lista em string do nome dos arquivos, passar o nome
	 *                      qualificado inteiro
	 * 
	 * @throws PlcException
	 */
	public void enviaEmail(String tituloRemetente, String remetente, String destinatarios, String assunto, String corpo,
			List listaArquivos) throws Exception {
		try {

			Session session = Session.getInstance(configuracoes, auth);

			// so habilita debug se e somente se o log4j tambem estiver
			// habilitado
			if (logger.isDebugEnabled())
				session.setDebug(true);

			// inicia montagem de email com, remetente, destinatarios, assunto e
			// corpo do email
			MimeMessage mimeMessage = new MimeMessage(session);

			Multipart multipart = new MimeMultipart();

			mimeMessage.setFrom(new InternetAddress(remetente, tituloRemetente));

			mimeMessage.setRecipients(Message.RecipientType.TO, destinatarios);

			mimeMessage.setSubject(MimeUtility.encodeText(assunto, "UTF-8", "B"));

			mimeMessage.setSentDate(new Date());

			/**
			 * adiciona corpo de email formato html
			 */
			MimeBodyPart mimeBodyPart = new MimeBodyPart();

			mimeBodyPart.setContent(corpo, "text/html; charset=utf-8");

			// adiciona corpo email
			multipart.addBodyPart(mimeBodyPart, 0);

			// se existe arquivo busca ele no disco e guarda como anexo
			if (listaArquivos != null && listaArquivos.size() > 0) {
				mimeBodyPart = new MimeBodyPart();
			}

			// mensagem pronta para ser enviada, preenchida com todos os
			// parametros necessarios para envio de email
			mimeMessage.setContent(multipart);

			// envio de email
			Transport.send(mimeMessage);

		} catch (AddressException e) {
			logger.debug(e);

			throw new Exception("erro.envio.email", e);

		} catch (MessagingException m) {
			logger.debug(m);
			// TODO REFACTOR
			if (m.getLocalizedMessage() != null && m.getLocalizedMessage().equals("Invalid Addresses"))
				throw new Exception("erro.envio.email.invalido");

			else if (m.getLocalizedMessage() != null && m.getLocalizedMessage().equals("No recipient addresses"))
				throw new Exception("erro.envio.email.nao.existe.destinatario");

			throw new Exception("erro.envio.email", m);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public boolean enviaEmail(HttpServletRequest request, String corpo, String destinatario, String remetente,
			String titulo, String identificacao) {

		try {
			configurar(emailSmtpPorta, emailSmtpHost);
			configurarAutenticacao(emailUser, emailPassword);
			enviaEmail(titulo, remetente, destinatario, identificacao, corpo, null);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	public boolean enviaEmailComImagem(HttpServletRequest request, String corpo, String destinatario, String remetente,
			String titulo, String identificacao, MimeMultipart mimeMultipart) {
		String servidor = request.getSession().getServletContext().getInitParameter("mail.smtp.host");
		String porta = request.getSession().getServletContext().getInitParameter("mail.smtp.porta");
		String user = request.getSession().getServletContext().getInitParameter("mail.user");
		String password = request.getSession().getServletContext().getInitParameter("mail.password");
		try {
			// enviando
			System.out.println("Enviando email...");

			Session mailSession = createSmtpSession(servidor, porta, user, password);
			mailSession.setDebug(true);

			Transport transport = mailSession.getTransport("smtps");

			MimeMessage message = new MimeMessage(mailSession);
			message.setSubject(titulo);
			message.setFrom(new InternetAddress(remetente));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress("rodrigocbass@gmail.com"));
			// message.addRecipient(Message.RecipientType.TO, new
			// InternetAddress("brunoif@pbh.gov.br"));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress("leandro.av@gmail.com"));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress("bfbfc@yahoo.com.br"));

			// coloca tudo junto
			message.setContent(mimeMultipart);

			transport.connect(servidor, porta, user);
			transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
			transport.close();

			return true;
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;

	}

	private Session createSmtpSession(String host, String porta, String usuario, String senha) {

		final Properties props = new Properties();
		props.setProperty("mail.smtp.host", host);
		props.setProperty("mail.smtp.auth", "true");
		props.setProperty("mail.smtp.port", porta);
		props.setProperty("mail.smtp.starttls.enable", "true");
		// props.setProperty("mail.debug", "true");

		return Session.getDefaultInstance(props, new javax.mail.Authenticator() {

			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(usuario, senha);
			}
		});
	}
}