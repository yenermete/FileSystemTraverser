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

import com.app.service.FileService;

@Singleton
public class FileServiceImpl implements FileService {

  // TODO
  private long longFileLowerLimit = 1024 * 128;
  private int longFileBufferSize = (int) longFileLowerLimit / 10;
  private int wordThreshold = 5;
  private String encoding = StandardCharsets.UTF_8.name();
  private final String wordRegex = "\\W+";

  public Map<String, Integer> getWordCountMap(Path path, boolean ignoreCase) throws IOException {
    if (Files.size(path) > longFileLowerLimit) {
      return getMapFromLargeFile(path, ignoreCase);
    }
    return getMapFromSmallFile(path, ignoreCase);
  }

  private Map<String, Integer> getMapFromSmallFile(Path path, boolean ignoreCase) throws IOException {
    Map<String, Integer> map = new HashMap<>();
    String fileContent = new String(Files.readAllBytes(path), encoding);
    updateMapWithContent(map, fileContent, ignoreCase);
    return filterMap(map);
  }

  private Map<String, Integer> getMapFromLargeFile(Path path, boolean ignoreCase) throws IOException {
    Map<String, Integer> map = new HashMap<>();
    RandomAccessFile file = new RandomAccessFile(path.toAbsolutePath().toString(), "r");
    FileChannel fileChannel = file.getChannel();
    ByteBuffer buffer = ByteBuffer.allocate(longFileBufferSize);
    while (fileChannel.read(buffer) > 0) {
      buffer.flip();
      String currentBuffer = new String(buffer.array(), encoding);
      updateMapWithContent(map, currentBuffer, ignoreCase);
      buffer.clear();
    }
    fileChannel.close();
    file.close();
    return filterMap(map);
  }

  private Map<String, Integer> filterMap(Map<String, Integer> map) {
    return map.entrySet().stream().filter(m -> m.getValue() >= wordThreshold).collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
  }

  private void updateMapWithContent(Map<String, Integer> map, String fileContent, boolean ignoreCase) {
    String[] words = fileContent.split(wordRegex);
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
