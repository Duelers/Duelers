# Building an .exe

## Instructions

These instructions assume that you already have:

1) a jar file ready to go with dependancies. To do that, run 'mvm clean package'.
2) a copy of the 1.8.0_221 jre somewhere on your system.
3) Python 3.5+ installed on the computer
4) The build_exe\Application directory just have two empty folders (jre, and Client)

### Copy jre

In build_exe\Application\jre copy the jre you want to bundle with this build. 

We are currently using 1.8.0_221

### Run Python Script

The Python Script copies over all necessary files to the 'build_exe\Application' folder. 
Note that this script reads from the launch4j configurations file.

For best results, run the python script in ADMINISTRATOR mode. If you get file permission errors, try removing any file permissions on the relevant folders.

#### Launch4j

The first step is to download and setup the Launch4j app. 
You can download it here: http://launch4j.sourceforge.net/

As a quick hack, if the software complains about having the wrong JDK you maybe able to run jar file from the command line with a different JDK. 

### Configurations

When you open the launch4j app, load the "launch4j._build_configs.xml" file. 
You may need to change some of the paths (especially the link to the jre)

Note that all relative paths are relative in relation to the directory of the config file itself.

Once you hit the cog icon you should get a log message:
	Successfully created ..\build_exe\Application\Client\CardBoard.exe
	
### Sucess !?

If this all works, you should be able to double-click the .exe in the application folder and having a working game.   

