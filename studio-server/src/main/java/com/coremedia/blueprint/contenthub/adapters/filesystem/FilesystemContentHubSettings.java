package com.coremedia.blueprint.contenthub.adapters.filesystem;


/**
 * Interface that marks the settings that are needed for a connection to Dropbox
 */
interface FilesystemContentHubSettings {

  /**
   * @return the name
   */
  String getDisplayName();

  /**
   * @return the Proxy Host
   */
  String getProxyHost();

  /**
   * @return a value of {@link java.net.Proxy}#type
   */
  String getProxyType();

  /**
   * @return a proxy port for your Dropbox Connection
   */
  Integer getProxyPort();

  //--------------------# Filesystem settings #--------------------//

  /**
   * @return path to root folder to synch files from
   */
  String getRootFolder();

  /**
   * @return return if non-accessable files and directories should be filtered or not
   */
  boolean getFilterNoAccess();
}
