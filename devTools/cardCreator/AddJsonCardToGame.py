import os
import json
import shutil
from typing import Dict, Any

import Generate_card_list
from ValidateCard import validate_card

SCRIPT_PATH = os.path.dirname(os.path.abspath(__file__))

RESOURCES_DIR = os.path.abspath(os.path.join(SCRIPT_PATH, "..", "..", "resources"))
RESOURCES_SERVER_DIR = os.path.abspath(os.path.join(SCRIPT_PATH, "..", "..", "Server", "resources"))

IMPORT_DIRECTORY = os.path.join(SCRIPT_PATH, "cards_to_import")

RESOURCES_SUBFOLDER_NAMES = ["heroCards", "minionCards", "spellCards"]
SUPPORTED_CARD_TYPES = ["HERO", "MINION", "SPELL"]

subfolder_for_type = dict(zip(SUPPORTED_CARD_TYPES, RESOURCES_SUBFOLDER_NAMES)) 

def load_json(fullpath: str) -> Dict[Any, Any]:
    with open(fullpath) as f:
        data = json.load(f)
    return data

def main(card: str, card_path: str) -> None:
    
    print("loading json...")
    data = load_json(card_path)
    
    is_custom = False
    validate_card(card, data, is_custom)

    card_type = data["type"]
    loc_1 = os.path.join(RESOURCES_DIR, subfolder_for_type[card_type])
    print(f"attempting to copy card to dir: {loc_1}")
    shutil.copy(card_path, loc_1)

    loc_2 = os.path.join(RESOURCES_SERVER_DIR, subfolder_for_type[card_type])
    print(f"attempting to copy card to dir: {loc_2}")
    shutil.copy(card_path, loc_2)


if __name__ == "__main__":

    print("+===========================================+")
    print("|    NEW CARD INSTALLER SCRIPT ver1.0       |")
    print("+===========================================+")

    assert os.path.isdir(RESOURCES_DIR), "ERROR: {RESOURCES_PATH} is not a valid directory!"
    assert os.path.isdir(RESOURCES_SERVER_DIR), "ERROR: {RESOURCES_SERVER_PATH} is not a valid directory!"

    import_card_path =  os.path.abspath(IMPORT_DIRECTORY)
    assert os.path.isdir(import_card_path), f"ERROR: {import_card_path} is not a valid directory!"
    print(f"Looking for cards in: {import_card_path}")

    cards = []
    for f in os.listdir(import_card_path):
        if f.endswith(".card.json"):
            cards.append(f)
    assert cards, f"ERROR: no cards were found in {import_card_path}"
    
    print(f"Number of cards Found: {len(cards)}")

    for c in cards:
        card_path = os.path.join(import_card_path, c)
        print("---------------------------------------")
        print(f"Starting import process for Card: {c}")
        main(c, card_path)
        print(f"Finished import process for Card: {c}\n")

    
    # csv list of cards.
    Generate_card_list.main()

    print("SCRIPT COMPLETE -- If you see this message no errors were detected :)")
