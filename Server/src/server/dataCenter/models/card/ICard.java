package server.dataCenter.models.card;

import server.dataCenter.models.card.CardType;

public interface ICard {
    CardType getType();

    String getName();

    String getSpriteName();

    int getDefaultAp();

    int getDefaultHp();

    int getPrice();

    String getDescription();

    int getMannaPoint();
}
