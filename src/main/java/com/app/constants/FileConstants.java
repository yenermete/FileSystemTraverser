package com.app.constants;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class FileConstants {

	public static final String TAB = "\t";
	public static final String SUB_FOLDER_PREFIX = "<sub-directory ";
	public static final String FILE_PREFIX = "<file #";
	public static final String LONG_PRINT_PREFIX = "**** LONG FILES ****";
	public static final String SHORT_PRINT_PREFIX = "**** SHORT FILES ****";
	public static final String DEFAULT_CONFIG_FILE_LOCATION = "config.yml";
	public static final Set<String> EXTRA_WORD_SEPARATORS = new HashSet<>(Arrays.asList("-", "/"));
}
