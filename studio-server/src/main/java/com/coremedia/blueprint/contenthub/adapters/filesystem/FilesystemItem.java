package com.coremedia.blueprint.contenthub.adapters.filesystem;


import com.coremedia.common.util.WordAbbreviator;
import com.coremedia.contenthub.api.*;
import com.coremedia.contenthub.api.preview.DetailsElement;
import com.coremedia.contenthub.api.preview.DetailsSection;
import com.coremedia.mimetype.TikaMimeTypeService;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import org.springframework.web.util.HtmlUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.UserPrincipal;
import java.util.*;
import java.util.stream.Collectors;

class FilesystemItem extends FilesystemHubObject implements Item {
  private static final WordAbbreviator ABBREVIATOR = new WordAbbreviator();
  private static final int BLOB_SIZE_LIMIT = 10000000;
  private TikaMimeTypeService tikaservice;

  FilesystemItem(ContentHubContext context, FilesystemFolder parent, ContentHubObjectId id, File file) {
    super(context, parent, id, file);
    this.tikaservice = new TikaMimeTypeService();
    tikaservice.init();
  }

  @NonNull
  @Override
  public ContentHubType getContentHubType() {
    return new ContentHubType("filesystem_file");
  }

  @NonNull
  @Override
  public String getName() {
    return file.getName();
  }

  @Nullable
  @Override
  public String getDescription() {
    if (file.exists()) {
      return HtmlUtils.htmlUnescape(file.getAbsolutePath());
    }

    return null;
  }

  @NonNull
  @Override
  public String getCoreMediaContentType() {
    if (tikaservice != null) {
      String type = tikaservice.getMimeTypeForResourceName(getName());

      if (type.contains("image"))
        return "CMPicture";
      else if (type.contains("audio"))
        return "CMAudio";
      else if (type.contains("video"))
        return "CMVideo";
      else if (type.equals("application/pdf"))
        return "CMDownload";
      else
        return "CMArticle";
    } else
      return "CMArticle";
  }

  @NonNull
  @Override
  public List<DetailsSection> getDetails() {
    return List.of(
            new DetailsSection("main", List.of(
                    new DetailsElement<>(file.getName(), false, SHOW_TYPE_ICON)
            ), false, false, false),
            new DetailsSection("metadata", List.of(
                    new DetailsElement<>("text", formatPreviewString(getDescription())),
                    new DetailsElement<>("author", formatPreviewString(getOwner(file))),
                    new DetailsElement<>("lastModified", formatPreviewDate(new Date(file.lastModified())))
            ).stream().filter(p -> Objects.nonNull(p.getValue())).collect(Collectors.toUnmodifiableList())));
  }


  @Nullable
  @Override
  public ContentHubBlob getBlob(String classifier) {

    return null;
  }

  @Nullable
  private String formatPreviewString(@Nullable String str) {
    return str == null ? null : ABBREVIATOR.abbreviateString(str, 240);
  }

  @Nullable
  private Calendar formatPreviewDate(@Nullable Date date) {
    if (date == null) {
      return null;
    }
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    return calendar;
  }
  //-------------------# Helper #-------------------//

  private String getOwner(File file){
    FileOwnerAttributeView ownerAttributeView = Files.getFileAttributeView(file.toPath(), FileOwnerAttributeView.class);
    try {
      UserPrincipal owner = ownerAttributeView.getOwner();
      return owner.getName();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }
}
