package com.app.test;

import java.util.LinkedHashMap;
import java.util.Map;

import com.app.model.FileInfo;

import junit.framework.TestCase;

public class TestFileInfo extends TestCase {

	public void testOutStringLongFile() {
		FileInfo info = new FileInfo().setBigFile(true).setFileName("text_file.txt").setFolder(false).setIndent(3)
				.setIndex(2).setWordCountMap(null);
		assertEquals("			<file #2 text_file.txt> <#words> ", info.getOutString());
	}

	public void testOutStringLongFileWithRepetitions() {
		Map<String, Integer> wordCountMap = new LinkedHashMap<>();
		wordCountMap.put("abcd", 2);
		wordCountMap.put("xyz", 1);
		FileInfo info = new FileInfo().setBigFile(true).setFileName("text_file.txt").setFolder(false).setIndent(3)
				.setIndex(2).setWordCountMap(wordCountMap);
		assertEquals("			<file #2 text_file.txt> <#words>  <wordabcd 2> <wordxyz 1>", info.getOutString());
	}

	public void testOutStringShortFile() {
		FileInfo info = new FileInfo().setBigFile(false).setFileName("text_file.txt").setFolder(false).setIndent(2)
				.setIndex(1).setWordCountMap(null);
		assertEquals("		<file #1 text_file.txt> <#words> ", info.getOutString());

	}

	public void testOutStringFolder() {
		FileInfo info = new FileInfo().setBigFile(true).setFileName("folder_name").setFolder(true).setIndent(1)
				.setIndex(2).setWordCountMap(null);
		assertEquals("	<sub-directory folder_name>", info.getOutString());
	}
}
