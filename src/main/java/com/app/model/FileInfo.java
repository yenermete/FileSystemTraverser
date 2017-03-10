package com.app.model;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.collections4.MapUtils;

import com.app.constants.FileConstants;

public class FileInfo {

  private String fileName;
  private String parentFolder;
  private long fileSize;
  private String outString;
  private Integer indent;
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

  public String getOutString() {
    if (outString == null) {
      outString =
          new StringBuilder().append(String.join("", Collections.nCopies(indent, FileConstants.TAB))).append("<").append(fileName)
              .append("> ").append("<#words> ")
              .append(MapUtils.isEmpty(wordCountMap) ? ""
                  : wordCountMap.entrySet().stream().map(
                      entry -> new StringBuilder().append(" <word").append(entry.getKey()).append(" ").append(entry.getValue()).append(">"))
                      .collect(Collectors.joining("")))
              .toString();
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

  public String getParentFolder() {
    return parentFolder;
  }

  public FileInfo setParentFolder(String parentFolder) {
    this.parentFolder = parentFolder;
    return this;
  }

  @Override
  public String toString() {
    return outString == null ? new StringBuilder().append(fileName).append(" size => ").append(fileSize).toString() : outString;
  }
}
