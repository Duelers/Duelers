package models.languageLocalisation;

/**
 * Possible keys in a language json file.
 */
public final class LanguageKeys {

    public static final String LANGUAGE_ENGLISH_TITLE = "LANGUAGE_ENGLISH_TITLE";
    public static final String LANGUAGE_NATIVE_TITLE = "LANGUAGE_NATIVE_TITLE";

    // ------- LOGIN MENU -------
    public static final String LOGIN_MENU = "LOGIN_MENU";
    // Login Menu subkeys...
    public static final String WELCOME_MESSAGE = "WELCOME_MESSAGE";
    public static final String VERSION = "VERSION";
    public static final String LOGIN = "LOGIN";
    public static final String REGISTER = "REGISTER";
    public static final String USERNAME = "USERNAME";
    public static final String PASSWORD = "PASSWORD";
    public static final String ERROR_SHORT_USERNAME = "ERROR_SHORT_USERNAME";
    public static final String ERROR_SHORT_PASSWORD = "ERROR_SHORT_PASSWORD";
    public static final String ERROR_INCORRECT_PASSWORD = "ERROR_INCORRECT_PASSWORD";

    // ------- MAIN MENU -------
    public static final String MAIN_MENU = "MAIN_MENU";
    // Main Menu subkeys...
    public static final String PLAY = "PLAY";
    public static final String COLLECTION = "COLLECTION";
    public static final String CHAT = "CHAT";
    public static final String SPECTATE = "SPECTATE";
    public static final String PROFILE = "PROFILE";
    public static final String QUIT = "QUIT";

    // ------- COLLECTION -------
    public static final String COLLECTION_MENU = "COLLECTION_MENU";
    // Collection subkeys...
    public static final String NEW_DECK = "NEW_DECK";
    public static final String IMPORT_DECK = "IMPORT_DECK";
    public static final String CARDS = "CARDS";
    public static final String HERO = "HERO";
    public static final String DECK_NAME_PROMPT = "DECK_NAME_PROMPT";
    public static final String DECK_NAME = "DECK_NAME";
    public static final String DECK_CARD_ADD_ERROR = "DECK_CARD_ADD_ERROR";
    public static final String DECK_CARD_REMOVE_ERROR = "DECK_CARD_REMOVE_ERROR";

    // ------- Spectate -------
    public static final String SPECTATE_MENU = "SPECTATE_MENU";
    // Spectate subkeys...
    public static final String ONLINE_GAMES = "ONLINE_GAMES";
    public static final String INDEX = "INDEX";
    public static final String PLAYER_ONE = "PLAYER_ONE";
    public static final String PLAYER_TWO = "PLAYER_TWO";
    public static final String GAME_TYPE = "GAME_TYPE";

    // ------- PROFILE -------
    public static final String PROFILE_MENU = "PROFILE_MENU";
    // Profile subkeys...
    public static final String MATCH_HISTORY = "MATCH_HISTORY";
    public static final String LOGOUT = "LOGOUT";

    // ------- BUTTON TEXT -------
    public static final String BUTTON_TEXT = "BUTTON_TEXT";
    // Button test subkeys...
    public static final String OK = "OK";
    public static final String START = "START";
    public static final String ADD = "ADD";
    public static final String CREATE = "CREATE";
    public static final String CANCEL = "CANCEL";
    public static final String REMOVE = "REMOVE";
    public static final String BACK = "BACK";
    public static final String ERROR = "ERROR";

    private LanguageKeys() {
    }
}