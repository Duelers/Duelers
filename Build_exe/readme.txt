# Building an .exe

## Instructions

These instructions assume that you already have:

1) a jar file ready to go with dependancies. To do that, run 'mvm clean package'.
2) a copy of the 1.8.0_221 jre somewhere on your system.


#### Launch4j

The first step is to download and setup the Launch4j app. 
You can download it here: http://launch4j.sourceforge.net/

When you open the app, load the launch4j._build_configs.xml file. You may need to change some of the paths (especially the link to the jre)
Note that all relative paths are relative in relation to the directory of the config file itself.

