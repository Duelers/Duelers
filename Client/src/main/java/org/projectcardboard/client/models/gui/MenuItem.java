package org.projectcardboard.client.models.gui;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class MenuItem {
  final int index;
  final String title;
  final String hint;
  final EventHandler<? super MouseEvent> event;

  public MenuItem(int index, String title, String hint, EventHandler<? super MouseEvent> event) {
    this.index = index;
    this.title = title;
    this.hint = hint;
    this.event = event;
  }
}
