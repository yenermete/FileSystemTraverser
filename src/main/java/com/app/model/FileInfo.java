package com.app.model;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.MapUtils;

import com.app.constants.FileConstants;

public class FileInfo {

	private String fileName;
	private boolean folder;
	private long fileSize;
	private int index;
	private String outString;
	private Integer indent;
	private boolean longFile;
	private Map<String, Integer> wordCountMap;

	public String getFileName() {
		return fileName;
	}

	public FileInfo setFileName(String fileName) {
		this.fileName = fileName;
		return this;
	}

	public long getFileSize() {
		return fileSize;
	}

	public FileInfo setFileSize(long fileSize) {
		this.fileSize = fileSize;
		return this;
	}

	public Map<String, Integer> getWordCountMap() {
		return wordCountMap;
	}

	public FileInfo setWordCountMap(Map<String, Integer> wordCountMap) {
		this.wordCountMap = wordCountMap;
		return this;
	}

	private StringBuilder getFileBaseString(String prefix) {
		StringBuilder builder = new StringBuilder()
				.append(String.join("", Collections.nCopies(indent, FileConstants.TAB))).append(prefix);
		if (folder) {
			builder.append(fileName).append(">");
		} else {
			builder.append(index).append(" ").append(fileName).append("> ").append("<#words> ");
		}
		return builder;
	}

	private String convertMaptoString() {
		if (MapUtils.isEmpty(wordCountMap)) {
			return "";
		}
		return wordCountMap.entrySet().stream().map(entry -> new StringBuilder().append(" <word").append(entry.getKey())
				.append(" ").append(entry.getValue()).append(">")).collect(Collectors.joining(""));
	}

	public String getOutString() {
		if (outString == null) {
			if (folder) {
				outString = getFileBaseString(FileConstants.SUB_FOLDER_PREFIX).toString();
			} else {
				if (longFile) {
					outString = getFileBaseString(FileConstants.FILE_PREFIX).append(convertMaptoString()).toString();
				} else {
					outString = getFileBaseString(FileConstants.FILE_PREFIX).toString();
				}
			}
		}
		return outString;
	}

	public FileInfo setOutString(String outString) {
		this.outString = outString;
		return this;
	}

	public Integer getIndent() {
		return indent;
	}

	public FileInfo setIndent(Integer indent) {
		this.indent = indent;
		return this;
	}

	public boolean isFolder() {
		return folder;
	}

	public FileInfo setFolder(boolean folder) {
		this.folder = folder;
		return this;
	}

	public boolean isLongFile() {
		return longFile;
	}

	public void setBigFile(boolean longFile) {
		this.longFile = longFile;
	}

	public int getIndex() {
		return index;
	}

	public FileInfo setIndex(int index) {
		this.index = index;
		return this;
	}

	@Override
	public String toString() {
		return outString == null ? new StringBuilder().append(fileName).append(" size => ").append(fileSize).toString()
				: outString;
	}
}
