package br.com.quub.utils;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;

@Component
public class EmailHelper {

	private SendGrid sendGrid;
	
	@Value("${spring.sendgrid.api-key}")
	String sendGridKey;

	public EmailHelper(SendGrid sendGrid) {
		this.sendGrid = sendGrid;
	}

	void sendMail(String emailTO) {
		Email from = new Email(sendGridKey);
		String subject = "Email enviado por sendgrid";
		Email to = new Email(emailTO);
		Content content = new Content("text/plain", "Bem vindo ao chat");
		Mail mail = new Mail(from, subject, to, content);

		Request request = new Request();
		try {
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());
			Response response = this.sendGrid.api(request);
			sendGrid.api(request);

		} catch (IOException ex) {

		}
	}
}
