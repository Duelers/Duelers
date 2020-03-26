package org.projectcardboard.client.models.gui;

public class NumberField extends NormalField {
  public NumberField(String text) {
    super(text);
    textProperty().addListener(((observable, oldValue, newValue) -> {
      if (newValue.isEmpty() || newValue.equals("-"))
        return;
      if (!newValue.matches("-?\\d*") || Integer.parseInt(newValue) > 10000
          || Integer.parseInt(newValue) < -10000) {
        setText(oldValue);
      }
    }));
  }

  public int getValue() {
    return (getText().length() == 0 || getText().equals("-")) ? 0 : Integer.parseInt(getText());
  }
}
