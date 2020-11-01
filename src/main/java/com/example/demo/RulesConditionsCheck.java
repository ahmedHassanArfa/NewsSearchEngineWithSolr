package com.example.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.ahoCorasickAlgorithm.AhoCorasick;
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

	public void check(News[] newsArray, AhoCorasick ahoCorasick) {
		Map<String, List<Output>> map = new HashMap<String, List<Output>>();
		for (News news : newsArray) {
			Output output = mapNewsToOutput(news);
			// first check this text for keyword with aho-corasick for all ruleset at first
			int node = 0;
			String outputENChar = Utils.clearTurkishChars(output.getText().replace(" ", "").toLowerCase());
			for (char ch : outputENChar.toCharArray())
	        {
	            node = ahoCorasick.transition(node, ch);
	        }
	        if (!ahoCorasick.nodes[node].leaf) {
	        	continue;
	        }
			
			
			ruleSetLoop: for (RuleSet ruleSet : ruleSetsProperties.getRuleSets()) {
				if (map.get(ruleSet.getRuleset_Name()) == null) {
					map.put(ruleSet.getRuleset_Name(), new ArrayList<Output>());
				}
				boolean passCategory = false;
				boolean passTags = false;
				boolean passLang = false;
				boolean passName = false;
				boolean passKeywords = false;
				for (Rule rule : ruleSet.getRules()) {
					// if one rule success this document is accepted
					if (rule.getCategories() != null) {
						if (rule.getCategories().contains(",")) {
							String[] categArr = rule.getCategories().split(",");
							categLoop: for (String categ : categArr) {
								if (news.getCategories().contains(categ)) {
									passCategory = true;
									break categLoop;
								}
							}
						} else {
							if (news.getCategories().contains(rule.getCategories())) {
								passCategory = true;
							}
						}
					} else {
						passCategory = true;
					}

					if (rule.getTags() != null) {
						if (rule.getTags().contains(",")) {
							String[] tagsArr = rule.getTags().split(",");
							tagLoop: for (String tag : tagsArr) {
								if (news.getTags().contains(tag)) {
									passTags = true;
									break tagLoop;
								}
							}
						} else {
							if (news.getTags().contains(rule.getTags())) {
								passTags = true;
							}
						}
					}

					if (rule.getLang() != null) {
						if (rule.getLang().contains(",")) {
							String[] langArr = rule.getLang().split(",");
							langLoop: for (String lang : langArr) {
								if (news.getLang().equals(lang)) {
									passLang = true;
									break langLoop;
								}
							}
						} else {
							if (news.getLang().equals(rule.getTags())) {
								passTags = true;
							}
						}
					}

					if (rule.getName() != null) {
						if (rule.getName().contains(",")) {
							String[] nameArr = rule.getName().split(",");
							nameLoop: for (String name : nameArr) {
								if (news.getName().equals(name)) {
									passName = true;
									break nameLoop;
								}
							}
						} else {
							if (news.getName().equals(rule.getName())) {
								passName = true;
							}
						}
					}
					
					// check keywords
					AhoCorasick ahoCorasickForRule = new AhoCorasick(1000);
					String[] keywords = rule.getKeywords().split(",");
					for(String keyword : keywords) {
						keyword = Utils.clearTurkishChars(keyword);
						ahoCorasickForRule.addString(keyword.replace(" ", "").toLowerCase());
						int nodeRule = 0;
						String outputENCharRule = Utils.clearTurkishChars(output.getText().replace(" ", "").toLowerCase());
						for (char ch : outputENCharRule.toCharArray())
				        {
							nodeRule = ahoCorasickForRule.transition(nodeRule, ch);
				        }
				        if (!ahoCorasickForRule.nodes[node].leaf) {
				        	passKeywords = false;
				        	break;
				        }
					}
					

					if (passCategory && passLang && passName && passTags && passKeywords) {
						// if success then
						map.get(ruleSet.getRuleset_Name()).add(output);
						break ruleSetLoop;
					}

				}

			}
		}

		for (Entry<String, List<Output>> entry : map.entrySet()) {
			// save in solr and excel file
			solrOperations.indexingInputDocuments(entry.getValue());
			// every ruleset has it's excel sheets
			ExcelWriting.writeToExcel(entry.getValue(), entry.getKey());
		}
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
		output.setTitle(news.getTitle());
		output.setType(news.getType());

		// (making lowercase, trimming redundant spaces,
		// removing punctuations, and removing stopwords by language)
		output.setText(Utils.normalizeTurkishText(news.getTitle() + news.getDescription() + news.getContent()));
		return output;
	}

}
