package com.fh.service.system.appuser;

import org.springframework.stereotype.Service;

import com.fh.entity.PushMessageBean;
import com.fh.entity.UserEntity;
import com.google.firebase.FirebaseOptions;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

@Service("pushNotificationService")
public class PushNotificationService {

	FirebaseOptions options = null;

	/**
	 * 
	 * @throws UnirestException
	 */
	public void pushMessage() throws UnirestException {
		HttpResponse<JsonNode> response = Unirest.post("https://fcm.googleapis.com/fcm/send")
				.basicAuth("key", "0GBYzPu7Udno5aA").asJson();

		System.out.println(response.getBody().getArray().toString());

	}
/**
 * 1.zoning_requests
 * 2.
 * @param message
 * @throws UnirestException
 */
	public void pushNotifiction(PushMessageBean message) throws UnirestException {

		HttpResponse<JsonNode> response = Unirest.post("https://fcm.googleapis.com/fcm/send")
				.basicAuth("key", "0GBYzPu7Udno5aA").asJson();

		System.out.println(response.getBody().getArray().toString());

		// push message

	}

}
