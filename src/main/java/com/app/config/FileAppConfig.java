package com.app.config;

public class FileAppConfig {

	private boolean ignoreCase;
	private String encoding;
	private int wordThreshold;
	private int fileWordThreshold;
	private int longFileLowerLimit;
	private String searchedExtension;

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
}
