package org.projectcardboard.client.models.localisation;

import org.junit.Assert;
import org.junit.Test;

public class LanguageDataTest {

  /**
   * Until we make our language loading a bit more static, any new fields should be added to this.
   * It's the closest we can get right now to making sure at least the default will work around
   * compile time.
   */
  @Test
  public void loadDataDefaultMustWork() {
    LanguageData recieved = LanguageData.getInstance();

    assertExisted(
        recieved.getValue(new String[] {LanguageKeys.LOGIN_MENU, LanguageKeys.WELCOME_MESSAGE}));
    assertExisted(recieved.getValue(new String[] {LanguageKeys.LOGIN_MENU, LanguageKeys.VERSION}));
    assertExisted(recieved.getValue(new String[] {LanguageKeys.LOGIN_MENU, LanguageKeys.LOGIN}));
    assertExisted(recieved.getValue(new String[] {LanguageKeys.LOGIN_MENU, LanguageKeys.REGISTER}));
    assertExisted(recieved.getValue(new String[] {LanguageKeys.LOGIN_MENU, LanguageKeys.USERNAME}));
    assertExisted(recieved.getValue(new String[] {LanguageKeys.LOGIN_MENU, LanguageKeys.PASSWORD}));
    assertExisted(recieved
        .getValue(new String[] {LanguageKeys.LOGIN_MENU, LanguageKeys.ERROR_SHORT_USERNAME}));
    assertExisted(recieved
        .getValue(new String[] {LanguageKeys.LOGIN_MENU, LanguageKeys.ERROR_SHORT_PASSWORD}));

    assertExisted(recieved.getValue(new String[] {LanguageKeys.MAIN_MENU, LanguageKeys.PLAY}));
    assertExisted(
        recieved.getValue(new String[] {LanguageKeys.MAIN_MENU, LanguageKeys.COLLECTION}));
    assertExisted(recieved.getValue(new String[] {LanguageKeys.MAIN_MENU, LanguageKeys.CHAT}));
    assertExisted(recieved.getValue(new String[] {LanguageKeys.MAIN_MENU, LanguageKeys.SPECTATE}));
    assertExisted(recieved.getValue(new String[] {LanguageKeys.MAIN_MENU, LanguageKeys.PROFILE}));
    assertExisted(recieved.getValue(new String[] {LanguageKeys.MAIN_MENU, LanguageKeys.QUIT}));

    assertExisted(
        recieved.getValue(new String[] {LanguageKeys.SPECTATE_MENU, LanguageKeys.ONLINE_GAMES}));

    assertExisted(
        recieved.getValue(new String[] {LanguageKeys.PROFILE_MENU, LanguageKeys.MATCH_HISTORY}));
    assertExisted(recieved.getValue(new String[] {LanguageKeys.PROFILE_MENU, LanguageKeys.LOGOUT}));

    assertExisted(recieved.getValue(new String[] {LanguageKeys.BUTTON_TEXT, LanguageKeys.ERROR}));
    assertExisted(recieved.getValue(new String[] {LanguageKeys.BUTTON_TEXT, LanguageKeys.OK}));
    assertExisted(recieved.getValue(new String[] {LanguageKeys.BUTTON_TEXT, LanguageKeys.CANCEL}));
  }

  private void assertExisted(String s) {
    Assert.assertNotEquals("???", s);
  }
}
