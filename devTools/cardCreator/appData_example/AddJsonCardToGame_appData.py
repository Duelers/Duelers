import os
import json
from typing import Dict, Any

from ValidateCard_copy import validate_card

SCRIPT_PATH = os.path.dirname(os.path.abspath(__file__))

IMPORT_DIRECTORY = os.path.join(SCRIPT_PATH, "Custom_Cards")

def load_json(fullpath: str) -> Dict[Any, Any]:
    with open(fullpath) as f:
        data = json.load(f)
    return data

def main(card: str, card_path: str) -> None:
    
    print("loading json...")
    data = load_json(card_path)
    
    is_custom = True
    validate_card(card, data, is_custom)
    
if __name__ == "__main__":

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

    print("SCRIPT COMPLETE -- If you see this message no errors were detected :)")
