package com.app.file;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.app.model.FileInfo;
import com.app.service.FileService;

public class CompanyFileVisitor implements FileVisitor<Path> {

  private List<FileInfo> shortList = new ArrayList<FileInfo>();
  private List<FileInfo> longList = new ArrayList<FileInfo>();
  @Inject
  private FileService fileService;

  public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
    return FileVisitResult.CONTINUE;
  }

  public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
    return FileVisitResult.CONTINUE;
  }

  public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
    if (file.toFile().getName().endsWith(".txt")) {
      FileInfo fileInfo = new FileInfo();
      fileInfo.setFileName(file.getFileName().toString());
      longList.add(fileInfo);
    }
    return FileVisitResult.CONTINUE;
  }

  public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
    return FileVisitResult.TERMINATE;
  }

  public List<FileInfo> getShortList() {
    return shortList;
  }

  public List<FileInfo> getLongList() {
    return longList;
  }

  public FileService getFileService() {
    return fileService;
  }

  public void setFileService(FileService fileService) {
    this.fileService = fileService;
  }

}
