package com.fh.service.system.appuser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PushNotificationService {

	FirebaseOptions options = null;

	PushNotificationService() throws FileNotFoundException {

		options = new FirebaseOptions.Builder()
				.setServiceAccount(new FileInputStream("path/to/serviceAccountCredentials.json"))
				.setDatabaseUrl("https://databaseName.firebaseio.com/").build();

		FirebaseApp.initializeApp(options);
	}
	
	public void operateFireBase(){
		// As an admin, the app has access to read and write all data, regardless of Security Rules
		DatabaseReference ref = FirebaseDatabase
		    .getInstance()
		    .getReference("restricted_access/secret_document");
		ref.addListenerForSingleValueEvent(new ValueEventListener() {
		    @Override
		    public void onDataChange(DataSnapshot dataSnapshot) {
		        Object document = dataSnapshot.getValue();
		        System.out.println(document);
		    }

			@Override
			public void onCancelled(DatabaseError arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	

}
