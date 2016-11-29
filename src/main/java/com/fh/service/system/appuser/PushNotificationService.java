package com.fh.service.system.appuser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.springframework.stereotype.Service;

import com.fh.util.Const;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

@Service("pushNotificationService")
public class PushNotificationService {

	FirebaseOptions options = null;
	//

	PushNotificationService() throws FileNotFoundException {

		options = new FirebaseOptions.Builder().setServiceAccount(new FileInputStream(Const.jsonKey))
				.setDatabaseUrl("https://sosxsos-c0363.firebaseio.com/").build();

		FirebaseApp.initializeApp(options);
	}
   
//	
//	https://fcm.googleapis.com/fcm/send
//		Content-Type:application/json
//		Authorization:key=AIzaSyZ-1u...0GBYzPu7Udno5aA
//
//		{ "data": {
//		    "score": "5x1",
//		    "time": "15:10"
//		  },
//		  "to" : "bk3RNwTe3H0:CI2k_HHwgIpoDKCIZvvDMExUdFQ3P1..."
//		}
	/**
	 * 
	 * @throws UnirestException
	 */
	public void pushMessage() throws UnirestException {
		HttpResponse<JsonNode> response=	Unirest.post("https://fcm.googleapis.com/fcm/send").basicAuth("key", "0GBYzPu7Udno5aA")
		  .asJson();
		
		System.out.println(response.getBody().getArray().toString());
		
		
	}
	
	
	public void operateFireBase() {
		// As an admin, the app has access to read and write all data,
		// regardless of Security Rules
		DatabaseReference ref = FirebaseDatabase.getInstance().getReference("restricted_access/secret_document");
		
		
		
		
		
		ref.addListenerForSingleValueEvent(new ValueEventListener() {

			public void onDataChange(DataSnapshot dataSnapshot) {
				Object document = dataSnapshot.getValue();
				System.out.println(document);
			}

			public void onCancelled(DatabaseError arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

}
