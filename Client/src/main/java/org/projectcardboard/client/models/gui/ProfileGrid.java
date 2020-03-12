package org.projectcardboard.client.models.gui;

import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import org.projectcardboard.client.models.account.Account;
import org.projectcardboard.client.models.localisation.LanguageData;
import org.projectcardboard.client.models.localisation.LanguageKeys;

import java.io.FileNotFoundException;
import java.io.InputStream;

import static org.projectcardboard.client.models.gui.UIConstants.SCALE;

public class ProfileGrid extends GridPane {
    private static final double ICON_SIZE = 70 * SCALE;
    private static final String DEFAULT_PROFILE_PIC_URL = "/ui/default_profile.jpg";
    private static final String GENERAL_ICON_URL = "/ui/icon_general.png";
    private static final String GOLD_ICON_URL = "/ui/icon_gold.png";
    private static final String HISTORY_ICON_URL = "/ui/icon_history.png";


    private static final String USERNAME_TEXT = LanguageData.getInstance().getValue(new String[]{LanguageKeys.LOGIN_MENU, LanguageKeys.USERNAME});
    private static final String MATCH_HISTORY_TEXT = LanguageData.getInstance().getValue(new String[]{LanguageKeys.PROFILE_MENU, LanguageKeys.MATCH_HISTORY});
    public static Image goldIcon;
    public static Image generalIcon;
    private static Image defaultProfilePic;
    private static Image historyIcon;

    static {
        try {
            InputStream profilePicR = ProfileGrid.class.getResourceAsStream(DEFAULT_PROFILE_PIC_URL);
            InputStream generalIconR = ProfileGrid.class.getResourceAsStream(GENERAL_ICON_URL);
            InputStream goldIconR = ProfileGrid.class.getResourceAsStream(GOLD_ICON_URL);
            InputStream historyIconR = ProfileGrid.class.getResourceAsStream(HISTORY_ICON_URL);
            if (
                profilePicR == null ||
                generalIconR == null ||
                goldIconR == null ||
                historyIconR == null
            ) {
                throw new FileNotFoundException();
            }
            defaultProfilePic = new Image(profilePicR);
            generalIcon = new Image(generalIconR);
            goldIcon = new Image(goldIconR);
            historyIcon = new Image(historyIconR);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ProfileGrid(Account account) {
        setHgap(UIConstants.DEFAULT_SPACING * 4);
        setVgap(UIConstants.DEFAULT_SPACING * 4);

        add(new ProfilePicture(defaultProfilePic), 0, 0, 3, 3);

        add(ImageLoader.makeImageView(generalIcon, ICON_SIZE, ICON_SIZE), 0, 3);
        add(new DialogText(USERNAME_TEXT), 1, 3);
        add(new DialogText(account.getUsername()), 2, 3);

        add(ImageLoader.makeImageView(historyIcon, ICON_SIZE, ICON_SIZE), 3, 0);
        add(new DialogText(MATCH_HISTORY_TEXT), 4, 0);

        add(new MatchHistoryTable(account.getMatchHistories()), 3, 1, 3, 4);
    }
}
