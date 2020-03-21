package shared.models.card;

public interface ICard {
    CardType getType();

    String getName();

    String getSpriteName();

    String getFxName();

    int getDefaultAp();

    int getDefaultHp();

    int getPrice();

    String getDescription();

    int getManaCost();
}
