package com.app.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

public interface FileService {


  public Map<String, Integer> getWordCountMap(Path path, boolean ignoreCase) throws IOException;

}
