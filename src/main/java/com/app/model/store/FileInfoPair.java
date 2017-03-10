package com.app.model.store;

import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.app.constants.FileConstants;
import com.app.model.FileInfo;



public class FileInfoPair {

  private static final Logger logger = LogManager.getLogger(FileInfoPair.class);
  private final List<FileInfo> shortList;
  private final List<FileInfo> longList;
  private String lastParent;


  public FileInfoPair(List<FileInfo> shortList, List<FileInfo> longList) {
    this.shortList = shortList;
    this.longList = longList;
  }

  public List<FileInfo> getShortList() {
    return shortList;
  }

  public List<FileInfo> getLongList() {
    return longList;
  }

  public void printResults(boolean longFile) {
    printResults(longFile ? longList : shortList, longFile);
  }

  private void printResults(List<FileInfo> infoList, boolean longFile) {
    if (CollectionUtils.isEmpty(infoList)) {
      logger.info(String.format("No {} files were found!", longFile ? "long" : "short"));
      return;
    }
    lastParent = null;
    for (FileInfo info : infoList) {
      if(StringUtils.isEmpty(lastParent) || !lastParent.equals(info.getParentFolder())) {
        // TODO TABBBB
        System.out.println(String.join("", Collections.nCopies(info.getIndent() - 1, FileConstants.TAB)) + info.getParentFolder());
      }
      lastParent = info.getParentFolder();
      System.out.println(info.getOutString());
    }
  }

}
