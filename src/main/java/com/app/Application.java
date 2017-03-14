package com.app;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.app.config.FileAppConfig;
import com.app.constants.FileConstants;
import com.app.file.FileApplication;
import com.app.module.FileModule;
import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class Application {

	private static FileAppConfig config;
	private static FileApplication fileApplication;
	private static final Logger logger = LogManager.getLogger(Application.class);

	public static void main(String[] args) {
		initConfig(args);
		createInjector();
		fileApplication.processDirectory();
	}

	private static void createInjector() {
		Injector injector = Guice.createInjector(new FileModule(config));
		fileApplication = injector.getInstance(FileApplication.class);

	}

	private static void initConfig(String[] arguments) {
		String configLocation = null;
		if (arguments != null && arguments.length > 0) {
			if (arguments.length == 1) {
				configLocation = arguments[0];
			} else {
				logger.error("This program accepts only one argument!");
				throw new RuntimeException("This program accepts only one argument!");
			}
		}
		YamlReader reader = null;
		try {
			if (StringUtils.isBlank(configLocation)) {
				reader = new YamlReader(new InputStreamReader(Thread.currentThread().getContextClassLoader()
						.getResourceAsStream(FileConstants.DEFAULT_CONFIG_FILE_LOCATION)));
			} else {
				reader = new YamlReader(new FileReader(configLocation));
			}

		} catch (FileNotFoundException e) {
			logger.error("File not found!", e);
			throw new RuntimeException("FileNotFound at config initialization!", e);
		}
		try {
			config = reader.read(FileAppConfig.class);
		} catch (YamlException e) {
			logger.error("Error reading file!", e);
			throw new RuntimeException("Error reading file at config initialization!", e);
		}
	}
}
