package com.demo.testmail.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.testmail.service.AzureOpenAIService;


@RestController
public class AzureOpenAIController {
	
	@Autowired
	AzureOpenAIService azureOpenAIService;
	
	@PostMapping("/chat")
	public String generateText(@RequestParam String prompt)
	{
		return azureOpenAIService.generateText(prompt);
	}
	
	@PostMapping("/textTranslate")
	public String translateText(@RequestBody Map<String,String> reqMap)
	{
		String text = reqMap.get("text");
		String language =reqMap.get("targetLanguage");
		return azureOpenAIService.translateText(text,language);
	}

}
