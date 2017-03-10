package com.app;

import java.io.IOException;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.tuple.Pair;

import com.app.file.CompanyFileVisitor;
import com.app.model.FileInfo;
import com.app.service.FileService;

public class FileApplication {

  @Inject
  private FileService service;

  public Pair<List<FileInfo>, List<FileInfo>> traverseTree(Path folderPath) throws IOException {
    FileVisitor<Path> visitor = new CompanyFileVisitor();
    Files.walkFileTree(folderPath, visitor);
    return Pair.of(((CompanyFileVisitor) visitor).getShortList(), ((CompanyFileVisitor) visitor).getLongList());
  }

}
