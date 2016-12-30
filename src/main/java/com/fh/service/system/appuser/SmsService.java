package com.fh.service.system.appuser;
import org.springframework.stereotype.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;



@Service("smsService")
public class SmsService {
  // Find your Account Sid and Token at twilio.com/user/account
  public static final String ACCOUNT_SID = "AC2454168be8ba0cd3b5ee549aee80073b";
  public static final String AUTH_TOKEN = "6216791c2bb1f2e6db2cf5d7f6b4dea3";
  
  
 public SmsService(){
	  Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
 }
 /**
  * 
  * @param phone
  * @param content
  */
  public void sendMessage(String phone,String content){
	  Message message = Message.creator(new PhoneNumber(phone), new PhoneNumber(phone),
			  content).create();
  }

//  public static void main(String[] args) {
//    //Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
//
//    Message message = Message
//        .creator(new PhoneNumber("+8618665016206"), new PhoneNumber("+640212923526"),
//            "This is test message ?")
//      //  .setMediaUrl("https://c1.staticflickr.com/3/2899/14341091933_1e92e62d12_b.jpg")
//        .create();
//
//    System.out.println(message.getSid());
//  }
}