package com.coremedia.blueprint.contenthub.adapters.filesystem;


import com.coremedia.contenthub.api.ContentHubContext;
import com.coremedia.contenthub.api.ContentHubObjectId;
import com.coremedia.contenthub.api.ContentHubType;
import com.coremedia.contenthub.api.Folder;
import edu.umd.cs.findbugs.annotations.NonNull;

import java.io.File;
import java.util.*;

class FilesystemFolder extends FilesystemHubObject implements Folder {

  FilesystemFolder(ContentHubObjectId id, FilesystemFolder parent, ContentHubContext context, File file) {
    super(context, parent, id, file);
  }

  @NonNull
  @Override
  public ContentHubType getContentHubType() {
    return new ContentHubType("filesystem_folder");
  }

  public List<File> getChildren(){
    if (file.isDirectory()) {
      return Arrays.asList(Objects.requireNonNull(file.listFiles()));
    }
    return null;
  }
}
