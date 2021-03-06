package com.app.file.filter;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

import javax.inject.Inject;
import javax.inject.Named;

public class TextFileFilter implements DirectoryStream.Filter<Path> {

	private final String searchedExtension;

	@Inject
	public TextFileFilter(@Named("extension") String searchedExtension) {
		this.searchedExtension = searchedExtension;
	}

	@Override
	public boolean accept(Path entry) throws IOException {
		return Files.isDirectory(entry, LinkOption.NOFOLLOW_LINKS)
				|| (Files.isRegularFile(entry, LinkOption.NOFOLLOW_LINKS)
						&& entry.getFileName().toString().endsWith(searchedExtension));
	}

}
