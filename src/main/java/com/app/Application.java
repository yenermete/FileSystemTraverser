package com.app;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

import org.apache.commons.collections4.MapUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.app.file.filter.CompanyFileFilter;
import com.app.injector.AppInjector;
import com.app.model.FileInfo;
import com.app.model.store.FileInfoPair;
import com.app.service.FileService;
import com.app.service.impl.FileServiceImpl;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class Application {

  private static List<FileInfo> shortList;
  private static List<FileInfo> longList;
  // TODO inject
  private static FileService service = new FileServiceImpl();
  private static boolean ignoreCase;
  private static final Logger logger = LogManager.getLogger(Application.class);

  public static void main(String[] args) throws IOException {
    System.out.println("Please enter directory to search:");
    Scanner scanner = new Scanner(System.in);
    String folder = scanner.nextLine();
    logger.info("Traversing directory {}", folder);
    Path folderPath = createPath(folder);

    scanner.close();
    CompanyFileFilter fileFilter = new CompanyFileFilter();
    shortList = new LinkedList<>();
    longList = new LinkedList<>();
    createLists(folderPath, fileFilter, 1);
    FileInfoPair pair = new FileInfoPair(shortList, longList);
    pair.printResults(true);
    pair.printResults(false);
  }

  private static void createLists(Path root, DirectoryStream.Filter<Path> fileFilter, int indent) {
    FileInfo info;
    long fileSize;
    try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(root, fileFilter)) {
      for (Path path : directoryStream) {
        // TODO replace indent logic and directory writing logic
        if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
          logger.info("Searching directory {}", path);
          createLists(path, fileFilter, indent + 1);
        } else {
          fileSize = Files.size(path);
          info = new FileInfo().setFileName(path.getFileName().toString()).setFileSize(fileSize)
              .setParentFolder(path.getParent().toString()).setIndent(indent)
              .setWordCountMap(service.getWordCountMap(path, ignoreCase));
          if (MapUtils.isEmpty(info.getWordCountMap())) {
            shortList.add(info);
          } else {
            longList.add(info);
          }
        }

      }
    } catch (IOException e) {
      logger.error("IO Error while reading files", e);
      throw new RuntimeException("IO Error", e);
    }
  }

  private static Path createPath(String path) {
    Path result = Paths.get(path);
    // TODO check if not required
    if (Files.exists(result, LinkOption.NOFOLLOW_LINKS) && Files.isReadable(result)) {
      if (Files.isDirectory(result, LinkOption.NOFOLLOW_LINKS)) {
        return result;
      }
      throw new RuntimeException(String.format("%s is not a folder", path));
    }
    throw new RuntimeException(String.format("Can't access file %s", path));
  }
}
