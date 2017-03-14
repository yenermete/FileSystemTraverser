package com.app.test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;

import com.app.model.FileInfo;
import com.app.service.FileService;
import com.app.service.impl.FileServiceImpl;

import junit.framework.TestCase;

public class TestFileService extends TestCase {

	private FileService germanService;
	private FileService englishService;
	private FileService engLishCaseSensitiveService;
	private final String testFilesFolder = "src/test/resources/testfiles/";

	@Before
	protected void setUp() {
		germanService = new FileServiceImpl("UTF-8", 8096, 3, 20, true, "de");
		englishService = new FileServiceImpl("UTF-8", 8096, 3, 20, true, "en");
		engLishCaseSensitiveService = new FileServiceImpl("UTF-8", 8096, 3, 20, false, "en");
	}

	public void testSmallFileGermanRead() throws IOException {
		Map<String, Integer> map = germanService.getWordCountMap(Paths.get(testFilesFolder + "smallGermanFile.txt"),
				new FileInfo());
		assertNotNull(map);
		assertEquals(0, map.size());
	}

	public void testSmallFileGermanReadWithRepetitions() throws IOException {
		Map<String, Integer> map = germanService
				.getWordCountMap(Paths.get(testFilesFolder + "smallGermanFileWithRepetitions.txt"), new FileInfo());
		assertNotNull(map);
		assertEquals(0, map.size());
	}

	public void testSmallFileEnglishRead() throws IOException {
		Map<String, Integer> map = englishService.getWordCountMap(Paths.get(testFilesFolder + "smallEnglishFile.txt"),
				new FileInfo());
		assertNotNull(map);
		assertEquals(0, map.size());
	}

	public void testSmallFileEnglishReadWithEnoughRepetitions() throws IOException {
		Map<String, Integer> map = englishService
				.getWordCountMap(Paths.get(testFilesFolder + "smallEnglishFileWithRepetitions.txt"), new FileInfo());
		assertNotNull(map);
		assertEquals(0, map.size());
	}

	public void testBigFileEnglishDontIgnoreCase() throws IOException {
		Map<String, Integer> map = engLishCaseSensitiveService
				.getWordCountMap(Paths.get(testFilesFolder + "bigEnglishFile.txt"), new FileInfo());
		assertNotNull(map);
		assertEquals(0, map.size());
	}

	public void testBigFileEnglishDontIgnoreCaseWithRepetitions() throws IOException {
		Map<String, Integer> map = engLishCaseSensitiveService
				.getWordCountMap(Paths.get(testFilesFolder + "bigEnglishFileWithRepetitions.txt"), new FileInfo());
		assertNotNull(map);
		assertEquals(3, map.size());
		Map<String, Integer> expected = new HashMap<>();
		expected.put("is", 3);
		expected.put("it", 3);
		expected.put("English", 4);
		assertEquals(expected, map);
	}

	public void testBigFileGermanRead() throws IOException {
		Map<String, Integer> map = germanService.getWordCountMap(Paths.get(testFilesFolder + "bigGermanFile.txt"),
				new FileInfo());
		assertNotNull(map);
		assertEquals(0, map.size());
	}

	public void testBigFileEnglishRead() throws IOException {
		Map<String, Integer> map = englishService.getWordCountMap(Paths.get(testFilesFolder + "bigEnglishFile.txt"),
				new FileInfo());
		assertNotNull(map);
		assertEquals(0, map.size());
	}

	public void testBigFileGermanReadWithRepetitions() throws IOException {
		Map<String, Integer> map = germanService.getWordCountMap(Paths.get(testFilesFolder + "bigGermanFileWithRepetitions.txt"),
				new FileInfo());
		assertNotNull(map);
		assertEquals(7, map.size());
		Map<String, Integer> expected = new HashMap<>();
		expected.put("gerd", 5);
		expected.put("und", 4);
		expected.put("der", 3);
		expected.put("er", 4);
		expected.put("ich", 4);
		expected.put("heißt", 3);
		expected.put("übergeben", 3);
		assertEquals(expected, map);
	}

	public void testBigFileEnglishReadWithRepetitions() throws IOException {
		Map<String, Integer> map = englishService.getWordCountMap(Paths.get(testFilesFolder + "bigEnglishFileWithRepetitions.txt"),
				new FileInfo());
		assertNotNull(map);
		assertEquals(4, map.size());
		Map<String, Integer> expected = new HashMap<>();
		expected.put("think", 3);
		expected.put("english", 4);
		expected.put("is", 5);
		expected.put("it", 3);
		assertEquals(expected, map);
	}
}
