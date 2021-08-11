package com.packsendme.microservice.sms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.packsendme.microservice.sms.service.SMSCacheService;

@RestController("/3rpart/sms")
public class SMSController {
	
	@Autowired
	private SMSCacheService smsService;
	
	
	//** BEGIN OPERATION: USER FIRST ACCESS *************************************************//
	
	
	@GetMapping("/{username}")
	public ResponseEntity<?> generatorSMSCode(@Validated @PathVariable("username") String username) throws Exception {
		return smsService.createSMSCode(username);
	}

	@GetMapping("/{username}/{smscode}")
	public ResponseEntity<?> validateSMSCode(@Validated @PathVariable("username") String username, 
			@Validated @PathVariable("smscode") String smscode) throws Exception {
		return smsService.findSMSCode(username,smscode);
	}
	
	
	@DeleteMapping("/{username}/{smscode}")
	public ResponseEntity<?> deleteSMSCode(@Validated @PathVariable("username") String username, 
			@Validated @PathVariable("smscode") String smscode) throws Exception {
		return smsService.deleteSMSCode(username,smscode);
	}
}
