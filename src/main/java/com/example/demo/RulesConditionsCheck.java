package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.beans.News;
import com.example.demo.beans.Rule;
import com.example.demo.beans.RuleSet;

@Service
public class RulesConditionsCheck {
	
	@Autowired
	RuleSetsProperties ruleSetsProperties;

	public boolean check(News news) {
		// (making lowercase, trimming redundant spaces,
		// removing punctuations, and removing stopwords by language)
		
		// build tree for AhoCoraSick for all ruleSets to check at first it it found here or not 
		// to reduce the time complexity
		
		for(RuleSet ruleSet : ruleSetsProperties.ruleSets) {
			// every ruleset has it's excel sheets
			for(Rule rule : ruleSet.getRules()) {
				// if on rule success this document is accepted
			}
		}
		
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
	
}
