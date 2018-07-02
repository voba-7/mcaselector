# MCA Selector
#### An external tool to export or delete selected chunks and regions from a Minecraft world.
---

## Usage
### Navigation
Executing the tool, it shows an empty window with a chunk and a region grid. To actually show a world, open a folder containing Minecraft Anvil (\*.mca) files. The tool will then render a top-down view of this world that you can zoom into and zoom out of by scrolling up and down and that you can move around using the middle mouse button (```Cmd+LMB``` on Mac OS).

![alt text](https://raw.githubusercontent.com/Querz/mcaselector/assets/assets/mca_selector_default.png "MCA Selector window showing chunk and region grid")

Zooming out far enough disables the selection of single chunks but lets you select entire regions.

### Selections
Upon finishing selecting chunks and regions, they can be deleted or exported using the ```Selection```-menu. Exported chunks and regions are not deleted from the original world.

![alt text](https://raw.githubusercontent.com/Querz/mcaselector/assets/assets/mca_selector_selections.png "MCA Selector window showing chunk and region selection export")

A selection (not the chunks and regions themselves) can also be exported or imported and even be applied to different worlds.

### Chunk filter
The MCA Selector also contains a powerful tool to delete chunks and regions by conditions like the data version, the time it was last updated, hoch much time players have spent in this chunk or the location of that chunk. Multiple of these conditions can be used to create a very specific query about what chunks and regions should be deleted or exported.

![alt text](https://raw.githubusercontent.com/Querz/mcaselector/assets/assets/mca_selector_filter_chunks.png "MCA Selector window showing the chunk filter")

Because the conditions use internal values used by minecraft, they are not very easy to understand:

| Condition | Type | Description |
| --------- | ----- | ----------- |
| Group | - | Groups multiple conditions. |
| DataVersion | int | The DataVersion tag of the chunk. 100-1343 for 1.12.2 and below, 1444 for 1.13 snapshots and above. |
| InhabitedTime | long | The total amount of time in game-ticks players have spent in that chunk. 1 second ~20 ticks. |
| LastUpdate | int | The time a chunk was last updated in seconds since 1970-01-01. |
| xPos | int | The location of the chunk on the x-axis in chunk coordinates. |
| zPos | int | The location of the chunk on the z-axis in chunk coordinates. |

**Notice**
Running the query will check the ```.mca```-file's name first if the query would even apply to any chunk in this file using the xPos and zPos conditions, as long as the query is built in a way that allows doing this.


### Caching
The tool creates an image for each region from the provided mca-files. These images are saved separately inside a ```cache```-folder in the working directory of the program for future usage. Experience showed that a Minecraft world with a size of 10GB resulted in cached image files with a total size of 80-100MB. Caching as many regions as possible significantly improves loading times though.

---
## Supported Versions
The MCA Selector currently supports the following Minecraft versions:

| Minecraft Version | DataVersion | Supported |
| ----------------- | ----------- | :-------: |
| Beta 1.3 - 1.12.2 | 100 - 1343  | Yes       |
| 1.13              | 1444 - ?    | Yes       |

---

## Download and installation

[**Download Version 1.0**](https://github.com/Querz/mcaselector/releases/download/1.0/mcaselector-1.0.jar)

"Requirements":
* JRE 8+, you can get it from [HERE](http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html)
* A computer
* A brain

If ```.jar``` files are associated with java on your computer, it should launch by simply double-clicking the file (or however your OS is configured to open files using your mouse or keyboard). If not, you can try ```java -jar mcaselector-1.0.jar``` from your console. If this still doesn't work, apply "A brain" that you providently helt ready after having read the "Requirements" section carefully.
