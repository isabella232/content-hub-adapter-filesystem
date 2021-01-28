package com.coremedia.labs.plugins.adapters.filesystem.server;

import com.coremedia.contenthub.api.ContentHubAdapterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilesystemConfiguration {
  @Bean
  public ContentHubAdapterFactory<?> filesystemContentHubAdapterFactory() {
    return new FilesystemContentHubAdapterFactory();
  }
}
