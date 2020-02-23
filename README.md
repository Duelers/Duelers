# CardBoard

CardBoard is an open-source project to build a multiplayer collectable-card-game with units that move freely on a battlefield. 
The name is a terrible pun; the game has cards, and its played on a board.

The core tech-stack is Java8(Fx) with Python used for scripting and Maven for our build automation. If you are interested in contributing please drop us a message.

### Screenshot

![Gameplay Screenshot](promoScreenshot.png)

### Building and installing with Maven

1. Import as a mavan project (you will need the maven-plugin to do this)
2. Run the command: mvm clean package
3. In Client/target there should be a file ending with "...jar-with-dependencies.jar". 
4. Run the above file and enjoy playing the game!

### Building an Executable (Windows)

* Please see the readme in the 'build_exe' directory for instructions.

### Original Project

The Original 'Duelers' Project was created by:

	1. Ahmad Salimi
	2. MohammadMahdi Jarrahi
	3. Mohammad Hadi Esnaashari

...And has been copied under the MIT license.

The original repo can be found here: https://github.com/aps2019project/Duelers

### NOTES:

* Sound effects just work on Windows. On Linux you need to install Glib.