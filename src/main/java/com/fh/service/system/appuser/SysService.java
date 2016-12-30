package com.fh.service.system.appuser;

import javax.annotation.Resource;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

@Service("SysService")
public class SysService {
//	@Resource(name = "pushNotificationService")
//	private PushNotificationService ps;
//	
//	@Resource(name = "emailService")
//	private EmailService emailService;
	
	@Resource(name = "threadsPool")
	private ThreadPoolTaskExecutor threadsPool;
	
	
	//threadsPool

}
