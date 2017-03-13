package com.app.service.impl;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Singleton;

import org.apache.commons.lang3.StringUtils;

import com.app.constants.FileConstants;
import com.app.model.FileInfo;
import com.app.service.FileService;

@Singleton
public class FileServiceImpl implements FileService {

	// TODO inject
	private long longFileLowerLimit;
	private int longFileBufferSize;
	private int wordThreshold;
	private int fileWordThreshold;
	private String encoding;
	private boolean ignoreCase;

	public FileServiceImpl(String encoding, int longFileLowerLimit, int wordThreshold, int fileWordThreshold,
			boolean ignoreCase) {
		this.encoding = encoding;
		this.longFileLowerLimit = longFileLowerLimit;
		this.longFileBufferSize = longFileLowerLimit / 10;
		this.wordThreshold = wordThreshold;
		this.fileWordThreshold = fileWordThreshold;
		this.ignoreCase = ignoreCase;
	}

	public Map<String, Integer> getWordCountMap(Path path, FileInfo fileInfo) throws IOException {
		if (Files.size(path) > longFileLowerLimit) {
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
		String[] words = fileContent.split(FileConstants.WORD_REGEX);
		if (words != null && words.length > fileWordThreshold) {
			fileInfo.setBigFile(true);
		} else {
			fileInfo.setBigFile(false);
		}
		String currentWord;
		for (String word : words) {
			if (ignoreCase) {
				currentWord = word.toLowerCase();
			} else {
				currentWord = word;
			}
			if (StringUtils.isNotBlank(currentWord)) {
				if (map.get(currentWord) == null) {
					map.put(currentWord, 1);
				} else {
					map.put(currentWord, map.get(currentWord) + 1);
				}
			}
		}
	}

}
