package com.app.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

import com.app.model.FileInfo;

public interface FileService {

	public Map<String, Integer> getWordCountMap(Path path, FileInfo fileInfo) throws IOException;

}
