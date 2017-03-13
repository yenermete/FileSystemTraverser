package com.app;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.app.config.FileAppConfig;
import com.app.constants.FileConstants;
import com.app.file.filter.TextFileFilter;
import com.app.injector.AppInjector;
import com.app.model.FileInfo;
import com.app.service.FileService;
import com.app.service.impl.FileServiceImpl;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class Application {

	private static List<FileInfo> shortFileList;
	private static List<FileInfo> longFileList;
	// TODO inject
	private static FileService service;
	private static final String CONFIG_FILE = "src/main/resources/config.yml";
	private static final Logger logger = LogManager.getLogger(Application.class);

	public static void main(String[] args) throws IOException {
		/*
		 * System.out.println("Please enter directory to search:"); Scanner
		 * scanner = new Scanner(System.in); String folder = scanner.nextLine();
		 * scanner.close();
		 */
		// TODO add parameter
		YamlReader reader = new YamlReader(new FileReader(CONFIG_FILE));
		FileAppConfig config = reader.read(FileAppConfig.class);
		String folder = "src/test/resources";
		logger.info("Traversing directory {}", folder);
		Path folderPath = createPath(folder);
		service = new FileServiceImpl(config.getEncoding(), config.getLongFileLowerLimit(), config.getWordThreshold(),
				config.getFileWordThreshold(), config.isIgnoreCase());
		TextFileFilter fileFilter = new TextFileFilter(config.getSearchedExtension());
		shortFileList = new LinkedList<>();
		longFileList = new LinkedList<>();
		createLists(folderPath, fileFilter, 1);
		System.out.println("<Directory " + folder + ">");
		printResults(true);
		printResults(false);
	}

	private static void printResults(boolean longList) {
		if (longList) {
			printList(longFileList, FileConstants.LONG_PRINT_PREFIX);
		} else {
			printList(shortFileList, FileConstants.SHORT_PRINT_PREFIX);
		}
	}

	private static void printList(List<FileInfo> list, String text) {
		Iterator<FileInfo> iterator = list.listIterator();
		System.out.println(text);
		while (iterator.hasNext()) {
			System.out.println(iterator.next().getOutString());
		}
	}

	private static void createLists(Path root, DirectoryStream.Filter<Path> fileFilter, int indent) {
		FileInfo info;
		long fileSize;
		int fileIndex = 1;
		int directoryIndex = 0;
		try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(root, fileFilter)) {
			for (Path path : directoryStream) {
				// TODO replace indent logic and directory writing logic
				if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
					logger.info("Searching directory {}", path);
					directoryIndex++;
					shortFileList.add(new FileInfo().setFileName(path.getFileName().toString()).setIndent(indent)
							.setIndex(directoryIndex).setFolder(true));
					longFileList.add(new FileInfo().setFileName(path.getFileName().toString()).setIndent(indent)
							.setIndex(directoryIndex).setFolder(true));
					createLists(path, fileFilter, indent + 1);
				} else {
					fileSize = Files.size(path);
					info = new FileInfo().setFileName(path.getFileName().toString()).setFileSize(fileSize)
							.setFolder(false).setIndent(indent).setIndex(fileIndex++);
					info.setWordCountMap(service.getWordCountMap(path, info));
					if (info.isLongFile()) {
						longFileList.add(info);
					} else {
						shortFileList.add(info);
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
