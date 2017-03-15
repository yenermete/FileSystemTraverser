package com.app.config;

import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Locale;

import org.apache.commons.lang3.LocaleUtils;

public class FileAppConfig {

	private boolean ignoreCase;
	private String encoding;
	private int wordThreshold;
	private int fileWordThreshold;
	private int longFileLowerLimit;
	private String searchedExtension;
	private String language;

	public void validate() {
		Locale locale = new Locale.Builder().setLanguageTag(language).build();
		if (!LocaleUtils.isAvailableLocale(locale)) {
			throw new RuntimeException("Please enter a valid language value at config file.");
		}
		try {
			Charset.forName(encoding);
		} catch (UnsupportedCharsetException e) {
			throw new RuntimeException("Please enter a valid encoding value at config file.", e);
		}
		if (wordThreshold <= 0 || fileWordThreshold <= 0 || longFileLowerLimit <= 0) {
			throw new RuntimeException("All threshold and limit values at config file should be greater than zero.");
		}
	}

	public boolean isIgnoreCase() {
		return ignoreCase;
	}

	public void setIgnoreCase(boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public int getWordThreshold() {
		return wordThreshold;
	}

	public void setWordThreshold(int wordThreshold) {
		this.wordThreshold = wordThreshold;
	}

	public int getFileWordThreshold() {
		return fileWordThreshold;
	}

	public void setFileWordThreshold(int fileWordThreshold) {
		this.fileWordThreshold = fileWordThreshold;
	}

	public int getLongFileLowerLimit() {
		return longFileLowerLimit;
	}

	public void setLongFileLowerLimit(int longFileLowerLimit) {
		this.longFileLowerLimit = longFileLowerLimit;
	}

	public String getSearchedExtension() {
		return searchedExtension;
	}

	public void setSearchedExtension(String searchedExtension) {
		this.searchedExtension = searchedExtension;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
}
