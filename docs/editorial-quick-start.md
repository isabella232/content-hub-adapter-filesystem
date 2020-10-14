# Editorial Quick Start

--------------------------------------------------------------------------------

\[[Up](README.md)\] \[[Top](#top)\]

--------------------------------------------------------------------------------

## Table of contents

* [Introducing](#introducing)
* [Browsing the content-hub-adapter-filesystem](#browsing-the-content-hub-adapter-filesystem)
    * [Basic adapter configuration](#basic-adapter-configuration)
        * [Global adapter configuration](#global-adapter-configuration)
        * [Site specific adapter configuration](#site-specific-adapter-configuration)
    * [Detailed adapter configuration](#detailed-adapter-configuration)
        * [Basic structure](#basic-structure)
        * [Required configuration](#required-configuration)
        * [Example](#example)     
* [Usage](#usage)    

## Introducing

As mentioned in the main documentation, the coremedia-content-hub-filesystem extension
is providing access to a configurable path on the server hosting the Studio server. Common to all
content-hub-adapters is the appearance in CoreMedia Studio. The image below is showing 
multiple configured content-hub-adapters in Studio (content-hub-adapter-filesystem is marked with a purple border).

![Image1: Studio appearance with configured adapters](images/editorial/library01.png)
  
## Browsing the content-hub-adapter-filesystem
Depending on the configuration of the adapter, the appearance of the tree may vary. The following sections 
are taking care of all configuration **places** and **options**.

### Basic adapter configuration
This section is covering the two possibilities to enable the content-hub-adapter-filesystem integration. Please note that those
options are valid for all content-hub-adapters. Before configuring the adapter, please refer to the documentation [Content hub configuration](https://documentation.coremedia.com/cmcc-10/artifacts/2004/webhelp/deployment-en/content/Studio-Contenthub-Configuration.html)
for preliminary steps.

#### Global adapter configuration
To enable the content-hub-adapter-filesystem for all sites, it is necessary to create a CMSettings document located at:
* /Settings/Options/Settings/Content Hub/Connections/
For convenience reasons, naming proposal of this document is "Filesystem"

#### Site specific adapter configuration
To enable the content-hub-adapter-filesystem for a single site, it is necessary to create a CMSettings document located at:
* Options/Settings/Content Hub/Connections/ (relative to the site's root folder)
For convenience reasons, naming proposal of this document is "Filesystem" (name of the third party system)


### Detailed adapter configuration

#### Basic structure
The table below is showing the initial toplevel entry for all content-hub-connector configurations.

| Key         | Type       | Required   |
|-------------|------------|------------|
| connections | StructList | Yes        |

After creation of the initial struct list called **connections** the next step is to create the first entry. This can be done 
in Studio with the struct editor by pressing "add Item to ListProperty". The table below is showing the entries which are common for all connectors.

| Key           | Type       | Value                 | Required   |
|---------------|------------|------------           |------------|
| connectionId  | String      | <SOME_UNIQUE_ID>     | Yes        |
| factoryId     | String      | <YOUR_FACTORY_ID>    | Yes        |
| enabled       | Boolean     | true or false        | Yes        |
| settings       | Struct     |                      | Yes        |
          

#### Required configuration
In section [Basic structure](#basic-structure) and according to the table, the settings struct is currently empty.
The settings struct itself is holding specific configuration options for the connector (common to all connectors).
The table below is depicting all potential entries. 

| Key               | Type       | Value                                                    | Required   |
|---------------    |------------|------------                                              |------------|
| rootPath          | String     | Absolute path where the adapter starts to build the tree from             | Yes        |
| filterNoAccess    | Boolean    | true or false                                            | No        |
| displayName       | String     | Name of the root folder to display in Studio             | No         |

#### Example
The image below is depicting a full configuration of the content-hub-adapter-filesystem in global space

![Image2: Full adapter configuration](images/editorial/settings.png)

## Usage
Once the connector is configured, the "Filesystem" named tree should appear inside the library, and by clicking on "Filesystem"
the tree expands and is loading and showing content from the file system in the well known folder-content structure from CoreMedia. The image below is showing the expected appearance.

![Image2: Expanded Studio tree](images/editorial/library02.png)  

By browsing the tree, the content-hub-adapter-filesystem is providing an import mechanism for content objects. The picture below is showing the 
button for creation (purple frame).

![Image2: Expanded Studio tree](images/editorial/library03.png) 