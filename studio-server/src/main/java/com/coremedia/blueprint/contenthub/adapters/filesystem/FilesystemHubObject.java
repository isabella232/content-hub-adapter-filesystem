package com.coremedia.blueprint.contenthub.adapters.filesystem;


import com.coremedia.contenthub.api.ContentHubContext;
import com.coremedia.contenthub.api.ContentHubObject;
import com.coremedia.contenthub.api.ContentHubObjectId;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;

import java.io.File;

abstract class FilesystemHubObject implements ContentHubObject {

  private ContentHubObjectId hubId;
  private String name;
  private ContentHubContext context;
  private FilesystemFolder parent;
  protected File file;

  FilesystemHubObject(ContentHubContext context, @Nullable FilesystemFolder parent, ContentHubObjectId hubId, File file) {
    this.hubId = hubId;
    this.context = context;
    this.parent = parent;
    this.name = file.getName();
    this.file = file;

  }

  @NonNull
  @Override
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @NonNull
  @Override
  public String getDisplayName() {
    return getName();
  }

  @NonNull
  @Override
  public ContentHubObjectId getId() {
    return hubId;
  }

  public FilesystemFolder getParent() {
    return parent;
  }

  public File getFile() {
    return file;
  }
}
