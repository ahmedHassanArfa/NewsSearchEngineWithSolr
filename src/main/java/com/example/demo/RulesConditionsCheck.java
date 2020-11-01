package com.example.demo;

import java.util.ArrayList;
import java.util.Arrays;
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
		newsLoop: for (News news : newsArray) {
			Output output = mapNewsToOutput(news);
			// first check this text for keyword with aho-corasick for all ruleset at first
			int node = 0;
			String outputENChar = Utils.clearTurkishChars(output.getText()).replace(" ", "").toLowerCase();
			boolean found = false;
			for (char ch : outputENChar.toCharArray()) {
				node = ahoCorasick.transition(node, ch);
				if (ahoCorasick.nodes[node].leaf) {
					found = true;
					break;
				}
			}
			if(!found) {
				continue newsLoop;
			}

			ruleSetLoop: for (RuleSet ruleSet : ruleSetsProperties.getRuleSets()) {
				if (map.get(ruleSet.getRuleset_Name()) == null) {
					map.put(ruleSet.getRuleset_Name(), new ArrayList<Output>());
				}
				boolean passCategory = false;
				boolean passTags = false;
				boolean passLang = false;
				boolean passName = false;
				boolean passType = false;
				boolean passKeywords = true;
				for (Rule rule : ruleSet.getRules()) {
					// if one rule success this document is accepted
					if (rule.getCategories() != null) {
						if(news.getCategories() == null) {
							continue;
						}
						if (rule.getCategories().contains(",")) {
							String[] categArr = rule.getCategories().split(",");
							categLoop: for (String categ : categArr) {
								for (String c : news.getCategories()) {
									if (c.equals(categ)) {
										passCategory = true;
										break categLoop;
									}
								}
							}
						} else {
							for (String c : news.getCategories()) {
								if (c.equals(rule.getCategories())) {
									passCategory = true;
								}
							}
						}
					} else {
						passCategory = true;
					}

					if (rule.getTags() != null) {
						if(news.getTags() == null) {
							continue;
						}
						if (rule.getTags().contains(",")) {
							String[] tagsArr = rule.getTags().split(",");
							tagLoop: for (String tag : tagsArr) {
								for(String t : news.getTags()) {
									if (t.equals(tag)) {
										passTags = true;
										break tagLoop;
									}
								}
							}
						} else {
							for(String t : news.getTags()) {
								if (t.equals(rule.getTags())) {
									passTags = true;
								}
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
							if (news.getLang().equals(rule.getLang())) {
								passLang = true;
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

					if (rule.getType() != null) {
						if (rule.getType().contains(",")) {
							String[] typeArr = rule.getType().split(",");
							typeLoop: for (String type : typeArr) {
								if (news.getType().equals(type)) {
									passType = true;
									break typeLoop;
								}
							}
						} else {
							if (news.getType().equals(rule.getType())) {
								passType = true;
							}
						}
					}

					// check keywords
					AhoCorasick ahoCorasickForRule = new AhoCorasick(1000);
					passKeywords = true;
					String[] keywords;
					if(rule.getKeywords().contains(",")) {
						keywords = rule.getKeywords().split(",");
					} else {
						keywords = new String[]{rule.getKeywords()};
					}
					for (String keyword : keywords) {
						boolean keywordFound = false;
						keyword = Utils.clearTurkishChars(keyword);
						keyword = keyword.replace(" ", "").toLowerCase();
						ahoCorasickForRule.addString(keyword);
						int nodeRule = 0;
						String outputENCharRule = Utils.clearTurkishChars(output.getText()).replace(" ", "")
								.toLowerCase();
						for (char ch : outputENCharRule.toCharArray()) {
							nodeRule = ahoCorasickForRule.transition(nodeRule, ch);
							if (ahoCorasickForRule.nodes[nodeRule].leaf) {
								keywordFound = true;
								break;
							}
						}
						if(!keywordFound) {
							passKeywords = false;
							break;
						}
					}
					output.getRules().addAll(Arrays.asList(keywords));

					if (passCategory && passLang && passName && passTags && passType && passKeywords) {
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
		String text = news.getTitle() + news.getDescription() + news.getContent();
		text = Utils.clearTurkishChars(text);
		output.setText(Utils.normalizeTurkishText(text));
		return output;
	}

}
