package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.demo.ahoCorasickAlgorithm.AhoCorasick;
import com.example.demo.beans.Rule;
import com.example.demo.beans.RuleSet;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {

	@Autowired
	ApiConsumer apiConsumer;

	@Autowired
	RuleSetsProperties ruleSetsProperties;

	@Override
	public void run(String... args) throws Exception {
		// build tree for AhoCoraSick for all ruleSets to check at first it it found
		// here or not
		// to reduce the time complexity
		AhoCorasick ahoCorasick = new AhoCorasick(1000);
		for (RuleSet ruleSet : ruleSetsProperties.getRuleSets()) {
			for (Rule rule : ruleSet.getRules()) {
				String[] keywords = rule.getKeywords().split(",");
				for(String keyword : keywords) {
					keyword = Utils.clearTurkishChars(keyword);
					ahoCorasick.addString(keyword.replace(" ", "").toLowerCase());
				}
			}
		}
		apiConsumer.retrieveNewsFromApi(ahoCorasick);
	}

}
