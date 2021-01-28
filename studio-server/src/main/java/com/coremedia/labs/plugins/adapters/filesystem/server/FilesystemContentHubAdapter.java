package com.coremedia.labs.plugins.adapters.filesystem.server;

import com.coremedia.contenthub.api.*;
import com.coremedia.contenthub.api.exception.ContentHubException;
import com.coremedia.contenthub.api.pagination.PaginationRequest;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import java.util.ArrayList;
import java.util.List;


class FilesystemContentHubAdapter implements ContentHubAdapter {
  private static final Logger LOGGER = LoggerFactory.getLogger(FilesystemContentHubAdapter.class);

  private final FilesystemContentHubSettings settings;
  private final String connectionId;

  private final File rootFile;
  private final boolean filterNoAccess;


  FilesystemContentHubAdapter(@NonNull FilesystemContentHubSettings settings, @NonNull String connectionId) {
    this.settings = settings;
    this.connectionId = connectionId;

    String rootPath = settings.getRootFolder();
    File file = new File(rootPath);
    this.rootFile = file;
    this.filterNoAccess = settings.getFilterNoAccess();

    if (!file.exists()) {
      LOGGER.warn("File Connector folder '" + file.getAbsolutePath() + " does not exist, connector will be ignored.");
    }
  }

  @NonNull
  @Override
  public Folder getRootFolder(@NonNull ContentHubContext context) throws ContentHubException {
    if (!rootFile.canRead()) throw new ContentHubException("Cannot read content of configured folder due to missing permissions.");

    ContentHubObjectId rootId = new ContentHubObjectId(connectionId, connectionId);
    return new FilesystemFolder(rootId, null, context, rootFile);

  }

  @Nullable
  @Override
  public Item getItem(@NonNull ContentHubContext context, @NonNull ContentHubObjectId id) throws ContentHubException {
    File file = new File(id.getExternalId());
    return new FilesystemItem(context, null, id, file);
  }

  @Nullable
  @Override
  public Folder getFolder(@NonNull ContentHubContext context, @NonNull ContentHubObjectId id) throws ContentHubException {
    File file = new File(id.getExternalId());
    return new FilesystemFolder(id, null, context, file);
  }

  @NonNull
  @Override
  public GetChildrenResult getChildren(@NonNull ContentHubContext context, @NonNull Folder folder, @Nullable PaginationRequest paginationRequest) {
    List<ContentHubObject> children = new ArrayList<>();

    List<File> result = ((FilesystemFolder) folder).getChildren();
    for (File entry : result) {
      ContentHubObjectId id = new ContentHubObjectId(connectionId, entry.getPath());
      if (!filterNoAccess || entry.canRead()){
        if (entry.isDirectory()) {
          children.add(new FilesystemFolder(id, (FilesystemFolder) folder, context, entry));
        } else {
          children.add(new FilesystemItem(context, (FilesystemFolder) folder, id, entry));
        }
      }
    }

    return new GetChildrenResult(children);
  }

  @Nullable
  @Override
  public Folder getParent(@NonNull ContentHubContext context, @NonNull ContentHubObject contentHubObject) throws ContentHubException {
    if (!contentHubObject.getId().equals(getRootFolder(context).getId())) {
      return getRootFolder(context);
    }
    return null;
  }

  @Override
  @NonNull
  public ContentHubTransformer transformer() {
    return new FilesystemContentHubTransformer();
  }


  //------------------------ Helper ------------------------------------------------------------------------------------

}
