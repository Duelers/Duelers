package server.clientPortal.models.message;

import server.Server;
import server.clientPortal.models.JsonConverter;
import server.clientPortal.models.comperessedData.CompressedCard;
import server.dataCenter.models.account.Account;
import server.dataCenter.models.account.AccountType;
import server.dataCenter.models.account.Collection;
import server.dataCenter.models.card.Card;
import server.dataCenter.models.card.ExportedDeck;
import server.dataCenter.models.card.spell.AvailabilityType;
import server.gameCenter.models.game.*;
import server.gameCenter.models.map.Cell;

import java.util.List;
import java.util.Set;

public class Message {
    private MessageType messageType;
    //serverName || clientName
    private String sender;
    private String receiver;

    //SENDER:SERVER
    private GameCopyMessage gameCopyMessage;
    private CardsCopyMessage cardsCopyMessage;
    private AccountCopyMessage accountCopyMessage;
    private CardPositionMessage cardPositionMessage;
    private TroopUpdateMessage troopUpdateMessage;
    private GameUpdateMessage gameUpdateMessage;
    private ExceptionMessage exceptionMessage;
    private OpponentInfoMessage opponentInfoMessage;
    private GameFinishMessage gameFinishMessage;
    private GameAnimations gameAnimations;
    private OnlineGame[] onlineGames;
    //SENDER:CLIENT
    private String cardName;
    private GetDataMessage getDataMessage;
    private OtherFields otherFields;
    private ExportedDeck exportedDeck;
    private AccountFields accountFields;
    private OnlineGame onlineGame;
    //SENDER:DUAL
    private Card card;
    private String cardID;
    private ChatMessage chatMessage;
    private NewGameFields newGameFields;
    private ChangeCardNumber changeCardNumber;
    private ChangeAccountType changeAccountType;
    private CompressedCard compressedCard;


    private Message(String receiver) {
        this.sender = Server.getInstance().serverName;
        this.receiver = receiver;
    }

    public static Message convertJsonToMessage(String messageJson) {
        return JsonConverter.fromJson(messageJson, Message.class);
    }

    public static Message makeDoneMessage(String receiver) {
        Message message = new Message(receiver);
        message.messageType = MessageType.DONE;
        return message;
    }

    public static Message makeGameCopyMessage(String receiver, Game game) {
        Message message = new Message(receiver);
        message.gameCopyMessage = new GameCopyMessage(game);
        message.messageType = MessageType.GAME_COPY;
        return message;
    }

    public static Message makeOriginalCardsCopyMessage(String receiver, Collection originalCards) {
        Message message = new Message(receiver);
        message.cardsCopyMessage = new CardsCopyMessage(originalCards);
        message.messageType = MessageType.ORIGINAL_CARDS_COPY;
        return message;
    }

    public static Message makeAccountCopyMessage(String receiver, Account account) {
        Message message = new Message(receiver);
        message.accountCopyMessage = new AccountCopyMessage(account);
        message.messageType = MessageType.ACCOUNT_COPY;
        return message;
    }

    public static Message makeChangeCardPositionMessage(String receiver, Card card, CardPosition cardPosition) {
        Message message = new Message(receiver);
        message.cardPositionMessage = new CardPositionMessage(card, cardPosition);
        message.messageType = MessageType.CARD_POSITION;
        return message;
    }

	// Client
    public static Message makeAttackMessage(String receiver, String myCardId, String opponentCardId) {
        Message message = new Message(receiver);
        message.otherFields = new OtherFields();
        message.otherFields.setMyCardId(myCardId);
        message.otherFields.setOpponentCardId(opponentCardId);
        message.messageType = MessageType.ATTACK;
        return message;
    }

    public static Message makeAttackMessage(String receiver, Troop attacker, Troop defender, boolean counterAttack) {
        Message message = new Message(receiver);
        message.gameAnimations = new GameAnimations();
        message.gameAnimations.addAttacks(attacker.getCard().getCardId(), defender.getCard().getCardId());
        if (counterAttack)
            message.gameAnimations.addCounterAttacks(defender.getCard().getCardId(), attacker.getCard().getCardId());
        message.messageType = MessageType.ANIMATION;
        return message;
    }

    public static Message makeNewNextCardSetMessage(String receiver, CompressedCard nextCard) {
        Message message = new Message(receiver);
        message.messageType = MessageType.SET_NEW_NEXT_CARD;
        message.compressedCard = nextCard;
        return message;
    }

    public static Message makeSpellMessage(String receiver, Set<Cell> cells, AvailabilityType availabilityType) {
        Message message = new Message(receiver);
        message.gameAnimations = new GameAnimations();
        message.gameAnimations.addSpellAnimation(cells, availabilityType);
        message.messageType = MessageType.ANIMATION;
        return message;
    }

	// Client
    public static Message makeMoveTroopMessage(String receiver, String cardId, Cell cell) {
        Message message = new Message(receiver);
        message.otherFields = new OtherFields();
        message.otherFields.setMyCardId(cardId);
        message.otherFields.setCell(cell);
        message.messageType = MessageType.MOVE_TROOP;
        return message;
    }

	// Client
    public static Message makeInsertMessage(String receiver, String cardId, Cell cell) {
        Message message = new Message(receiver);
        message.otherFields = new OtherFields();
        message.otherFields.setMyCardId(cardId);
        message.otherFields.setCell(cell);
        message.messageType = MessageType.INSERT;
        return message;
    }

	// Client
    public static Message makeStopShowGameMessage(String receiver, OnlineGame onlineGame) {
        Message message = new Message(receiver);
        message.onlineGame = onlineGame;
        message.messageType = MessageType.STOP_SHOW_GAME;
        return message;
    }

    public static Message makeTroopUpdateMessage(String receiver, Troop troop) {
        Message message = new Message(receiver);
        message.troopUpdateMessage = new TroopUpdateMessage(troop);
        message.messageType = MessageType.TROOP_UPDATE;
        return message;
    }

    public static Message makeGameUpdateMessage(String receiver, int turnNumber, int player1CurrentMP, int player2CurrentMP,
                                                List<CellEffect> cellEffects) {
        Message message = new Message(receiver);
        message.gameUpdateMessage = new GameUpdateMessage(turnNumber, player1CurrentMP,
                player2CurrentMP,  cellEffects);
        message.messageType = MessageType.GAME_UPDATE;
        return message;
    }

    public static Message makeExceptionMessage(String receiver, String exceptionString) {
        Message message = new Message(receiver);
        message.exceptionMessage = new ExceptionMessage(exceptionString);
        message.messageType = MessageType.SEND_EXCEPTION;
        return message;
    }

    public static Message makeChatMessage(String receiver, String messageSender, String messageReceiver,
                                          String textMessage) {
        Message message = new Message(receiver);
        message.chatMessage = new ChatMessage(messageSender, messageReceiver, textMessage);
        message.messageType = MessageType.CHAT;
        return message;
    }

    public static Message makeAccountInfoMessage(String receiver, Account opponent) {
        Message message = new Message(receiver);
        message.opponentInfoMessage = new OpponentInfoMessage(opponent);
        message.messageType = MessageType.OPPONENT_INFO;
        return message;
    }

	// Client
    public static Message makeForceFinishGameMessage(String receiver) {
        Message message = new Message(receiver);
        message.messageType = MessageType.FORCE_FINISH;
        return message;
    }

    public static Message makeGameFinishMessage(String receiver, boolean youWon, int reward) {
        Message message = new Message(receiver);
        message.gameFinishMessage = new GameFinishMessage(youWon, reward);
        message.messageType = MessageType.Game_FINISH;
        return message;
    }

	// Client
    public static Message makeEndTurnMessage(String receiver) {
        Message message = new Message(receiver);
        message.messageType = MessageType.END_TURN;
        return message;
    }

    public static Message makeInvitationMessage(String receiver, String inviterUsername, GameType gameType) {
        Message message = new Message(receiver);
        message.messageType = MessageType.INVITATION;
        message.newGameFields = new NewGameFields(gameType, 0, null, inviterUsername);
        return message;
    }

	// Client
    public static Message makeAcceptRequestMessage(String receiver, String opponentUsername) {
        Message message = new Message(receiver);
        message.messageType = MessageType.ACCEPT_REQUEST;
        message.newGameFields = new NewGameFields();
        message.newGameFields.setOpponentUsername(opponentUsername);
        return message;
    }

    public static Message makeAcceptRequestMessage(String receiver) {
        Message message = new Message(receiver);
        message.messageType = MessageType.ACCEPT_REQUEST;
        return message;
    }

	// Client
    public static Message makeDeclineRequestMessage(String receiver, String opponentUsername) {
        Message message = new Message(receiver);
        message.messageType = MessageType.DECLINE_REQUEST;
        message.newGameFields = new NewGameFields();
        message.newGameFields.setOpponentUsername(opponentUsername);
        return message;
    }

    public static Message makeDeclineRequestMessage(String receiver) {
        Message message = new Message(receiver);
        message.messageType = MessageType.DECLINE_REQUEST;
        return message;
    }

    public static Message makeChangeCardNumberMessage(String receiver, Card card, int newValue) {
        Message message = new Message(receiver);
        message.changeCardNumber = new ChangeCardNumber(card.getName(), newValue);
        message.messageType = MessageType.CHANGE_CARD_NUMBER;
        return message;
    }

    public static Message makeAddOriginalCardMessage(String receiver, Card card) {
        Message message = new Message(receiver);
        message.card = card;
        message.messageType = MessageType.ADD_TO_ORIGINALS;
        return message;
    }

	// Client
    public static Message makeSetNewNextCardMessage(String receiver){
        Message message = new Message(receiver);
        message.messageType = MessageType.SET_NEW_NEXT_CARD;
        return message;
    }

	// Client
    public static Message makeNewReplaceCardMessage(String serverName, String cardID) {
        Message message = new Message(serverName);
        message.messageType = MessageType.REPLACE_CARD;
        message.cardID = cardID;
        return message;
    }

    public static Message makeOnlineGamesCopyMessage(String receiver, OnlineGame[] onlines) {
        Message message = new Message(receiver);
        message.onlineGames = onlines;
        message.messageType = MessageType.ONLINE_GAMES_COPY;
        return message;
    }

	// Client
    public static Message makeCancelRequestMessage(String receiver) {
        Message message = new Message(receiver);
        message.messageType = MessageType.CANCEL_REQUEST;
        return message;
    }

	// Client
    public static Message makeNewCustomGameMessage(String receiver, GameType gameType, String customDeckName) {
        Message message = new Message(receiver);
        message.newGameFields = new NewGameFields();
        message.newGameFields.setCustomDeckName(customDeckName);
        message.newGameFields.setGameType(gameType);
        message.messageType = MessageType.NEW_DECK_GAME;
        return message;
    }

	// Client
    public static Message makeLogInMessage(String receiver, String userName, String passWord) {
        Message message = new Message(receiver);
        message.accountFields = new AccountFields(userName, passWord);
        message.messageType = MessageType.LOG_IN;
        return message;
    }

    public static Message makeRegisterMessage(String receiver, String userName, String passWord) {
        Message message = new Message(receiver);
        message.accountFields = new AccountFields(userName, passWord);
        message.messageType = MessageType.REGISTER;
        return message;
    }

    public static Message makeRequestOnlineGameShowMessage(String receiver, OnlineGame onlineGame) {
        Message message = new Message(receiver);
        message.onlineGame = onlineGame;
        message.messageType = MessageType.ONLINE_GAME_SHOW_REQUEST;
        return message;
    }

    public static Message makeImportDeckMessage(String receiver, ExportedDeck exportedDeck) {
        Message message = new Message(receiver);
        message.exportedDeck = exportedDeck;
        message.messageType = MessageType.IMPORT_DECK;
        return message;
    }

    public static Message makeGetDataMessage(String receiver, DataName dataName) {
        Message message = new Message(receiver);
        message.getDataMessage = new GetDataMessage(dataName);
        message.messageType = MessageType.GET_DATA;
        return message;
    }

    public static Message makeSelectDeckMessage(String receiver, String deckName) {
        Message message = new Message(receiver);
        message.otherFields = new OtherFields();
        message.otherFields.setDeckName(deckName);
        message.messageType = MessageType.SELECT_DECK;
        return message;
    }

    public static Message makeRemoveDeckMessage(String receiver, String deckName) {
        Message message = new Message(receiver);
        message.otherFields = new OtherFields();
        message.otherFields.setDeckName(deckName);
        message.messageType = MessageType.REMOVE_DECK;
        return message;
    }

    public static Message makeRemoveCardFromDeckMessage(String receiver, String deckName, String cardId) {
        Message message = new Message(receiver);
        message.otherFields = new OtherFields();
        message.otherFields.setDeckName(deckName);
        message.otherFields.setMyCardId(cardId);
        message.messageType = MessageType.REMOVE_FROM_DECK;
        return message;
    }

    public static Message makeAddCardToDeckMessage(String receiver, String deckName, String cardId) {
        Message message = new Message(receiver);
        message.otherFields = new OtherFields();
        message.otherFields.setDeckName(deckName);
        message.otherFields.setMyCardId(cardId);
        message.messageType = MessageType.ADD_TO_DECK;
        return message;
    }

    public static Message makeLogOutMessage(String receiver) {
        Message message = new Message(receiver);
        message.messageType = MessageType.LOG_OUT;
        return message;
    }

    public static Message makeChangeAccountTypeMessage(String receiver, String username, AccountType newValue) {
        Message message = new Message(receiver);
        message.changeAccountType = new ChangeAccountType(username, newValue);
        message.messageType = MessageType.CHANGE_ACCOUNT_TYPE;
        return message;
    }

    public static Message makeCreateDeckMessage(String receiver, String deckName) {
        Message message = new Message(receiver);
        message.otherFields = new OtherFields();
        message.otherFields.setDeckName(deckName);
        message.messageType = MessageType.CREATE_DECK;
        return message;
    }

    public static Message makeMultiPlayerGameReQuestMessage(String receiver, GameType gameType, String opponentUsername) {
        Message message = new Message(receiver);
        message.newGameFields = new NewGameFields();
        message.newGameFields.setOpponentUsername(opponentUsername);
        message.newGameFields.setGameType(gameType);
        message.messageType = MessageType.MULTIPLAYER_GAME_REQUEST;
        return message;
    }


    public String toJson() {
        return JsonConverter.toJson(this);
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public GetDataMessage getGetDataMessage() {
        return getDataMessage;
    }

    public OtherFields getOtherFields() {
        return otherFields;
    }

    public AccountFields getAccountFields() {
        return accountFields;
    }

    public NewGameFields getNewGameFields() {
        return newGameFields;
    }

    public Card getCard() {
        return card;
    }

    public ExportedDeck getExportedDeck() {
        return exportedDeck;
    }

    public ChatMessage getChatMessage() {
        return chatMessage;
    }

    public ChangeCardNumber getChangeCardNumber() {
        return changeCardNumber;
    }

    public ChangeAccountType getChangeAccountType() {
        return changeAccountType;
    }

    public String getCardName() {
        return cardName;
    }

    public OnlineGame getOnlineGame() {
        return onlineGame;
    }

    public String getCardID() {
        return cardID;
    }

    public GameFinishMessage getGameFinishMessage() {
        return gameFinishMessage;
    }

    public GameCopyMessage getGameCopyMessage() {
        return gameCopyMessage;
    }

    public CardPositionMessage getCardPositionMessage() {
        return cardPositionMessage;
    }

    public TroopUpdateMessage getTroopUpdateMessage() {
        return troopUpdateMessage;
    }

    public CompressedCard getCompressedCard() {
        return compressedCard;
    }

    public GameUpdateMessage getGameUpdateMessage() {
        return gameUpdateMessage;
    }

    public GameAnimations getGameAnimations() {
        return gameAnimations;
    }

    public OnlineGame[] getOnlineGames() {
        return onlineGames;
    }

    public ExceptionMessage getExceptionMessage() {
        return exceptionMessage;
    }

    public AccountCopyMessage getAccountCopyMessage() {
        return accountCopyMessage;
    }


}
