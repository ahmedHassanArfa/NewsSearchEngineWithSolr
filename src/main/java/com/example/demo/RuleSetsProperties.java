package com.example.demo;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.example.demo.beans.RuleSet;

@Component
@ConfigurationProperties("rulesconfig")
public class RuleSetsProperties {
	
	private List<RuleSet> ruleSets;

	public List<RuleSet> getRuleSets() {
		return ruleSets;
	}

	public void setRuleSets(List<RuleSet> ruleSets) {
		this.ruleSets = ruleSets;
	}
	

}
