package com.fh.service.system.appuser;

import java.io.File;
import java.util.List;

import javax.ws.rs.core.MediaType;
import org.springframework.stereotype.Service;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;
@Service("emailService")
public class EmailService {
	private static String domain = "mail.sosxsos.com";
	private static String pubkey = "pubkey-cdaaaccad370f3134b14933dcfa4a260";
	private static String key = "key-29af45b614e8a3a330f3af36d1e6ac58";

	/**
	 * 
	 * @return
	 */
	public  ClientResponse SendSimpleMessage(List<String> toMails, String content, String subject) {
		Client client = Client.create();
		client.addFilter(new HTTPBasicAuthFilter("api", key));
		WebResource webResource = client.resource("https://api.mailgun.net/v3/" + domain + "/messages");
		MultivaluedMapImpl formData = new MultivaluedMapImpl();
		formData.add("from", "Excited User <mailgun@mail.sosxsos.com>");
		for (String mail : toMails) {
			formData.add("to", mail);
		}

		formData.add("subject", subject);
		formData.add("text", content);
		return webResource.type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData);
	}

	/**
	 * 
	 * @return
	 */
	public static ClientResponse SendComplexMessage() {
		Client client = Client.create();
		client.addFilter(new HTTPBasicAuthFilter("api", "YOUR_API_KEY"));
		WebResource webResource = client.resource("https://api.mailgun.net/v3/" + domain + "/messages");
		FormDataMultiPart form = new FormDataMultiPart();
		form.field("from", "Excited User <YOU@YOUR_DOMAIN_NAME>");
		form.field("to", "foo@example.com");
		form.field("bcc", "bar@example.com");
		form.field("cc", "baz@example.com");
		form.field("subject", "Hello");
		form.field("text", "Testing some Mailgun awesomness!");
		String file_separator = System.getProperty("file.separator");
		File txtFile = new File("." + file_separator + "files" + file_separator + "test.txt");
		form.bodyPart(new FileDataBodyPart("attachment", txtFile, MediaType.TEXT_PLAIN_TYPE));
		File jpgFile = new File("." + file_separator + "files" + file_separator + "test.jpg");
		form.bodyPart(new FileDataBodyPart("attachment", jpgFile, MediaType.APPLICATION_OCTET_STREAM_TYPE));
		return webResource.type(MediaType.MULTIPART_FORM_DATA_TYPE).post(ClientResponse.class, form);
	}

	/**
	 * 
	 * @return
	 */
	public static ClientResponse SendMimeMessage() {
		Client client = Client.create();
		client.addFilter(new HTTPBasicAuthFilter("api", "YOUR_API_KEY"));
		WebResource webResource = client.resource("https://api.mailgun.net/v3/" + domain + "/messages.mime");
		FormDataMultiPart form = new FormDataMultiPart();
		form.field("to", "bar@example.com");
		String file_separator = System.getProperty("file.separator");
		File mimeFile = new File("." + file_separator + "files" + file_separator + "message.mime");
		form.bodyPart(new FileDataBodyPart("message", mimeFile, MediaType.APPLICATION_OCTET_STREAM_TYPE));
		return webResource.type(MediaType.MULTIPART_FORM_DATA_TYPE).post(ClientResponse.class, form);
	}
}
