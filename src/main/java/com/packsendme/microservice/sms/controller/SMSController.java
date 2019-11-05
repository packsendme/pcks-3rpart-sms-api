package com.packsendme.microservice.sms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.packsendme.lib.common.constants.HttpExceptionPackSend;
import com.packsendme.lib.common.response.Response;
import com.packsendme.microservice.sms.service.SMSCacheService;

@RestController
public class SMSController {
	
	@Autowired
	private SMSCacheService smsService;
	
	
	//** BEGIN OPERATION: USER FIRST ACCESS *************************************************//
	
	
	@PostMapping("/sms/{username}")
	public ResponseEntity<?> generatorSMSCode(@Validated @PathVariable("username") String username) throws Exception {
		return smsService.createSMSCode(username);
	}

	@GetMapping("/sms/{username}")
	public ResponseEntity<?> validateSMSCode(@Validated @PathVariable("username") String username, 
			@Validated @PathVariable("smscode") String smscode) throws Exception {
		return smsService.findSMSCode(username,smscode);
	}
	
	
	@DeleteMapping("/sms/{username}")
	public ResponseEntity<?> deleteSMSCode(@Validated @PathVariable("username") String username, 
			@Validated @PathVariable("smscode") String smscode) throws Exception {
		return smsService.deleteSMSCode(username,smscode);
	}
}
