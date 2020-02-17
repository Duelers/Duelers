# Card Creation and Import Guide

## You will need: 

* Test Editor (that can read Json)
* [OPTIONAL] Python installed on your computer

For text editors I recommend Notepad++, with the 'JSTool' plugin installed (this plugin will allow you to test and format Json files).
If you do not have python then use the 'AddJsonCardToGame.exe' instead.

## What does the script/exe do?

The 'AddJsonCardToGame.py' script currently does the following:

* Loads info.json to determine (a) what account do add the card(s) to and (b) set the directory for the cards.
* If you do not edit the info.json file then the program will default to importing the 'cards_to_import' directory.
* For each card it finds it checks that the file follows the current conventions.
* Adds the card in both the Server/Resources and Resources directory.
* Attempts to add 3x copies (or 1x copy for Hero cards) to a account (found in Server/Resources/accounts).
* If a card with the same cardId is found IT WILL BE OVERWRITTEN

The .exe does exactly the same thing, its a much larger file because it runs without Python.

## Info.json

The info.json has two properties; where to look for the cards and what account to add them to.
By default, we search for cards in the 'cards_to_import' directory and add them to the 'duelyst' account.

## Instructions 

* design a new card, and add it in the "cards_to_import" directory (or, if you changed it, the directory specified in info.json. 
* Run the script  (ideally from the command line, if you double-click the .exe you won't see error messages)
* Assuming you have no errors, run the game and login as the account mentioned in info.json (this defaults to "duelyst").
* Go to collection, add your new card to decks and playtest it.
* If you are happy with the card then the next is to sumbit it:
	* Developers can 'submit' the card by creating a pull request using Git. 
	* Other users can post the card.json file on Discord and some friendly dev will come along and do the rest for you. 

## Conventions/Rules to follow

Cards need to follow conventions. If they do not abide by the rules then we cannot add them. 

Here is a (non-exhaustive) list of card creation rules:

* All cards should follow the {cardname}.{cardType}.card.json naming convention. Examples include:

	* BloodshardGolem.minion.card.json
	* ArgeonHighmayne.hero.card.json
	* SpiralTechnique.spell.card.json
	
* CardId should be same as card name, but with spaces removed. For example:

	"name": "Auryn Nexus",
	"cardId": "AurynNexus"

* card 'type' must be one of "HERO", "MINION", "SPELL" (case sensitive)
* the "spellId" field should accuratly describe what the effect does. This will often be different than the card description. 

## Card Creation Tips

* Instead of starting with an empty page, its probably a good idea to use an existing card as a template.
* The description field is the card text.
* In the "spriteName" field look for the correct png in the Client/Resources/troopAnimations and Client/Resources/icons directories for the correct sprite.
* Be aware of technical limitations, "healing" effects are currenly difficult to do, and there is no current ability to add tribe date (e.g. golem, Arcaynst, Dervish, etc).
* Use pre-existing cards as inspiration: For example, I was able to create the "spiral technique" spell in about 5 minutes of work because that card is just a bigger version of a card I had already created (i.e. 'Pheonix Fire')
* Other card examples can be found in the Client/Resources directory. 
* If you get an error when running the python script, please do read the error message. In many cases, these messages will indicate why the import process failed.
* 'CardTriggerNotes' has some description about what different things do. Its not complete, be sure to add to it if you discover something!

## Fin! 