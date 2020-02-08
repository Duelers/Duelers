package server.clientPortal.models.comperessedData;

import server.dataCenter.models.card.Card;
import server.dataCenter.models.card.CardType;

import java.util.List;

public class CompressedCell {
    private int row;
    private int column;
    private CompressedCard item;//non flag item

    public CompressedCell(int row, int column, List<Card> items) {
        this.row = row;
        this.column = column;
        for (Card item : items) {
            if (item.getType() == CardType.COLLECTIBLE_ITEM) {
                this.item = item.toCompressedCard();
            }
        }
    }


}
