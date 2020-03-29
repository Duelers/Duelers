package org.projectcardboard.client.models.gui;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.projectcardboard.client.models.account.Collection;
import org.projectcardboard.client.models.card.Deck;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import static org.junit.Assert.*;

import java.util.ArrayList;

import static org.mockito.Mockito.when;


public class DeckBox_Test extends ApplicationTest {

  private DeckBox testDeckBox;
  private Stage stage;

  @Override
  public void start(Stage stage) {
    this.stage = stage;
  }

  @Mock
  Deck mockDeck;

  @BeforeClass
  public static void setupSpec() throws Exception {
    System.setProperty("testfx.robot", "glass");
    System.setProperty("testfx.headless", "true");
    System.setProperty("prism.order", "sw");
    System.setProperty("prism.text", "t2k");
    System.setProperty("java.awt.headless", "true");
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    when(mockDeck.getName()).thenReturn("mockDeckName");

    Platform.runLater(() -> {
      testDeckBox = new DeckBox(mockDeck);
      stage.setScene(new Scene(new StackPane(testDeckBox), 100, 100));
      stage.show();
    });
    WaitForAsyncUtils.waitForFxEvents();
  }

  @After
  public void tearDown() throws Exception {
    FxToolkit.hideStage();
  }

  @Test
  public void whenDeckIsEmpty_FunctionCountReturnsEmptyString() {
    when(mockDeck.getCards()).thenReturn(new ArrayList<>());

    String expected = "";
    String actual = testDeckBox.getFactionCardCount(mockDeck);

    assertEquals(expected, actual);
  }
}
