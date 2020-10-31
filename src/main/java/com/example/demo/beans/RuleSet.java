package com.example.demo.beans;

import java.util.List;

public class RuleSet {

	private String ruleset_Name;
	
	private List<Rule> rules;

	public String getRuleset_Name() {
		return ruleset_Name;
	}

	public void setRuleset_Name(String ruleset_Name) {
		this.ruleset_Name = ruleset_Name;
	}

	public List<Rule> getRules() {
		return rules;
	}

	public void setRules(List<Rule> rules) {
		this.rules = rules;
	}

}
