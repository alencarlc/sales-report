package com.company.salesreport.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class FileService {
	
	@Autowired
    private RedisTemplate<String, String> redisTemplate;
	
	public boolean isFileProcessed(String fileName) {
		return this.redisTemplate.hasKey(fileName);
	}
	
	public void markFileAsProcessed(String fileName) {
		this.redisTemplate.opsForValue().set(fileName, "p");;
	}
	
	public void markFileAsFinished(String fileName) {
		this.redisTemplate.expire(fileName, 1, TimeUnit.MINUTES);
	}

}
