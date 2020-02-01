package view;

import controller.CustomCardController;
import controller.GraphicalUserInterface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.paint.Color;
import models.card.AttackType;
import models.card.CardType;
import models.card.EditableCard;
import models.card.spell.*;
import models.game.map.Position;
import models.gui.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static controller.SoundEffectPlayer.SoundName.click;
import static models.card.CardType.HERO;
import static models.gui.UIConstants.*;

public class CustomCardMenu extends Show implements PropertyChangeListener {
    private static final EventHandler<? super MouseEvent> BACK_EVENT = event -> new MainMenu().show();
    private static final List<CardType> cardTypes = Arrays.asList(HERO, CardType.MINION, CardType.SPELL, CardType.USABLE_ITEM);
    private static final String ICON_PATH = "Client/resources/icons";
    private static final String TROOPS_PATH = "Client/resources/troopAnimations";
    private static final Map<CardType, ObservableList<String>> sprites = new HashMap<>();
    private static Media backgroundMusic = new Media(
            new File("Client/resources/music/shop_menu.m4a").toURI().toString()
    );

    static {
        ObservableList<String> iconSprites = FXCollections.observableArrayList();
        ObservableList<String> troopSprites = FXCollections.observableArrayList();
        loadFiles(ICON_PATH, iconSprites);
        loadFiles(TROOPS_PATH, troopSprites);
        sprites.put(HERO, troopSprites);
        sprites.put(CardType.MINION, troopSprites);
        sprites.put(CardType.USABLE_ITEM, iconSprites);
        sprites.put(CardType.SPELL, iconSprites);
    }

    private final EditableCard card = new EditableCard();
    private final DefaultLabel typeLabel = new DefaultLabel("TYPE", DEFAULT_FONT, Color.WHITE);
    private final DefaultLabel defaultApLabel = new DefaultLabel("AP", DEFAULT_FONT, Color.WHITE);
    private final DefaultLabel defaultHpLabel = new DefaultLabel("HP", DEFAULT_FONT, Color.WHITE);
    private final DefaultLabel attackTypeLabel = new DefaultLabel("ATTACK", DEFAULT_FONT, Color.WHITE);
    private final DefaultLabel mannaPointLabel = new DefaultLabel("MANNA", DEFAULT_FONT, Color.WHITE);
    private final DefaultLabel rangeLabel = new DefaultLabel("RANGE", DEFAULT_FONT, Color.WHITE);
    private final DefaultLabel hasComboLabel = new DefaultLabel("COMBO", DEFAULT_FONT, Color.WHITE);
    private final NormalField name = new NormalField("name");
    private final NormalField description = new NormalField("description");
    private final DefaultSpinner<CardType> cardTypeSpinner = new DefaultSpinner<>(FXCollections.observableArrayList(cardTypes));
    private final DefaultSpinner<AttackType> attackTypeSpinner = new DefaultSpinner<>(FXCollections.observableArrayList(AttackType.values()));
    private final DefaultSpinner<ComboState> hasComboSpinner = new DefaultSpinner<>(FXCollections.observableArrayList(ComboState.values()));
    private final DefaultSpinner<Integer> defaultHpSpinner = new DefaultSpinner<>(1, 50, 1);
    private final DefaultSpinner<Integer> defaultApSpinner = new DefaultSpinner<>(0, 15, 0);
    private final DefaultSpinner<Integer> rangeSpinner = new DefaultSpinner<>(2, 4, 2);
    private final DefaultSpinner<Integer> mannaPointSpinner = new DefaultSpinner<>(0, 9, 0);
    private final DefaultSpinner<String> spriteSpinner = new DefaultSpinner<>(FXCollections.observableArrayList(sprites.get(cardTypeSpinner.getValue())));
    private final NumberField priceField = new NumberField("price");
    private final OrangeButton addSpellButton = new OrangeButton("ADD NEW SPELL", event -> addSpell(), click);
    private final OrangeButton makeCardButton = new OrangeButton("MAKE CARD", event -> {
        CustomCardController.getInstance().createCard(card);
        new MainMenu().show();
    }, click);
    private VBox spellsBox;
    private GridPane cardMakerGrid;
    private EditableCardPane cardPane;

    {
        card.setSpriteName(spriteSpinner.getValue());
        card.setType(cardTypeSpinner.getValue());
        card.setAttackType(attackTypeSpinner.getValue());
        card.setDefaultAp(defaultApSpinner.getValue());
        card.setDefaultHp(defaultHpSpinner.getValue());
        card.setHasCombo(hasComboSpinner.getValue().hasCombo);
        card.setMannaPoint(mannaPointSpinner.getValue());
        card.setPrice(String.valueOf(priceField.getValue()));
        card.setRange(rangeSpinner.getValue());
        try {
            cardPane = new EditableCardPane(card);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public CustomCardMenu() {
        preProcess();
        try {
            root.setBackground(UIConstants.DEFAULT_ROOT_BACKGROUND);
            BorderPane background = BackgroundMaker.getMenuBackground();
            BackButton backButton = new BackButton(BACK_EVENT);

            cardMakerGrid = new GridPane();
            cardMakerGrid.setHgap(DEFAULT_SPACING * 2);
            cardMakerGrid.setVgap(DEFAULT_SPACING * 2);
            cardMakerGrid.setMaxSize(1500 * SCALE, 1000 * SCALE);
            GridPane.setHalignment(addSpellButton, HPos.CENTER);
            GridPane.setHalignment(makeCardButton, HPos.CENTER);
            GridPane.setHalignment(spriteSpinner, HPos.CENTER);

            cardMakerGrid.add(name, 0, 0, 2, 1);
            cardMakerGrid.add(description, 0, 1, 2, 1);

            cardMakerGrid.add(typeLabel, 0, 2);
            cardMakerGrid.add(cardTypeSpinner, 1, 2);

            cardMakerGrid.add(priceField, 0, 3, 2, 1);
            cardMakerGrid.add(new DefaultSeparator(Orientation.HORIZONTAL), 0, 4, 2, 1);

            setType(cardTypeSpinner.getValue(), cardTypeSpinner.getValue());

            cardMakerGrid.add(new DefaultSeparator(Orientation.VERTICAL), 2, 0, 1, 11);

            cardMakerGrid.add(cardPane, 3, 0, 2, 8);
            cardMakerGrid.add(new DefaultLabel("SPRITE", DEFAULT_FONT, Color.WHITE), 3, 8);
            cardMakerGrid.add(spriteSpinner, 4, 8);

            cardMakerGrid.add(addSpellButton, 3, 9, 2, 1);
            cardMakerGrid.add(makeCardButton, 3, 10, 2, 1);

            cardMakerGrid.add(new DefaultSeparator(Orientation.VERTICAL), 5, 0, 1, 11);

            spellsBox = new VBox(DEFAULT_SPACING * 2);
            spellsBox.setAlignment(Pos.CENTER);

            cardMakerGrid.add(spellsBox, 6, 0, 1, 11);

            DefaultContainer container = new DefaultContainer(cardMakerGrid);

            AnchorPane sceneContents = new AnchorPane(background, container, backButton);

            showGlobalChatDialog(sceneContents);

            root.getChildren().addAll(sceneContents);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void loadFiles(String path, ObservableList<String> target) {
        File directory = new File(path);
        File[] files = directory.listFiles();
        if (files != null) {
            Arrays.stream(files)
                    .filter(file -> file.getName().contains(".png"))
                    .map(file -> file.getName().replace(".png", ""))
                    .forEach(target::add);
        }
    }

    private void showGlobalChatDialog(AnchorPane sceneContents) {
        sceneContents.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.T)) {
                GlobalChatDialog.getInstance().show();
            }
        });
    }

    private void preProcess() {
        card.addListener(this);
        card.addListener(cardPane);
        name.textProperty().addListener((observable, oldValue, newValue) -> card.setName(newValue));
        description.textProperty().addListener((observable, oldValue, newValue) -> card.setDescription(newValue));
        cardTypeSpinner.valueProperty().addListener((observable, oldValue, newValue) -> card.setType(newValue));
        spriteSpinner.valueProperty().addListener(((observable, oldValue, newValue) -> card.setSpriteName(newValue)));
        attackTypeSpinner.valueProperty().addListener(((observable, oldValue, newValue) -> card.setAttackType(newValue)));
        defaultApSpinner.valueProperty().addListener(((observable, oldValue, newValue) -> card.setDefaultAp(newValue)));
        defaultHpSpinner.valueProperty().addListener(((observable, oldValue, newValue) -> card.setDefaultHp(newValue)));
        mannaPointSpinner.valueProperty().addListener(((observable, oldValue, newValue) -> card.setMannaPoint(newValue)));
        rangeSpinner.valueProperty().addListener(((observable, oldValue, newValue) -> card.setRange(newValue)));
        hasComboSpinner.valueProperty().addListener(((observable, oldValue, newValue) -> card.setHasCombo(newValue.hasCombo)));
        priceField.textProperty().addListener(((observable, oldValue, newValue) -> card.setPrice(newValue)));
    }

    @Override
    public void show() {
        super.show();
        BackgroundMaker.makeMenuBackgroundFrozen();
        GraphicalUserInterface.getInstance().setBackgroundMusic(backgroundMusic);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "type":
                setType((CardType) evt.getOldValue(), (CardType) evt.getNewValue());
                break;
            case "attackType":
                setAttackType((AttackType) evt.getNewValue());
                break;
            case "spells":
                showSpells((ArrayList<Spell>) evt.getNewValue());
        }
    }

    private void showSpells(ArrayList<Spell> spells) {
        spellsBox.getChildren().clear();
        for (Spell spell : spells) {
            spellsBox.getChildren().add(new SpellBox(card, spell));
        }
    }

    private void setType(CardType oldValue, CardType newValue) {
        if (!sprites.get(oldValue).equals(sprites.get(newValue))) {
            spriteSpinner.setValueFactory(new SpinnerValueFactory.ListSpinnerValueFactory<>(sprites.get(newValue)));
        }
        switch (newValue) {
            case HERO:
                cardMakerGrid.getChildren().removeAll(
                        defaultApLabel, defaultApSpinner,
                        defaultHpLabel, defaultHpSpinner,
                        attackTypeLabel, attackTypeSpinner,
                        mannaPointLabel, mannaPointSpinner,
                        hasComboLabel, hasComboSpinner
                );
                cardMakerGrid.add(defaultApLabel, 0, 5);
                cardMakerGrid.add(defaultApSpinner, 1, 5);

                cardMakerGrid.add(defaultHpLabel, 0, 6);
                cardMakerGrid.add(defaultHpSpinner, 1, 6);

                cardMakerGrid.add(attackTypeLabel, 0, 7);
                cardMakerGrid.add(attackTypeSpinner, 1, 7);

                cardMakerGrid.add(hasComboLabel, 0, 8);
                cardMakerGrid.add(hasComboSpinner, 1, 8);
                break;
            case MINION:
                cardMakerGrid.getChildren().removeAll(
                        defaultApLabel, defaultApSpinner,
                        defaultHpLabel, defaultHpSpinner,
                        attackTypeLabel, attackTypeSpinner,
                        mannaPointLabel, mannaPointSpinner,
                        hasComboLabel, hasComboSpinner
                );
                cardMakerGrid.add(defaultApLabel, 0, 5);
                cardMakerGrid.add(defaultApSpinner, 1, 5);

                cardMakerGrid.add(defaultHpLabel, 0, 6);
                cardMakerGrid.add(defaultHpSpinner, 1, 6);

                cardMakerGrid.add(attackTypeLabel, 0, 7);
                cardMakerGrid.add(attackTypeSpinner, 1, 7);

                cardMakerGrid.add(mannaPointLabel, 0, 8);
                cardMakerGrid.add(mannaPointSpinner, 1, 8);

                cardMakerGrid.add(hasComboLabel, 0, 9);
                cardMakerGrid.add(hasComboSpinner, 1, 9);
                break;
            case SPELL:
                cardMakerGrid.getChildren().removeAll(
                        defaultApLabel, defaultApSpinner,
                        defaultHpLabel, defaultHpSpinner,
                        attackTypeLabel, attackTypeSpinner,
                        mannaPointLabel, mannaPointSpinner,
                        hasComboLabel, hasComboSpinner
                );
                cardMakerGrid.add(mannaPointLabel, 0, 5);
                cardMakerGrid.add(mannaPointSpinner, 1, 5);
                break;
            case USABLE_ITEM:
                cardMakerGrid.getChildren().removeAll(
                        defaultApLabel, defaultApSpinner,
                        defaultHpLabel, defaultHpSpinner,
                        attackTypeLabel, attackTypeSpinner,
                        mannaPointLabel, mannaPointSpinner,
                        hasComboLabel, hasComboSpinner
                );
                break;
        }
    }

    private void setAttackType(AttackType newValue) {
        switch (newValue) {
            case MELEE:
                cardMakerGrid.getChildren().removeAll(rangeLabel, rangeSpinner);
                break;
            case RANGED:
            case HYBRID:
                cardMakerGrid.getChildren().removeAll(rangeLabel, rangeSpinner);
                cardMakerGrid.add(rangeLabel, 0, 10);
                cardMakerGrid.add(rangeSpinner, 1, 10);
                break;
        }
    }

    private void addSpell() {
        DialogText dialogText = new DialogText("spell properties : ");
        DialogBox dialogBox = new DialogBox();
        NormalField spellId = new NormalField("spell ID");

        DialogText spellActionLabel = new DialogText("spell action : ");
        NumberField enemyHitChange = new NumberField("enemyHitChange");
        NumberField apChange = new NumberField("ap Change");
        NumberField hpChange = new NumberField("hpChange");
        CheckBox isPoison = new CheckBox("poison");
        CheckBox makeStun = new CheckBox("make stun");
        CheckBox disarm = new CheckBox("disarm");
        CheckBox noDisarm = new CheckBox("noDisarm");
        CheckBox noPoison = new CheckBox("noPoison");
        CheckBox noStun = new CheckBox("noStun");
        CheckBox noBadEffect = new CheckBox("noBadEffect");
        CheckBox noAttackFromWeakerOnes = new CheckBox("noAttackFromWeakerOnes");
        CheckBox killsTarget = new CheckBox("killsTarget");
        CheckBox durable = new CheckBox("durable");
        NumberField duration = new NumberField("duration");
        NumberField delay = new NumberField("delay");

        DialogText spellTarget = new DialogText("target : ");
        CheckBox isRelatedToCardOwnerPosition = new CheckBox("is Related To Card Owner Position?");
        CheckBox isForAroundOwnHero = new CheckBox("is For Around Own Hero?");
        NumberField row = new NumberField("dimension(row)?");
        NumberField column = new NumberField("dimension(column)?");
        CheckBox isRandom = new CheckBox("isRandom?");
        CheckBox own = new CheckBox("is for own?");
        CheckBox enemy = new CheckBox("is for enemy?");
        CheckBox cell = new CheckBox("is for cell?");
        CheckBox hero = new CheckBox("is for hero?");
        CheckBox minion = new CheckBox("is for minion?");
        CheckBox melee = new CheckBox("is for melee?");
        CheckBox ranged = new CheckBox("is for ranged?");
        CheckBox hybrid = new CheckBox("is for hybrid?");
        CheckBox isForDeckCards = new CheckBox("is for isForDeckCards?");

        DialogText availabilityTypeLabel = new DialogText("availability Type : ");
        CheckBox onPut = new CheckBox("onPut");
        CheckBox onAttack = new CheckBox("onAttack");
        CheckBox onDeath = new CheckBox("onDeath");
        CheckBox continuous = new CheckBox("continuous");
        CheckBox specialPower = new CheckBox("specialPower");
        CheckBox onStart = new CheckBox("onStart");

        NumberField coolDown = new NumberField("coolDown");
        NumberField mannaPoint = new NumberField("manna point");
        dialogBox.getChildren().addAll(dialogText, spellId,
                spellActionLabel, new ScrollPane(new VBox(enemyHitChange, apChange, hpChange, isPoison, makeStun, disarm, noDisarm, noPoison, noStun, noBadEffect, noAttackFromWeakerOnes, killsTarget, durable, duration, delay)),
                spellTarget, new ScrollPane(new VBox(isRelatedToCardOwnerPosition, isForAroundOwnHero, row, column, isRandom, own, enemy, cell, hero, minion, melee, ranged, hybrid, isForDeckCards)),
                availabilityTypeLabel, new ScrollPane(new VBox(onPut, onAttack, onDeath, continuous, specialPower, onStart)),
                coolDown, mannaPoint
        );
        dialogBox.getChildren().stream().filter(node -> node instanceof ScrollPane).forEach(node -> ((ScrollPane) node).setMinHeight(300 * SCALE));

        dialogBox.makeButton("add", event -> {
            SpellAction spellAction = new SpellAction(
                    enemyHitChange.getValue(), apChange.getValue(), hpChange.getValue(),
                    0, isPoison.isSelected(), makeStun.isSelected(),
                    disarm.isSelected(), noDisarm.isSelected(), noPoison.isSelected(),
                    noStun.isSelected(), noBadEffect.isSelected(), noAttackFromWeakerOnes.isSelected(),
                    killsTarget.isSelected(), durable.isSelected(), duration.getValue(), delay.getValue()
            );

            Target target = new Target(
                    isRelatedToCardOwnerPosition.isSelected(), isForAroundOwnHero.isSelected(),
                    new Position(row.getValue(), column.getValue()), isRandom.isSelected(),
                    new Owner(own.isSelected(), enemy.isSelected()),
                    new TargetCardType(cell.isSelected(), hero.isSelected(), minion.isSelected(), false),
                    new CardAttackType(melee.isSelected(), ranged.isSelected(), hybrid.isSelected()),
                    isForDeckCards.isSelected()
            );

            AvailabilityType availabilityType = new AvailabilityType(
                    onPut.isSelected(), onAttack.isSelected(), onDeath.isSelected(),
                    continuous.isSelected(), specialPower.isSelected(), onStart.isSelected(),
                    false);

            card.addSpell(new Spell(
                    spellId.getText(), spellAction, target, availabilityType,
                    coolDown.getValue(), mannaPoint.getValue())
            );
        });
        DialogContainer dialogContainer = new DialogContainer(root, dialogBox);
        dialogContainer.show();
        dialogBox.makeClosable(dialogContainer);
    }


    enum ComboState {
        YES(true),
        NO(false);

        private final boolean hasCombo;

        ComboState(boolean hasCombo) {
            this.hasCombo = hasCombo;
        }
    }
}
