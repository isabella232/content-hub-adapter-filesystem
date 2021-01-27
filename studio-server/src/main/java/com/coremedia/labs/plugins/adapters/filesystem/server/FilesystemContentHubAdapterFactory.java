package com.coremedia.labs.plugins.adapters.filesystem.server;

import com.coremedia.contenthub.api.ContentHubAdapter;
import com.coremedia.contenthub.api.ContentHubAdapterFactory;
import edu.umd.cs.findbugs.annotations.NonNull;

/**
 *
 */
class FilesystemContentHubAdapterFactory implements ContentHubAdapterFactory<FilesystemContentHubSettings> {

  @Override
  @NonNull
  public String getId() {
    return "filesystem";
  }

  @NonNull
  @Override
  public ContentHubAdapter createAdapter(@NonNull FilesystemContentHubSettings settings,
                                         @NonNull String connectionId) {
    return new FilesystemContentHubAdapter(settings, connectionId);
  }

}
