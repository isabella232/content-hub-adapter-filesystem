package com.coremedia.blueprint.contenthub.adapters.filesystem;

import com.coremedia.contenthub.api.ContentHubAdapterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilesystemConfiguration {
  @Bean
  public ContentHubAdapterFactory filesystemContentHubAdapterFactory() {
    return new FilesystemContentHubAdapterFactory();
  }
}
