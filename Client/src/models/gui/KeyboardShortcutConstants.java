package models.gui;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

public class KeyboardShortcutConstants {
    public static final KeyCombination EXIT_FULLSCREEN = new KeyCodeCombination(KeyCode.ENTER, KeyCombination.ALT_DOWN);
    public static final String EXIT_FULLSCREEN_HELP_MSG = "Exit fullscreen: Alt+Enter";

    public static final KeyCode KEY_FOR_CHAT = KeyCode.T;
}
