package org.projectcardboard.client.models.gui;

import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import org.projectcardboard.client.models.account.Account;
import org.projectcardboard.client.models.localisation.LanguageData;
import org.projectcardboard.client.models.localisation.LanguageKeys;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static org.projectcardboard.client.models.gui.UIConstants.SCALE;

public class ProfileGrid extends GridPane {
    private static final double ICON_SIZE = 70 * SCALE;
    private static final String DEFAULT_PROFILE_PIC_URL = "Client/src/main/resources/ui/default_profile.jpg";
    private static final String GENERAL_ICON_URL = "Client/src/main/resources/ui/icon_general.png";
    private static final String GOLD_ICON_URL = "Client/src/main/resources/ui/icon_gold.png";
    private static final String HISTORY_ICON_URL = "Client/src/main/resources/ui/icon_history.png";


    private static final String USERNAME_TEXT = LanguageData.getInstance().getValue(new String[]{LanguageKeys.LOGIN_MENU, LanguageKeys.USERNAME});
    private static final String MATCH_HISTORY_TEXT = LanguageData.getInstance().getValue(new String[]{LanguageKeys.PROFILE_MENU, LanguageKeys.MATCH_HISTORY});
    public static Image goldIcon;
    public static Image generalIcon;
    private static Image defaultProfilePic;
    private static Image historyIcon;

    static {
        try {
            defaultProfilePic = new Image(new FileInputStream(DEFAULT_PROFILE_PIC_URL));
            generalIcon = new Image(new FileInputStream(GENERAL_ICON_URL));
            goldIcon = new Image(new FileInputStream(GOLD_ICON_URL));
            historyIcon = new Image(new FileInputStream(HISTORY_ICON_URL));
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
