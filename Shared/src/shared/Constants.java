package shared;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Constants {
    // Board
    public static final int NUMBER_OF_ROWS = 5;
    public static final int NUMBER_OF_COLUMNS = 9;

    // Hand
    public static final int MAXIMUM_CARD_HAND_SIZE = 6;

    // Turn
    public static final long TURN_TIME_LIMIT = 120; // secs
    //public static final int MAXIMUM_CARD_HAND_SIZE = 6;
    public static final int END_OF_TURN_CARD_DRAW = 2;

    // Logging
    public static final String LOG_NAME = "Log.txt";
    public static final Path LOG_FILE_PATH = Paths.get("./logs/" + LOG_NAME).toAbsolutePath();
}
