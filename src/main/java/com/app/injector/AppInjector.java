package com.app.injector;

import com.app.service.FileService;
import com.app.service.impl.FileServiceImpl;
import com.google.inject.AbstractModule;

public class AppInjector extends AbstractModule {

  @Override
  protected void configure() {
    bind(FileService.class).to(FileServiceImpl.class);

  }

}
