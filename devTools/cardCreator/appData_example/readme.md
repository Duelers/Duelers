## Importing Custom Cards
 
In what follows the "config directory" path refers to one of these locations, depending on your operating system.

* MacOSX: /Users/yourusername/Library/Application Support/cardboard/1.0
* Windows: C:\Users\yourusername\AppData\Roaming\projectcardboard\cardboard\1.0
* Linux: /home/yourusername/.config/cardboard/1.0

## Basic overview

Step (1): Design the card in json format
Step (2): Add the card to the "Custom_Cards" directory
Step (3): Open up the command line/powershell and run the .exe
	cmd chdir <this directory>
	cmd ./AddJsonCardToGame_appData.exe
Step (4): If the .exe highlights any errors, try to correct them. Otherwise move directly to step(5).
Step (5): Add the json card to the "Custom_Cards" and and any art (eg spritesheets, plists) to "Custom_Cards_Sprites". Both of these folders need to be in the config directory (if this is your first custom card, you will need to create these directories).
Step (6): Connect to "local host". You can do this by replacing contents of the config.properties file (found in the config directory) with the local host example found in docs/configs.
Step (7): Load the game. On login screen, confirm that the server does say "local host"
Step (8): Try to open the collection. If this fails, that means there maybe additional errors with the custom card that the .exe failed to find. 
