package com.fh.service.system.appuser;


import java.io.File;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;

public class EmailService {
private static	String domain="mail.sosxsos.com";
	
	public static ClientResponse SendSimpleMessage() {
	       Client client = Client.create();
	       client.addFilter(new HTTPBasicAuthFilter("api",
	                       "YOUR_API_KEY"));
	       WebResource webResource =
	               client.resource("https://api.mailgun.net/v3/"+domain +
	                               "/messages");
	       MultivaluedMapImpl formData = new MultivaluedMapImpl();
	       formData.add("from", "Excited User <mailgun@mail.sosxsos.com>");
	       formData.add("to", "bar@example.com");
	       formData.add("to", "YOU@YOUR_DOMAIN_NAME");
	       formData.add("subject", "Hello");
	       formData.add("text", "Testing some Mailgun awesomness!");
	       return webResource.type(MediaType.APPLICATION_FORM_URLENCODED).
	               post(ClientResponse.class, formData);
	}
	/**
	 * 
	 * @return
	 */
	public static ClientResponse SendComplexMessage() {
	       Client client = Client.create();
	       client.addFilter(new HTTPBasicAuthFilter("api",
	                       "YOUR_API_KEY"));
	       WebResource webResource =
	               client.resource("https://api.mailgun.net/v3/"+domain +
	                               "/messages");
	       FormDataMultiPart form = new FormDataMultiPart();
	       form.field("from", "Excited User <YOU@YOUR_DOMAIN_NAME>");
	       form.field("to", "foo@example.com");
	       form.field("bcc", "bar@example.com");
	       form.field("cc", "baz@example.com");
	       form.field("subject", "Hello");
	       form.field("text", "Testing some Mailgun awesomness!");
	       String file_separator = System.getProperty("file.separator");
	       File txtFile = new File("." + file_separator +
	                       "files" + file_separator + "test.txt");
	       form.bodyPart(new FileDataBodyPart("attachment",txtFile,
	                       MediaType.TEXT_PLAIN_TYPE));
	       File jpgFile = new File("." + file_separator +
	                       "files" + file_separator + "test.jpg");
	       form.bodyPart(new FileDataBodyPart("attachment",jpgFile,
	                       MediaType.APPLICATION_OCTET_STREAM_TYPE));
	       return webResource.type(MediaType.MULTIPART_FORM_DATA_TYPE).
	               post(ClientResponse.class, form);
	}

	
	/**
	 * 
	 * @return
	 */
	public static ClientResponse SendMimeMessage() {
	       Client client = Client.create();
	       client.addFilter(new HTTPBasicAuthFilter("api",
	                       "YOUR_API_KEY"));
	       WebResource webResource =
	               client.resource("https://api.mailgun.net/v3/"+domain +
	                               "/messages.mime");
	       FormDataMultiPart form = new FormDataMultiPart();
	       form.field("to", "bar@example.com");
	       String file_separator = System.getProperty("file.separator");
	       File mimeFile = new File("." + file_separator + "files" +
	                       file_separator + "message.mime");
	       form.bodyPart(new FileDataBodyPart("message", mimeFile,
	                       MediaType.APPLICATION_OCTET_STREAM_TYPE));
	       return webResource.type(MediaType.MULTIPART_FORM_DATA_TYPE).
	               post(ClientResponse.class, form);
	}
}
