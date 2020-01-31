package models.gui;

import models.card.CardType;
import models.card.EditableCard;

import java.beans.PropertyChangeEvent;
import java.io.FileNotFoundException;

public class EditableCardPane extends CardPane {
    public EditableCardPane(EditableCard card) throws FileNotFoundException {
        super(card, true, false, null);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "name":
                setName((String) evt.getNewValue());
                break;
            case "description":
                setDescription((String) evt.getNewValue());
                break;
            case "spriteName":
                updateSprite();
                break;
            case "type":
                setType((CardType) evt.getNewValue());
                break;
            case "defaultAp":
                setDefaultAp((Integer) evt.getNewValue());
                break;
            case "defaultHp":
                setDefaultHp((Integer) evt.getNewValue());
                break;
            case "mannaPoint":
                setMannaPoint((Integer) evt.getNewValue());
                break;
            case "price":
                setPrice((Integer) evt.getNewValue());
                break;
        }
    }
}
