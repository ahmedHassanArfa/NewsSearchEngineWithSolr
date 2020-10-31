package com.example.demo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

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

	public void retrieveNewsFromApi() {
		RestTemplate restTemplate = new RestTemplate();
		for(int i=0; i < count/pageSize; i++) {
			Map<String, String> params = new HashMap<String, String>();
			params.put("_page", i+"");
			params.put("_limit", pageSize+"");
			
//			ResponseEntity<List<News>> rateResponse =
//			        restTemplate.exchange(url+ "?_page="+ 0+ "&_limit="+ 10,
//			                    HttpMethod.GET, null, new ParameterizedTypeReference<List<News>>() {
//								});
//			List<News> rates = rateResponse.getBody();

			
			ResponseEntity<Object[]> response = restTemplate.getForEntity(url+ "?_page="+ 0+ "&_limit="+ 10,
					Object[].class, params);
			Object[] newsArr = response.getBody();
			
			
			for(Object news: newsArr) {
				rulesConditionsCheck.check((News) news);
				
			}
			
			
		}

	}

}
