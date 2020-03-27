package org.projectcardboard.client.models.gui;

import org.junit.Test;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

public class BackButtonTest extends ApplicationTest {

    private Stage stage;
    private BackButton backButton;

    @Override
    public void start(Stage stage) {
        this.stage = stage;
    }

    @BeforeClass
    public static void setupSpec() throws Exception {
        System.setProperty("testfx.robot", "glass");
        System.setProperty("testfx.headless", "true");
        System.setProperty("prism.order", "sw");
        System.setProperty("prism.text", "t2k");
        System.setProperty("java.awt.headless", "true");
    }

    @Before
    public void setUp() throws Exception {
        Platform.runLater(() -> {
            backButton = new BackButton(null);
            stage.setScene(new Scene(new StackPane(backButton), 100, 100));
            stage.show();
        });
        WaitForAsyncUtils.waitForFxEvents();
    }

    @After
    public void tearDown() throws Exception {
        FxToolkit.hideStage();
    }

    @Test
    public void imageLoaded() {
        Image image = backButton.getImage();
        Assert.assertFalse(image == null || image.isError());
    }
}