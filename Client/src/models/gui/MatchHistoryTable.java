package models.gui;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import server.dataCenter.models.account.MatchHistory;

import java.util.List;

import static models.gui.UIConstants.SCALE;

class MatchHistoryTable extends ScrollPane {
    private static final Insets PADDING = new Insets(UIConstants.DEFAULT_SPACING * 2);
    private static final double SEPARATOR_OPACITY = 0.3;
    private static final double WIDTH = 1020 * SCALE;
    private static final double HEIGHT = 700 * SCALE;
    private static final double BACKGROUND_CORNER = 30 * SCALE;
    private static final String ID = "background_transparent";
    private static final Background BACKGROUND = new Background(
            new BackgroundFill(
                    Color.rgb(57, 63, 74, 0.5),
                    new CornerRadii(BACKGROUND_CORNER), Insets.EMPTY
            )
    );

    MatchHistoryTable(List<MatchHistory> matchHistories) {
        setMinWidth(WIDTH * 1.03);
        setId(ID);
        GridPane labelsGrid = new GridPane();
        labelsGrid.setMinSize(WIDTH, HEIGHT);
        labelsGrid.setPadding(PADDING);
        labelsGrid.setBackground(BACKGROUND);
        labelsGrid.setAlignment(Pos.CENTER);
        labelsGrid.setHgap(UIConstants.DEFAULT_SPACING);
        labelsGrid.setVgap(UIConstants.DEFAULT_SPACING);

        for (int i = 0; i < matchHistories.size(); i++) {
            MatchHistory history = matchHistories.get(i);
            Label opponentLabel = new DefaultLabel(history.getOppName(), UIConstants.DEFAULT_FONT, Color.WHITE);
            opponentLabel.setPadding(new Insets(UIConstants.DEFAULT_SPACING));

            Label dateLabel = new DefaultLabel(history.getDate(), UIConstants.DEFAULT_FONT, Color.WHITE);
            dateLabel.setPadding(new Insets(UIConstants.DEFAULT_SPACING));

            Label stateLabel = new DefaultLabel(history.amIWinner() ? "Win" : "Lose", UIConstants.DEFAULT_FONT, Color.WHITE);
            stateLabel.setPadding(new Insets(UIConstants.DEFAULT_SPACING));

            Button showButton = new Button("SHOW GAME");
            showButton.setPadding(new Insets(UIConstants.DEFAULT_SPACING));
            showButton.setFont(UIConstants.DEFAULT_FONT);
            showButton.setTextFill(Color.BLACK);

            labelsGrid.addRow(2 * i,
                    opponentLabel, new DefaultSeparator(Orientation.VERTICAL, SEPARATOR_OPACITY),
                    dateLabel, new DefaultSeparator(Orientation.VERTICAL, SEPARATOR_OPACITY),
                    stateLabel, new DefaultSeparator(Orientation.VERTICAL, SEPARATOR_OPACITY),
                    showButton
            );
            if (i < matchHistories.size() - 1) {
                labelsGrid.add(
                        new DefaultSeparator(Orientation.HORIZONTAL, SEPARATOR_OPACITY),
                        0, 2 * i + 1, 7, 1
                );
            }
        }
        setContent(labelsGrid);
    }
}
