import os
import json
import shutil
import copy
from typing import Dict, Any, List

SCRIPT_PATH = os.path.dirname(os.path.abspath(__file__))

RESOURCES_DIR = os.path.abspath(os.path.join(SCRIPT_PATH, "..", "..", "resources"))
RESOURCES_SERVER_DIR = os.path.abspath(os.path.join(SCRIPT_PATH, "..", "..", "Server", "resources"))

ACCOUNTS_DIR = os.path.join(RESOURCES_SERVER_DIR, "accounts")

INFO_FILE = os.path.join(SCRIPT_PATH, "info.json")

RESOURCES_SUBFOLDER_NAMES = ["heroCards", "minionCards", "spellCards"]
SUPPORTED_CARD_TYPES = ["HERO", "MINION", "SPELL"]

subfolder_for_type = dict(zip(SUPPORTED_CARD_TYPES, RESOURCES_SUBFOLDER_NAMES)) 

def load_json(fullpath: str) -> Dict[Any, Any]:
    with open(fullpath) as f:
        data = json.load(f)
    return data

def is_name_valid(filename: str, card_type: str, cardId: str) -> bool:
    check_1 = filename.endswith(f".{card_type.lower()}.card.json")
    check_2 = filename.startswith(cardId)
    
    return check_1 and check_2

def get_card_id_for_account(account_name: str, card_id: str, i: int) -> str:
    acc_name = account_name.replace(" ", "")
    return f"{acc_name}_{card_id}_{i}"

def make_copies(ids: List[str], data: Dict[Any, Any]) -> List[Dict[Any,Any]]:

    c1 = data.copy() 
    c1["cardId"] = ids[0]

    if len(ids) == 1:
        return [c1]
    
    else:
        c2 = data.copy()
        c3 = data.copy() 

        c2["cardId"] = ids[1]
        c3["cardId"] = ids[2]

        return [c1, c2, c3]

def main(card: str, card_path: str, account_path: str) -> None:
    
    print("loading json...")
    data = load_json(card_path)

    card_type = data["type"]
    assert card_type in SUPPORTED_CARD_TYPES, f"ERROR: '{card_type}' is not a supported card type."

    card_id   = data["cardId"]
    assert (" " not in card_id), "ERROR: Spaces should not be in cardId field"

    assert is_name_valid(card, card_type, card_id), f"ERROR: '{card}' does not follow naming convention."

    loc_1 = os.path.join(RESOURCES_DIR, subfolder_for_type[card_type])
    print(f"attempting to copy card to dir: {loc_1}")
    shutil.copy(card_path, loc_1)

    loc_2 = os.path.join(RESOURCES_SERVER_DIR, subfolder_for_type[card_type])
    print(f"attempting to copy card to dir: {loc_2}")
    shutil.copy(card_path, loc_2)


    print(f"Attempting to add card to account")
    acc_json = load_json(account_path)
    username = acc_json["username"]
    
    if card_type != "HERO":
        print("Adding 3x copies...")
        ## Creates the new account id's for all three copies. Each new id follows the format: '{username}_{cardid}_{number}'
        acc_ids = [get_card_id_for_account(username, card_id, i) for i in range(1, 4)]
    else:
        print("Adding 1x copy...")
        acc_ids = [get_card_id_for_account(username, card_id, 1)]

    card_objects = make_copies(acc_ids, data)

    key = card_type.lower() + "s" if card_type in ["MINION", "SPELL"] else card_type.lower() + "es" ## minion(s), spell(s), hero(es)
    collection = acc_json["collection"][key]
    for idx, collected_card in enumerate(collection): 
        if collected_card["cardId"] in acc_ids:
            print(f"Card was found in account, overwriting")

            x_card =  acc_ids.index(collected_card["cardId"])
            collection[idx] = card_objects[x_card]
            
            acc_ids.pop(x_card)
            card_objects.pop(x_card) ## pop both to keep index in sync

    for copy in card_objects:
        collection.append(copy)

    print("Saving changes made to account")
    with open(account_path, "w") as jsonFile:
        json.dump(acc_json, jsonFile, indent=4)


if __name__ == "__main__":

    print("+===========================================+")
    print("|    NEW CARD INSTALLER SCRIPT ver1.0       |")
    print("+===========================================+")

    assert os.path.isdir(RESOURCES_DIR), "ERROR: {RESOURCES_PATH} is not a valid directory!"
    assert os.path.isdir(RESOURCES_SERVER_DIR), "ERROR: {RESOURCES_SERVER_PATH} is not a valid directory!"
    assert os.path.isdir(ACCOUNTS_DIR), "ERROR: {ACCOUNTS_DIR} is not a valid directory!"
    assert os.path.isfile(INFO_FILE), f"ERROR: {INFO_FILE} is not a valid file!"

    print("Loading Info.json...")
    info = load_json(INFO_FILE)
    account_name = info["account_name"]

    print(f"Checking '{account_name}' account exists...")
    account_path = os.path.join(ACCOUNTS_DIR, account_name + ".account.json")
    assert os.path.isfile(account_path), f"ERROR: '{account_path}' is not a file!"

    import_card_path =  os.path.abspath(info["import_directory"])
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
        main(c, card_path, account_path)
        print(f"Finished import process for Card: {c}\n")

    print("¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦")
    print("SCRIPT COMPLETE -- If you see this message no errors were detected :)")
