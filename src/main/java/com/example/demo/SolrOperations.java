package com.example.demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.demo.beans.Output;

@Component
public class SolrOperations {

	@Value("${solrcoreurl}")
	public String solrcoreurl;

	SolrClient solrClient = new HttpSolrClient.Builder("http://localhost:8983/solr/").withConnectionTimeout(5000).withSocketTimeout(3000)
			.build();

	public void indexingInputDocuments(List<Output> outputs) {
		List<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
		for(Output output : outputs) {
			final SolrInputDocument doc = new SolrInputDocument();
			doc.addField("id", output.getId());
			doc.addField("url", output.getUrl());
			doc.addField("name", output.getName());
			doc.addField("lang", output.getLang());
			doc.addField("type", output.getType());
			doc.addField("tags", output.getTags());
			doc.addField("categories", output.getCategories());
			doc.addField("title", output.getTitle());
			doc.addField("description", output.getDescription());
			doc.addField("content", output.getContent());
			doc.addField("crawl_date", output.getCrawl_date());
			doc.addField("modified_date", output.getModified_date());
			doc.addField("published_date", output.getPublished_date());
			doc.addField("text", output.getText());
			doc.addField("rules", output.getRules());
			
			docs.add(doc);
			
		}

		try {
			if(!docs.isEmpty())
			solrClient.add("output_collection", docs);
			solrClient.commit("output_collection");

		} catch (SolrServerException | IOException e) {
			System.err.printf("\nFailed to indexing articles: %s", e.getMessage());
		}
	}

}
