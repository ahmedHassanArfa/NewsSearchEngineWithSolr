package com.example.demo;

public class Utils {

	public static String normalizeTurkishText(String text) {
		// (making lowercase, trimming redundant spaces,
		// removing punctuations, and removing stopwords by language)
		String normalizedText = text.replaceAll("[^\\p{L}\\p{N}]", "").toLowerCase();
		// remove StopWords
		return normalizedText;
	}

}
