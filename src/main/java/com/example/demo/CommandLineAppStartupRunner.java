package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

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
		for (RuleSet ruleSet : ruleSetsProperties.getRuleSets()) {
			for (Rule rule : ruleSet.getRules()) {
//				String pattern = sc.nextLine().toLowerCase().trim();
//		        ahoCorasick.addString(pattern);
//		        int node = 0;
//		        for (char ch : main.toCharArray())
//		        {
//		            node = ahoCorasick.transition(node, ch);
//		        }
//		        if (ahoCorasick.nodes[node].leaf)
//		            System.out.println("A '" + pattern + "' string is substring of "
//		                    + main + " string.");
//		        else
//		            System.out.println("A '" + pattern
//		                    + "' string is not substring of " + main + " string.");ed
			}
		}
		apiConsumer.retrieveNewsFromApi();
	}

}
