package com.coremedia.labs.plugins.adapters.filesystem.server;

import com.coremedia.cap.common.Blob;
import com.coremedia.contenthub.api.*;
import com.coremedia.cotopaxi.common.blobs.BlobServiceImpl;
import com.coremedia.mimetype.TikaMimeTypeService;
import com.coremedia.util.TempFileFactory;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import org.springframework.web.util.HtmlUtils;

import javax.activation.MimeTypeParseException;
import java.io.File;
import java.util.*;

class FilesystemContentHubTransformer implements ContentHubTransformer {

  private TikaMimeTypeService tikaMimeTypeService;

  @Override
  @NonNull
  public ContentModel transform(Item item, ContentHubAdapter contentHubAdapter, ContentHubContext contentHubContext) {
    if (!(item instanceof FilesystemItem)) {
      throw new IllegalArgumentException("Not my item: " + item);
    }
    return transformFilesystemItem((FilesystemItem) item);
  }

  @Override
  @Nullable
  public ContentModel resolveReference(ContentHubObject owner, ContentModelReference reference, ContentHubAdapter contentHubAdapter, ContentHubContext contentHubContext) {
    String imageUrl = (String) reference.getData();
    String imageName = ContentCreationUtil.extractNameFromUrl(imageUrl);
    if (imageName == null) {
      return null;
    }
    ContentModel referenceModel = ContentModel.createReferenceModel(imageName, reference.getCoreMediaContentType());
    referenceModel.put("data", new UrlBlobBuilder(owner, "filesystemPicture").withUrl(imageUrl).withEtag().build());
    referenceModel.put("title", "Image " + imageName);

    return referenceModel;
  }


  // --- internal ---------------------------------------------------

  @NonNull
  private ContentModel transformFilesystemItem(FilesystemItem item) {
    ContentModel contentModel = ContentModel.createContentModel(item.getName(), item.getId(), item.getCoreMediaContentType());
    contentModel.put("title", item.getName());
    String type = item.getCoreMediaContentType();

    Map<String, Object> additionalProps = new HashMap<>();

    if (type.equals("CMAudio") || type.equals("CMPicture") || type.equals("CMVideo") || type.equals("CMDownload")) {
      additionalProps.putAll(Objects.requireNonNull(getMediaProperties(item)));
    }

    String description = extractDescription(item);
    if (description != null) {
      contentModel.put("detailText", ContentCreationUtil.convertStringToRichtext(description));
    }

    //add additional properties
    additionalProps.forEach(contentModel::put);

    return contentModel;
  }

  @Nullable
  private String extractDescription(@Nullable FilesystemItem item) {
    if (item != null) {
      String description = item.getDescription();
      return description != null ? HtmlUtils.htmlUnescape(description) : null;
    }
    return null;
  }

  @Nullable
  private Map<String, Object> getMediaProperties(FilesystemItem item) {

    Map<String, Object> result = new HashMap<>();

    try {
      File file = item.getFile();
      BlobServiceImpl blobService = new BlobServiceImpl(new TempFileFactory(), getTika());
      Blob blob = blobService.fromFile(file, getTika().getMimeTypeForResourceName(file.getName()));
      result.put("data", blob);
      return result;
    } catch (MimeTypeParseException e) {
      e.printStackTrace();
    }
    return null;
  }

  private TikaMimeTypeService getTika() {
    if (tikaMimeTypeService == null) {
      this.tikaMimeTypeService = new TikaMimeTypeService();
      tikaMimeTypeService.init();
    }
    return tikaMimeTypeService;
  }
}
