import os
import json
import shutil
import copy
from typing import Dict, Any, List

SCRIPT_PATH = os.path.dirname(os.path.abspath(__file__))
RESOURCES_SERVER_DIR = os.path.abspath(os.path.join(SCRIPT_PATH, "..", "..", "Server", "resources"))

ACCOUNTS_DIR = os.path.join(RESOURCES_SERVER_DIR, "accounts")

accname = "duelyst"
passw = "duel"

if __name__ == "__main__":

    assert os.path.isdir(RESOURCES_SERVER_DIR), "ERROR: {RESOURCES_SERVER_PATH} is not a valid directory!"
    assert os.path.isdir(ACCOUNTS_DIR), "ERROR: {ACCOUNT_PATH} is not a valid directory!"

    account_path = os.path.join(ACCOUNTS_DIR, accname + ".account.json")
    template_path = os.path.join(ACCOUNTS_DIR, "template.account.json")

    main_deck = decks = None 
    if os.path.exists(account_path):
        with open(account_path, "r") as f:
            ac_data = json.load(f)

            main_deck = ac_data.get("mainDeckName", None)
            decks = ac_data.get("decks", None)

        os.remove(account_path)


    with open(template_path) as f:
        data = json.load(f)
    

    data["username"] = accname
    data["password"] = passw
    
    if main_deck and decks:
        data["mainDeckName"] = main_deck
        data["decks"] = decks

    with open(account_path, "w") as jsonFile:
        json.dump(data, jsonFile, indent=4)
