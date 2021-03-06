package com.example.demo;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.example.demo.ahoCorasickAlgorithm.AhoCorasick;
import com.example.demo.beans.News;

@Component
public class ApiConsumer {

	public RestTemplate restTemplate = new RestTemplate();
	@Autowired
	RulesConditionsCheck rulesConditionsCheck;

	@Value("${api.url}")
	public String url;
	@Value("${api.count}")
	public int count;
	@Value("${api.pageSize}")
	public int pageSize;

	public void retrieveNewsFromApi(AhoCorasick ahoCorasick) {
		RestTemplate restTemplate = new RestTemplate();
		for(int i=0; i < count/pageSize; i++) {
			Map<String, String> params = new HashMap<String, String>();
			params.put("_page", i+"");
			params.put("_limit", pageSize+"");

			ResponseEntity<News[]> response = restTemplate.getForEntity(url+ "?_page="+ i+ "&_limit="+ pageSize,
					News[].class, params);
			News[] newsArr = response.getBody();
			
			rulesConditionsCheck.check(newsArr, ahoCorasick);
			
			
		}	

	}

}
