package com.app.service.impl;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.BreakIterator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;

import com.app.constants.FileConstants;
import com.app.model.FileInfo;
import com.app.service.FileService;

public class FileServiceImpl implements FileService {

	private long longFileLowerLimit;
	private int longFileBufferSize;
	private int wordThreshold;
	private int fileWordThreshold;
	private String encoding;
	private boolean ignoreCase;
	private BreakIterator wordIterator;
	private Locale locale;

	@Inject
	public FileServiceImpl(@Named("encoding") String encoding, @Named("longFileLowerLimit") int longFileLowerLimit,
			@Named("wordThreshold") int wordThreshold, @Named("fileWordThreshold") int fileWordThreshold,
			@Named("ignoreCase") boolean ignoreCase, @Named("language") String language) {
		this.encoding = encoding;
		this.longFileLowerLimit = longFileLowerLimit;
		this.longFileBufferSize = longFileLowerLimit / 10;
		this.wordThreshold = wordThreshold;
		this.fileWordThreshold = fileWordThreshold;
		this.ignoreCase = ignoreCase;
		this.locale = new Locale(language);
		this.wordIterator = BreakIterator.getWordInstance(locale);
	}

	public Map<String, Integer> getWordCountMap(Path path, FileInfo fileInfo) throws IOException {
		if (fileInfo.getFileSize() > longFileLowerLimit) {
			return getMapFromLargeFile(path, fileInfo);
		}
		return getMapFromSmallFile(path, fileInfo);
	}

	private Map<String, Integer> getMapFromSmallFile(Path path, FileInfo fileInfo) throws IOException {
		Map<String, Integer> map = new HashMap<>();
		String fileContent = new String(Files.readAllBytes(path), encoding);
		if (StringUtils.isNotBlank(fileContent)) {
			updateMapWithContent(map, fileInfo, fileContent, ignoreCase);
		}
		return filterMap(map);
	}

	private Map<String, Integer> getMapFromLargeFile(Path path, FileInfo fileInfo) throws IOException {
		Map<String, Integer> map = new HashMap<>();
		RandomAccessFile file = new RandomAccessFile(path.toAbsolutePath().toString(), "r");
		FileChannel fileChannel = file.getChannel();
		ByteBuffer buffer = ByteBuffer.allocate(longFileBufferSize);
		while (fileChannel.read(buffer) > 0) {
			buffer.flip();
			String currentBuffer = new String(buffer.array(), encoding);
			if (StringUtils.isNotBlank(currentBuffer)) {
				updateMapWithContent(map, fileInfo, currentBuffer, ignoreCase);
			}
			buffer.clear();
		}
		fileChannel.close();
		file.close();
		return filterMap(map);
	}

	private Map<String, Integer> filterMap(Map<String, Integer> map) {
		return map.entrySet().stream().filter(m -> m.getValue() >= wordThreshold)
				.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
	}

	private void updateMapWithContent(Map<String, Integer> map, FileInfo fileInfo, String fileContent,
			boolean ignoreCase) {
		int wordCount = 0;
		wordIterator.setText(fileContent);
		int start = wordIterator.first();
		int end = wordIterator.next();
		String[] extraSeparatedWords;
		while (end != BreakIterator.DONE) {
			String word = fileContent.substring(start, end);
			if (ignoreCase) {
				word = word.toLowerCase(locale);
			}
			if (StringUtils.isNotBlank(word) && Character.isLetterOrDigit(word.charAt(0))) {
				for (String separator : FileConstants.EXTRA_WORD_SEPARATORS) {
					word = word.replaceAll(separator, " ");
				}
				extraSeparatedWords = word.split(" ");
				for (String currentWord : extraSeparatedWords) {
					if (map.get(currentWord) == null) {
						map.put(currentWord, 1);
					} else {
						map.put(currentWord, map.get(currentWord) + 1);
					}
					wordCount++;
				}
			}
			start = end;
			end = wordIterator.next();
		}
		if (wordCount >= fileWordThreshold) {
			fileInfo.setBigFile(true);
		} else {
			fileInfo.setBigFile(false);
			map.clear();
		}
	}

}
