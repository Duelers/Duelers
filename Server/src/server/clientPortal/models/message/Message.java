package server.clientPortal.models.message;

import server.GameServer;
import server.clientPortal.models.JsonConverter;
import shared.models.card.Card;
import server.dataCenter.models.account.Account;
import server.dataCenter.models.account.Collection;
import server.dataCenter.models.card.ExportedDeck;
import shared.models.card.spell.AvailabilityType;
import server.gameCenter.models.game.*;
import shared.models.game.GameType;
import shared.models.game.map.Cell;
import shared.models.game.map.CellEffect;

import java.util.List;
import java.util.Set;

public class Message {
    private MessageType messageType;
    //serverName || clientName
    private final String sender;
    private final String receiver;

    //SENDER:SERVER
    private GameCopyMessage gameCopyMessage;
    private CardsCopyMessage cardsCopyMessage;
    private AccountCopyMessage accountCopyMessage;
    private CardPositionMessage cardPositionMessage;
    private TroopUpdateMessage troopUpdateMessage;
    private GameUpdateMessage gameUpdateMessage;
    private ClientIDMessage clientIDMessage;
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
    public String token;
    private ChatMessage chatMessage;
    private NewGameFields newGameFields;
    private ChangeCardNumber changeCardNumber;
    private ChangeAccountType changeAccountType;


    private Message(String receiver) {
        this.sender = GameServer.serverName;
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

    public static Message makeAttackMessage(String receiver, ServerTroop attacker, ServerTroop defender, boolean counterAttack) {
        Message message = new Message(receiver);
        message.gameAnimations = new GameAnimations();
        message.gameAnimations.addAttacks(attacker.getCard().getCardId(), defender.getCard().getCardId());
        if (counterAttack)
            message.gameAnimations.addCounterAttacks(defender.getCard().getCardId(), attacker.getCard().getCardId());
        message.messageType = MessageType.ANIMATION;
        return message;
    }

    public static Message makeNewNextCardSetMessage(String receiver, Card nextCard) {
        Message message = new Message(receiver);
        message.messageType = MessageType.SET_NEW_NEXT_CARD;
        message.card = nextCard; // Used to set message.compressedCard. This could potentially alter behaviour.
        return message;
    }

    public static Message makeSpellMessage(String receiver, Set<Cell> cells, AvailabilityType availabilityType) {
        Message message = new Message(receiver);
        message.gameAnimations = new GameAnimations();
        message.gameAnimations.addSpellAnimation(cells, availabilityType);
        message.messageType = MessageType.ANIMATION;
        return message;
    }

    public static Message makeTroopUpdateMessage(String receiver, ServerTroop troop) {
        Message message = new Message(receiver);
        message.troopUpdateMessage = new TroopUpdateMessage(troop);
        message.messageType = MessageType.TROOP_UPDATE;
        return message;
    }

    public static Message makeGameUpdateMessage(String receiver, int turnNumber, int player1CurrentMP, int player2CurrentMP,
                                                List<CellEffect> cellEffects) {
        Message message = new Message(receiver);
        message.gameUpdateMessage = new GameUpdateMessage(turnNumber, player1CurrentMP,
                player2CurrentMP, cellEffects);
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

    public static Message makeGameFinishMessage(String receiver, boolean youWon) {
        Message message = new Message(receiver);
        message.gameFinishMessage = new GameFinishMessage(youWon);
        message.messageType = MessageType.Game_FINISH;
        return message;
    }

    public static Message makeInvitationMessage(String receiver, String inviterUsername, GameType gameType) {
        Message message = new Message(receiver);
        message.messageType = MessageType.INVITATION;
        message.newGameFields = new NewGameFields(gameType, 0, null, inviterUsername);
        return message;
    }

    public static Message makeAcceptRequestMessage(String receiver) {
        Message message = new Message(receiver);
        message.messageType = MessageType.ACCEPT_REQUEST;
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

    public static Message makeOnlineGamesCopyMessage(String receiver, OnlineGame[] onlines) {
        Message message = new Message(receiver);
        message.onlineGames = onlines;
        message.messageType = MessageType.ONLINE_GAMES_COPY;
        return message;
    }

    public static Message makeClientIDMessage(String receiver, String clientID) {
        Message message = new Message(receiver);
        message.clientIDMessage = new ClientIDMessage(clientID);
        message.messageType = MessageType.CLIENT_ID;
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
}
