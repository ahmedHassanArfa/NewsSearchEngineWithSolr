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

	@Value("${solrCoreUrl}")
	private String SOLR_CORE_URL;

	SolrClient solrClient = new HttpSolrClient.Builder(getSOLR_CORE_URL()).withConnectionTimeout(5000)
			.withSocketTimeout(3000).build();

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

	public String getSOLR_CORE_URL() {
		return SOLR_CORE_URL;
	}

	public void setSOLR_CORE_URL(String sOLR_CORE_URL) {
		SOLR_CORE_URL = sOLR_CORE_URL;
	}

}
