package com.example.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.beans.News;
import com.example.demo.beans.Output;
import com.example.demo.beans.Rule;
import com.example.demo.beans.RuleSet;

@Service
public class RulesConditionsCheck {

	@Autowired
	RuleSetsProperties ruleSetsProperties;

	@Autowired
	SolrOperations solrOperations;

	public boolean check(News[] newsArray) {

		// (making lowercase, trimming redundant spaces,
		// removing punctuations, and removing stopwords by language)

		Map<String, List<Output>> map = new HashMap<String, List<Output>>();
		List<Output> outputs = new ArrayList<Output>();
		for (News news : newsArray) {
ruleSetLoop:for (RuleSet ruleSet : ruleSetsProperties.getRuleSets()) {
				
				// every ruleset has it's excel sheets
				for (Rule rule : ruleSet.getRules()) {
					// if one rule success this document is accepted
					
					// if success then
					Output output = mapNewsToOutput(news);
					outputs.add(output);
					map.put(ruleSet.getRuleset_Name(), outputs);
					break ruleSetLoop;
					
				}


			}
		}
		
		// save in solr and excel file
		solrOperations.indexingInputDocuments(outputs);
		ExcelWriting.writeToExcel(outputs, "sfsgfs");

		int node = 0;
//        for (char ch : main.toCharArray())
//        {
//            node = ahoCorasick.transition(node, ch);
//        }
//        if (ahoCorasick.nodes[node].leaf)
//            System.out.println("A '" + pattern + "' string is substring of "
//                    + main + " string.");
//        else
//            System.out.println("A '" + pattern
//                    + "' string is not substring of " + main + " string.");
		return false;
	}

	public static Output mapNewsToOutput(News news) {
		Output output = new Output();
		output.setId(news.getId());
		output.setUrl(news.getUrl());
		output.setContent(news.getContent());
		output.setCrawl_date(news.getCrawl_date());
		output.setDescription(news.getDescription());
		output.setLang(news.getLang());
		output.setModified_date(news.getModified_date());
		output.setName(news.getName());
		output.setPublished_date(news.getPublished_date());
		output.setRules(new ArrayList<String>());
		output.setCategories(news.getCategories());
		output.setTags(news.getTags());
		output.setText(news.getContent());
		output.setTitle(news.getTitle());
		output.setType(news.getType());
		return output;
	}

}
