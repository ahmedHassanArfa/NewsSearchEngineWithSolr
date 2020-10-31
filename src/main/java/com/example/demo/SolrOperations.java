package com.example.demo;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.demo.beans.News;

@Component
public class SolrOperations {

	@Value("${solrcoreurl}")
	public String solrcoreurl;

	SolrClient solrClient = new HttpSolrClient.Builder("http://localhost:8983/solr/output_collection").withConnectionTimeout(5000).withSocketTimeout(3000)
			.build();

	public void indexingByUsingSolrInputDocument(News news) {
		final SolrInputDocument doc = new SolrInputDocument();
		doc.addField("id", news.getId());
		doc.addField("category", news.getCategories());
		doc.addField("title", news.getTitle());

		try {
			solrClient.add(doc);

			solrClient.commit();

		} catch (SolrServerException | IOException e) {
			System.err.printf("\nFailed to indexing articles: %s", e.getMessage());
		}
	}

}
