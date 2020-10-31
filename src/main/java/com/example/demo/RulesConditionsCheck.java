package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.beans.News;

@Service
public class RulesConditionsCheck {
	
	@Autowired
	RuleSetsProperties ruleSetsProperties;

	public boolean check(News news) {
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
