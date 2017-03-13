package com.app.module;

import com.app.config.FileAppConfig;
import com.app.file.filter.TextFileFilter;
import com.app.service.FileService;
import com.app.service.impl.FileServiceImpl;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class FileModule extends AbstractModule {

	private FileAppConfig configuration;

	public FileModule(FileAppConfig configuration) {
		this.configuration = configuration;
	}

	@Override
	protected void configure() {
		bindConstant().annotatedWith(Names.named("encoding")).to(configuration.getEncoding());
		bindConstant().annotatedWith(Names.named("fileWordThreshold")).to(configuration.getFileWordThreshold());
		bindConstant().annotatedWith(Names.named("longFileLowerLimit")).to(configuration.getLongFileLowerLimit());
		bindConstant().annotatedWith(Names.named("extension")).to(configuration.getSearchedExtension());
		bindConstant().annotatedWith(Names.named("wordThreshold")).to(configuration.getWordThreshold());
		bindConstant().annotatedWith(Names.named("ignoreCase")).to(configuration.isIgnoreCase());
		bindConstant().annotatedWith(Names.named("language")).to(configuration.getLanguage());
		bind(FileService.class).to(FileServiceImpl.class);
		bind(TextFileFilter.class).toInstance(new TextFileFilter(configuration.getSearchedExtension()));

	}

}
