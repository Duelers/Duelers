package models.message;


import controller.Client;
import models.JsonConverter;
import models.account.AccountType;
import models.card.Card;
import models.card.ExportedDeck;
import models.comperessedData.CompressedCard;
import models.game.GameType;
import models.game.map.Position;

public class Message {
    private MessageType messageType;
    //serverName || clientName
    private String sender;
    private String receiver;

    //SENDER:SERVER
    private GameCopyMessage gameCopyMessage;
    private CardsCopyMessage cardsCopyMessage;
    private AccountCopyMessage accountCopyMessage;
    private LeaderBoardCopyMessage leaderBoardCopyMessage;
    private StoriesCopyMessage storiesCopyMessage;
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
    private ExportedDeck exportedDeck;
    private GetDataMessage getDataMessage;
    private OtherFields otherFields;
    private AccountFields accountFields;
    private OnlineGame onlineGame;
    //SENDER:DUAL
    private Card card;
    private CompressedCard compressedCard;
    private ChatMessage chatMessage;
    private NewGameFields newGameFields;
    private ChangeCardNumber changeCardNumber;
    private ChangeAccountType changeAccountType;


    private Message(String receiver) {
        this.sender = Client.getInstance().getClientName();
        this.receiver = receiver;
    }

    public static Message convertJsonToMessage(String messageJson) {
        return JsonConverter.fromJson(messageJson, Message.class);
    }

    public static Message makeGetDataMessage(String receiver, DataName dataName) {
        Message message = new Message(receiver);
        message.getDataMessage = new GetDataMessage(dataName);
        message.messageType = MessageType.GET_DATA;
        return message;
    }

    public static Message makeRegisterMessage(String receiver, String userName, String passWord) {
        Message message = new Message(receiver);
        message.accountFields = new AccountFields(userName, passWord);
        message.messageType = MessageType.REGISTER;
        return message;
    }

    public static Message makeImportDeckMessage(String receiver, ExportedDeck exportedDeck) {
        Message message = new Message(receiver);
        message.exportedDeck = exportedDeck;
        message.messageType = MessageType.IMPORT_DECK;
        return message;
    }

    public static Message makeLogInMessage(String receiver, String userName, String passWord) {
        Message message = new Message(receiver);
        message.accountFields = new AccountFields(userName, passWord);
        message.messageType = MessageType.LOG_IN;
        return message;
    }

    public static Message makeLogOutMessage(String receiver) {
        Message message = new Message(receiver);
        message.messageType = MessageType.LOG_OUT;
        return message;
    }

    public static Message makeCreateDeckMessage(String receiver, String deckName) {
        Message message = new Message(receiver);
        message.otherFields = new OtherFields();
        message.otherFields.setDeckName(deckName);
        message.messageType = MessageType.CREATE_DECK;
        return message;
    }

    public static Message makeRemoveDeckMessage(String receiver, String deckName) {
        Message message = new Message(receiver);
        message.otherFields = new OtherFields();
        message.otherFields.setDeckName(deckName);
        message.messageType = MessageType.REMOVE_DECK;
        return message;
    }

    public static Message makeSelectDeckMessage(String receiver, String deckName) {
        Message message = new Message(receiver);
        message.otherFields = new OtherFields();
        message.otherFields.setDeckName(deckName);
        message.messageType = MessageType.SELECT_DECK;
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

    public static Message makeRemoveCardFromDeckMessage(String receiver, String deckName, String cardId) {
        Message message = new Message(receiver);
        message.otherFields = new OtherFields();
        message.otherFields.setDeckName(deckName);
        message.otherFields.setMyCardId(cardId);
        message.messageType = MessageType.REMOVE_FROM_DECK;
        return message;
    }

    public static Message makeBuyCardMessage(String receiver, String cardName) {
        Message message = new Message(receiver);
        message.otherFields = new OtherFields();
        message.otherFields.setCardName(cardName);
        message.messageType = MessageType.BUY_CARD;
        return message;
    }

    public static Message makeSellCardMessage(String receiver, String cardId) {
        Message message = new Message(receiver);
        message.otherFields = new OtherFields();
        message.otherFields.setMyCardId(cardId);
        message.messageType = MessageType.SELL_CARD;
        return message;
    }

    public static Message makeEndTurnMessage(String receiver) {
        Message message = new Message(receiver);
        message.messageType = MessageType.END_TURN;
        return message;
    }

    public static Message makeMoveTroopMessage(String receiver, String cardId, Position position) {
        Message message = new Message(receiver);
        message.otherFields = new OtherFields();
        message.otherFields.setMyCardId(cardId);
        message.otherFields.setPosition(position);
        message.messageType = MessageType.MOVE_TROOP;
        return message;
    }

    public static Message makeInsertMessage(String receiver, String cardId, Position position) {
        Message message = new Message(receiver);
        message.otherFields = new OtherFields();
        message.otherFields.setMyCardId(cardId);
        message.otherFields.setPosition(position);
        message.messageType = MessageType.INSERT;
        return message;
    }

    public static Message makeAttackMessage(String receiver, String myCardId, String opponentCardId) {
        Message message = new Message(receiver);
        message.otherFields = new OtherFields();
        message.otherFields.setMyCardId(myCardId);
        message.otherFields.setOpponentCardId(opponentCardId);
        message.messageType = MessageType.ATTACK;
        return message;
    }

    public static Message makeComboAttackMessage(String receiver, String opponentCardId, String[] myCardIds) {
        Message message = new Message(receiver);
        message.otherFields = new OtherFields();
        message.otherFields.setOpponentCardId(opponentCardId);
        message.otherFields.setMyCardIds(myCardIds);
        message.messageType = MessageType.COMBO;
        return message;
    }

    public static Message makeForceFinishGameMessage(String receiver) {
        Message message = new Message(receiver);
        message.messageType = MessageType.FORCE_FINISH;
        return message;
    }

    public static Message makeUseSpecialPowerMessage(String receiver, String cardId, Position position) {
        Message message = new Message(receiver);
        message.otherFields = new OtherFields();
        message.otherFields.setMyCardId(cardId);
        message.otherFields.setPosition(position);
        message.messageType = MessageType.USE_SPECIAL_POWER;
        return message;
    }

    public static Message makeMultiPlayerGameReQuestMessage(String receiver, GameType gameType, int numberOfFlags, String opponentUsername) {
        Message message = new Message(receiver);
        message.newGameFields = new NewGameFields();
        message.newGameFields.setOpponentUsername(opponentUsername);
        message.newGameFields.setNumberOfFlags(numberOfFlags);
        message.newGameFields.setGameType(gameType);
        message.messageType = MessageType.MULTIPLAYER_GAME_REQUEST;
        return message;
    }

    public static Message makeCancelRequestMessage(String receiver) {
        Message message = new Message(receiver);
        message.messageType = MessageType.CANCEL_REQUEST;
        return message;
    }

    public static Message makeAcceptRequestMessage(String receiver, String opponentUsername) {
        Message message = new Message(receiver);
        message.messageType = MessageType.ACCEPT_REQUEST;
        message.newGameFields = new NewGameFields();
        message.newGameFields.setOpponentUsername(opponentUsername);
        return message;
    }

    public static Message makeDeclineRequestMessage(String receiver, String opponentUsername) {
        Message message = new Message(receiver);
        message.messageType = MessageType.DECLINE_REQUEST;
        message.newGameFields = new NewGameFields();
        message.newGameFields.setOpponentUsername(opponentUsername);
        return message;
    }

    public static Message makeNewCustomGameMessage(String receiver, GameType gameType, int numberOfFlags, String customDeckName) {
        Message message = new Message(receiver);
        message.newGameFields = new NewGameFields();
        message.newGameFields.setCustomDeckName(customDeckName);
        message.newGameFields.setNumberOfFlags(numberOfFlags);
        message.newGameFields.setGameType(gameType);
        message.messageType = MessageType.NEW_DECK_GAME;
        return message;
    }

    public static Message makeNewStoryGameMessage(String receiver, int stage) {
        Message message = new Message(receiver);
        message.newGameFields = new NewGameFields();
        message.newGameFields.setStage(stage);
        message.messageType = MessageType.NEW_STORY_GAME;
        return message;
    }

    public static Message makeSelectUserMessage(String receiver, String opponentUserName) {
        Message message = new Message(receiver);
        message.newGameFields = new NewGameFields();
        message.newGameFields.setOpponentUsername(opponentUserName);
        message.messageType = MessageType.SELECT_USER;
        return message;
    }

    public static Message makeCustomCardMessage(String receiver, Card customCard) {
        Message message = new Message(receiver);
        message.card = customCard;
        message.messageType = MessageType.ADD_CARD;
        return message;
    }

    public static Message makeChatMessage(String receiver, String messageSender, String messageReceiver, String textMessage) {
        Message message = new Message(receiver);
        message.chatMessage = new ChatMessage(messageSender, messageReceiver, textMessage);
        message.messageType = MessageType.CHAT;
        return message;
    }

    public static Message makeSudoMessage(String receiver, String sudoCommand) {
        Message message = new Message(receiver);
        message.otherFields = new OtherFields();
        message.otherFields.setSudoCommand(sudoCommand);
        message.messageType = MessageType.SUDO;
        return message;
    }

    public static Message makeChangeCardNumberMessage(String receiver, Card card, int newValue) {
        Message message = new Message(receiver);
        message.changeCardNumber = new ChangeCardNumber(card.getName(), newValue);
        message.messageType = MessageType.CHANGE_CARD_NUMBER;
        return message;
    }

    public static Message makeChangeAccountTypeMessage(String receiver, String username, AccountType newValue) {
        Message message = new Message(receiver);
        message.changeAccountType = new ChangeAccountType(username, newValue);
        message.messageType = MessageType.CHANGE_ACCOUNT_TYPE;
        return message;
    }

    public static Message makeValidateCustomCardMessage(String receiver, String customCardName) {
        Message message = new Message(receiver);
        message.cardName = customCardName;
        message.messageType = MessageType.ACCEPT_CARD;
        return message;
    }

    public static Message makeInValidateCustomCardMessage(String receiver, String customCardName) {
        Message message = new Message(receiver);
        message.cardName = customCardName;
        message.messageType = MessageType.REJECT_CARD;
        return message;
    }

    public static Message makeRequestOnlineGameShowMessage(String receiver, OnlineGame onlineGame) {
        Message message = new Message(receiver);
        message.onlineGame = onlineGame;
        message.messageType = MessageType.ONLINE_GAME_SHOW_REQUEST;
        return message;
    }

    public static Message makeStopShowGameMessage(String receiver, OnlineGame onlineGame) {
        Message message = new Message(receiver);
        message.onlineGame = onlineGame;
        message.messageType = MessageType.STOP_SHOW_GAME;
        return message;
    }

    public static Message makeSetNewNextCardMessage(String receiver){
        Message message = new Message(receiver);
        message.messageType = MessageType.SET_NEW_NEXT_CARD;
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

    public GameCopyMessage getGameCopyMessage() {
        return gameCopyMessage;
    }

    public CardsCopyMessage getCardsCopyMessage() {
        return cardsCopyMessage;
    }

    public AccountCopyMessage getAccountCopyMessage() {
        return accountCopyMessage;
    }

    public LeaderBoardCopyMessage getLeaderBoardCopyMessage() {
        return leaderBoardCopyMessage;
    }

    public StoriesCopyMessage getStoriesCopyMessage() {
        return storiesCopyMessage;
    }

    public CardPositionMessage getCardPositionMessage() {
        return cardPositionMessage;
    }

    public TroopUpdateMessage getTroopUpdateMessage() {
        return troopUpdateMessage;
    }

    public GameUpdateMessage getGameUpdateMessage() {
        return gameUpdateMessage;
    }

    public ExceptionMessage getExceptionMessage() {
        return exceptionMessage;
    }

    public GameAnimations getGameAnimations() {
        return gameAnimations;
    }

    public OpponentInfoMessage getOpponentInfoMessage() {
        return opponentInfoMessage;
    }

    public GameFinishMessage getGameFinishMessage() {
        return gameFinishMessage;
    }

    public Card getCard() {
        return card;
    }

    public ChatMessage getChatMessage() {
        return chatMessage;
    }

    public NewGameFields getNewGameFields() {
        return newGameFields;
    }

    public ChangeCardNumber getChangeCardNumber() {
        return changeCardNumber;
    }

    public String getCardName() {
        return cardName;
    }

    public OnlineGame[] getOnlineGames() {
        return onlineGames;
    }

    public CompressedCard getCompressedCard() {
        return compressedCard;
    }
}
