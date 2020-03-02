# Building an .exe

### Before you start...

These instructions assume that you already have:

1) A jar file ready to go with dependancies. (To do that, run 'mvm clean package').
2) A copy of the 1.11 jre somewhere on your system.
3) Python 3.5+ installed on the computer
4) The build_exe\Application directory has two empty folders (jre, and Client). If this is not the case, you may wish to run the cleanup script first.

### Copy jre

In:
	\build_exe\Application\jre 

Copy the jre you want to bundle with this build (We are currently using 1.11). 

Please note that you should copy the contents of the jre. Ergo: '\build_exe\Application\jre\lib' should be a valid path.

### Run Python Script (package_script.py)

The Python Script copies over all necessary files to the 'build_exe\Application' folder. 
Also note that this script reads from the launch4j configurations file.

For best results, run the python script in ADMINISTRATOR mode. 
If you get file permission errors, try removing any file permissions on the relevant folders.

### Launch4j

Download and setup the Launch4j app. You can download it here: 
	http://launch4j.sourceforge.net/

As a quick hack, if the software complains about having the wrong JDK you maybe able to run jar file from the command line with a different JDK. 

### Configurations

When you open the launch4j app, load the "launch4j._build_configs.xml" file. 

Once you hit the cog icon you should get a log message:
	Successfully created ..\build_exe\Application\Client\CardBoard.exe

You can then press the blue arrow to check the process worked.
	
### Success !?

If this all works, you should be able to double-click the .exe in the application folder and having a working game.   
If something failed, you can always try again. The cleanup.py should help you get back to where you started.

The next step would be to zip the application folder and upload. 