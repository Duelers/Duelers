package server.gameCenter.models.game.availableActions;


import server.dataCenter.models.card.ServerCard;

public class Insert {
    private ServerCard card;

    Insert(ServerCard card) {
        this.card = card;
    }

    public ServerCard getCard() {
        return card;
    }
}
