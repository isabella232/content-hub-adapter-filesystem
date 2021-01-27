package com.coremedia.labs.plugins.adapters.filesystem.server;


import com.coremedia.contenthub.api.ContentHubContext;
import com.coremedia.contenthub.api.ContentHubObjectId;
import com.coremedia.contenthub.api.ContentHubType;
import com.coremedia.contenthub.api.Folder;
import edu.umd.cs.findbugs.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

class FilesystemFolder extends FilesystemHubObject implements Folder {

  private static final Logger LOGGER = LoggerFactory.getLogger(FilesystemContentHubAdapter.class);

  FilesystemFolder(ContentHubObjectId id, FilesystemFolder parent, ContentHubContext context, File file) {
    super(context, parent, id, file);
  }

  @NonNull
  @Override
  public ContentHubType getContentHubType() {
    return new ContentHubType("filesystem_folder");
  }

  @NonNull
  public List<File> getChildren(){
    if (file.isDirectory()) {
      try {
        File[] files = file.listFiles();
        if (files != null) {
          return Arrays.stream(files).filter(Objects::nonNull).collect(Collectors.toList());
        }
      } catch (SecurityException e) {
        LOGGER.debug("No read access to directory {}", file, e);
      }
    }
    return Collections.emptyList();
  }
}
