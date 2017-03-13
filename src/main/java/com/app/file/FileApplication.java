package com.app.file;

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

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.app.constants.FileConstants;
import com.app.file.filter.TextFileFilter;
import com.app.model.FileInfo;
import com.app.service.FileService;

public class FileApplication {

	private List<FileInfo> shortFileList;
	private List<FileInfo> longFileList;
	private FileService service;
	private TextFileFilter fileFilter;
	private final Logger logger = LogManager.getLogger(FileApplication.class);

	@Inject
	public FileApplication(FileService fileService, TextFileFilter fileFilter) {
		this.service = fileService;
		this.fileFilter = fileFilter;
	}

	private String getInputFolder() {
		System.out.println("Please enter directory to search:");
		Scanner scanner = new Scanner(System.in);
		String folder = scanner.nextLine();
		scanner.close();
		return folder;
	}

	private void printResults(boolean longList, String folder) {
		System.out.println("<Directory " + folder + ">");
		if (longList) {
			printList(longFileList, FileConstants.LONG_PRINT_PREFIX);
		} else {
			printList(shortFileList, FileConstants.SHORT_PRINT_PREFIX);
		}
	}

	private void printList(List<FileInfo> list, String text) {
		Iterator<FileInfo> iterator = list.listIterator();
		System.out.println(text);
		while (iterator.hasNext()) {
			System.out.println(iterator.next().getOutString());
		}
	}

	public void processDirectory() {
		String folder = getInputFolder();
		Path folderPath = createPath(folder);
		shortFileList = new LinkedList<>();
		longFileList = new LinkedList<>();
		logger.info("Traversing directory {}", folder);
		createLists(folderPath, fileFilter, 1);
		logger.info("Lists created. Printing details");
		printResults(true, folder);
		printResults(false, folder);
	}

	private void createLists(Path root, DirectoryStream.Filter<Path> fileFilter, int indent) {
		FileInfo info;
		long fileSize;
		int fileIndex = 1;
		int directoryIndex = 0;
		try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(root, fileFilter)) {
			for (Path path : directoryStream) {
				if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
					logger.info("Searching sub directory {}", path);
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

	private Path createPath(String path) {
		Path result = Paths.get(path);
		if (Files.exists(result, LinkOption.NOFOLLOW_LINKS) && Files.isReadable(result)) {
			if (Files.isDirectory(result, LinkOption.NOFOLLOW_LINKS)) {
				return result;
			}
			logger.warn("{} is not a folder", path);
			throw new RuntimeException(String.format("%s is not a folder", path));
		}
		logger.warn("Can't access %s", path);
		throw new RuntimeException(String.format("Can't access file %s", path));
	}
}
