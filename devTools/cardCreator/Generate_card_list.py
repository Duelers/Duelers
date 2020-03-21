import os
import pandas as pd

from AddJsonCardToGame import load_json, INFO_FILE

"""
Creates a CSV of all the cards we are about to import to the game.
"""

SCRIPT_PATH = os.path.dirname(os.path.abspath(__file__))

IMPORT_DIR: str = load_json(INFO_FILE).get("import_directory", "Error")
OUTPUT_FILEPATH = os.path.join(SCRIPT_PATH, "card_list.csv")


def main():
    assert os.path.isdir(IMPORT_DIR), "Error, import dir is empty"

    print("Attempted to create 'imported cards' csv file")

    cards = [f for f in os.listdir(IMPORT_DIR) if f.endswith(".json")]

    print(f"Reading {len(cards)} json files")
    dict_of_cards = dict()
    for idx, card in enumerate(cards):
        card_path = os.path.join(IMPORT_DIR, card)
        
        assert os.path.isfile(card_path), card_path
        
        data = load_json(card_path)
        dict_of_cards[idx] = data

    print("Creating pandas dataframe")
    db_tmp = pd.DataFrame(dict_of_cards).T

    card_database = pd.DataFrame(db_tmp, columns = ["type", "name", "description", "defaultAp", "defaultHp", "manaCost", "spriteName", "fxName"])
    card_database.rename(columns={'defaultAp':'attack','defaultHp':'health','manaCost':'cost'}, inplace=True)

    card_database.fillna("n/a", inplace=True)
    card_database.set_index(['type', "name"], inplace=True)
    card_database.sort_values(by=['type', "name"])

    print("Saving dataframe to csv")
    card_database.to_csv(OUTPUT_FILEPATH)
    print(f"csv saved at {OUTPUT_FILEPATH}")

if __name__ == "__main__":  
    main()