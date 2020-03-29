from os import name
from typing import Dict, Any
from distutils.util import strtobool


SUPPORTED_CARD_TYPES = ["HERO", "MINION", "SPELL"]
FACTIONS = ["songhai", "vetruvian", "lyonar", "abyssian", "magmar", "vanar", "mercenary"]

def is_name_valid(filename: str, card_type: str, cardId: str) -> bool:
    check_1 = filename.endswith(f".{card_type.lower()}.card.json")
    check_2 = filename.startswith(cardId)
    
    return check_1 and check_2

def validate_card(filename: str, card: Dict[Any, Any], is_custom=True) -> None:

    card_type = card["type"]
    assert card_type in SUPPORTED_CARD_TYPES, f"ERROR: '{card_type}' is not a supported card type."

    card_id   = card["cardId"]
    assert (" " not in card_id), "ERROR: Spaces should not be in cardId field"
    
    card_faction = card["faction"]
    assert card_faction in FACTIONS, f"ERROR {card_faction}: is not a valid faction (case sensitive)."
    
    card_sprite = card["spriteName"]
    assert (not card_sprite.endswith(".png")), "ERROR: SpriteName should not endwith '.png'."  

    assert is_name_valid(filename, card_type, card_id), f"ERROR: '{filename}' does not follow naming convention. Filename should be: {card_id}.{card_type}.card.json"

    card_custom: str = card.get("isCustom", "False")
    assert strtobool(card_custom) == is_custom, f"Error isCustom should be set to '{is_custom}', but was '{card_custom}''"
