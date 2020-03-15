package server.clientPortal.models.message;

public enum MessageType {
    //SENDER:SERVER
    OPPONENT_INFO,
    SEND_EXCEPTION,
    ACCOUNT_COPY,
    GAME_COPY,
    ORIGINAL_CARDS_COPY,
    CARD_POSITION,
    TROOP_UPDATE,
    GAME_UPDATE,
    Game_FINISH,
    ANIMATION,
    INVITATION,
    ADD_TO_ORIGINALS,
    ONLINE_GAMES_COPY,
    //SENDER:CLIENT
    GET_DATA,
    MOVE_TROOP,
    CREATE_DECK,
    REMOVE_DECK,
    ADD_TO_DECK,
    REMOVE_FROM_DECK,
    SELECT_DECK,
    BUY_CARD,
    INSERT,
    ATTACK,
    END_TURN,
    AUTHENTICATE,
    LOG_OUT,
    MULTIPLAYER_GAME_REQUEST,
    CANCEL_REQUEST,
    NEW_DECK_GAME,
    SELECT_USER,
    SUDO,
    IMPORT_DECK,
    FORCE_FINISH,
    CHANGE_ACCOUNT_TYPE,
    STOP_SHOW_GAME,
    ONLINE_GAME_SHOW_REQUEST,
    SET_NEW_NEXT_CARD,
    ADD_TO_HAND,
    //SENDER:DUAL
    CHAT,
    ACCEPT_REQUEST,
    DECLINE_REQUEST,
    CHANGE_CARD_NUMBER,
    REPLACE_CARD,
    CLIENT_ID;
}