package com.packsendme.microservice.sms.service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.packsendme.lib.common.constants.HttpExceptionPackSend;
import com.packsendme.lib.common.response.Response;
import com.packsendme.microservice.sms.component.GeneratorSMSCode;
import com.packsendme.microservice.sms.dto.SMSDto;


@Service
@CacheConfig(cacheNames = "SMSCache")
public class SMSCacheService {

	private static Map<String, SMSDto> storeSMS = new HashMap<String, SMSDto>();
    
    @Autowired
    private GeneratorSMSCode generatorSMSObj;
    
    private Response<SMSDto> responseSMS = null;
    
    private SMSDto smsObj = null;
    
    
	public ResponseEntity<?> createSMSCode(String username) throws Exception {
		String smscode = generatorSMSObj.generateSMSCode();
		String smscodeFormat = username+smscode;
		
		try{
			smsObj = createSMSCodeCache(smscodeFormat);
			if(smsObj != null) {
				// CALL SEND SMS TO CUSTOMER PHONE
				responseSMS = new Response<SMSDto>(0,HttpExceptionPackSend.GENERATOR_SMSCODE.getAction(), smsObj); 
				return new ResponseEntity<>(responseSMS, HttpStatus.ACCEPTED);
			}
			else {
				responseSMS = new Response<SMSDto>(0,HttpExceptionPackSend.GENERATOR_SMSCODE.getAction(), smsObj); 
				return new ResponseEntity<>(responseSMS, HttpStatus.NOT_FOUND);
			}
		}
		catch(Exception e){
			responseSMS = new Response<SMSDto>(0,HttpExceptionPackSend.FAIL_EXECUTION.getAction(), smsObj); 
			return new ResponseEntity<>(responseSMS, HttpStatus.INTERNAL_SERVER_ERROR);
		}
 
	}
	
	@Cacheable(value="SMSCache", key="#smscode")    
	public SMSDto createSMSCodeCache(String smscode) throws Exception {
		Timestamp timeCreate = new Timestamp(System.currentTimeMillis());
		System.out.println("-----------------------------------------");
		System.out.println("CreateCache--Username+SMS :: "+ smscode);
        System.out.println("CreateCache-Username HOURS/MINUTES :: "+ timeCreate.getHours() +" "+timeCreate.getMinutes());

		try{
			System.out.println("CreateCache-- Creating :: ");
            Thread.sleep(1000); 
            storeSMS.put(smscode,new SMSDto(smscode, timeCreate.getTime()));
			smsObj = storeSMS.get(smscode);
			System.out.println("CreateCache-Username ...:: OK :: smsCode "+ smscode);
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return smsObj;
	}
	
	public ResponseEntity<?> findSMSCode(String username, String smscode) throws Exception {
		try{
			String smscodeFormat = username+smscode;
			smsObj = findSMSCodeCache(smscodeFormat);
			
			if(smsObj != null) {
				responseSMS = new Response<SMSDto>(0,HttpExceptionPackSend.FOUND_SMS_CODE.getAction(), smsObj); 
				return new ResponseEntity<>(responseSMS, HttpStatus.FOUND);
			}
			else {
				responseSMS = new Response<SMSDto>(0,HttpExceptionPackSend.FOUND_SMS_CODE.getAction(), smsObj); 
				return new ResponseEntity<>(responseSMS, HttpStatus.NOT_FOUND);
			}
		
		}
		catch (Exception e) {
			responseSMS = new Response<SMSDto>(0,HttpExceptionPackSend.FAIL_EXECUTION.getAction(), smsObj); 
			return new ResponseEntity<>(responseSMS, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	@Cacheable(value="SMSCache", key="#smscode")   
	public SMSDto findSMSCodeCache(String smscode) throws Exception {
		SMSDto smsObj = null;
		try{
	    	System.out.println("-----------------------------------------------------------");
	    	System.out.println("find...:: USERNAME_NEW :: "+ smscode);
	    	System.out.println("-----------------------------------------------------------");
			Thread.sleep(1000); 
	     
			smsObj = storeSMS.get(smscode);
			if(smsObj != null) {
				if(smsObj.getSmsCodeUsername().equals(smscode)) {
					System.out.println("Result FIND  ...:: FOUND:: ");
					return smsObj;
				}
				else {
					System.out.println("Result FIND  ...:: 	NOT-FOUND:: "+ smsObj.getSmsCodeUsername());
					return null;
				}
			}
			else{
			    System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
				System.out.println("Result FIND  ...:: NOT-FOUND:: ");
			    System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
				return null;
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	@Scheduled(fixedRate = 1000)
	public void checkCacheDelete(){
		Timestamp timestampCache = new Timestamp(System.currentTimeMillis());
    	Iterator<Map.Entry<String, SMSDto>> itr = storeSMS.entrySet().iterator();
 
    	while(itr.hasNext())
    	{
    	   Map.Entry<String, SMSDto> entry = itr.next();
    	   SMSDto smsObj = entry.getValue();
    	   
    	   long milliseconds = timestampCache.getTime() - smsObj.getTimeCreate();
       	   int seconds = (int) milliseconds / 1000;
       	   int minutes = (seconds % 3600) / 60;
       	   
       	   if(minutes >= 1) {
       		   System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++");
       		   System.out.println("CHECK CACHE DELETE -- "+ minutes);
       		   System.out.println("checkCacheDelete-UsernameSMS ::"+ smsObj.getSmsCodeUsername());
       		   System.out.println("checkCacheDelete-Username HOURS/MINUTES :: "+ timestampCache.getHours() +" "+timestampCache.getMinutes());
       		   System.out.println("checkCacheDelete-Minutes "+ minutes);
       		   storeSMS.remove(itr);
       		  // itr.remove();
       		   deleteSMSCache(smsObj.getSmsCodeUsername());
       		   System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++");
       	   }
    	}
    }

	public ResponseEntity<?> deleteSMSCode(String username,String smscode){
		try{
			String smscodeFormat = username+smscode;
			smsObj = deleteSMSCache(smscodeFormat);
			
			if(smsObj == null) {
				responseSMS = new Response<SMSDto>(0,HttpExceptionPackSend.DELETE_SMSCODE.getAction(), smsObj); 
				return new ResponseEntity<>(responseSMS, HttpStatus.ACCEPTED);
			}
			else {
				responseSMS = new Response<SMSDto>(0,HttpExceptionPackSend.DELETE_SMSCODE.getAction(), smsObj); 
				return new ResponseEntity<>(responseSMS, HttpStatus.FOUND);
			}
		
		}
		catch (Exception e) {
			responseSMS = new Response<SMSDto>(0,HttpExceptionPackSend.DELETE_SMSCODE.getAction(), smsObj); 
			return new ResponseEntity<>(responseSMS, HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
	
    @CacheEvict(value="SMSCache",key="#smscode")   
	public SMSDto deleteSMSCache(String smscode){
		SMSDto smsObj = null;
		
    	try{
    		storeSMS.remove(smscode);
    		smsObj = storeSMS.get(smscode);
    		return smsObj;
    	}
    	catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }
    		
}
