package com.app.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.app.file.CompanyFileVisitor;

public class FileUtil {

  public static String getFileContent(File file) {
    return null;
  }
  
  public static void traverseFileTree(Path path) throws IOException {
    
    Files.walkFileTree(path, new CompanyFileVisitor());
  }
}
