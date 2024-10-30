package gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.Die;
import models.RaffleCup;
import models.YatzyResultCalculator;

public class YatzyGui extends Application {
    private final RaffleCup raffleCup = new RaffleCup();
    private final Die[] dice = raffleCup.getDice();
    private final Text[] rollText = new Text[5];
    private final CheckBox[] checkBoxes = new CheckBox[5];
    private int remainingThrows = 0;
    private final Label remainingThrowsLabel = new Label();
    private boolean roundEnd = false;
    private boolean bonus = false;
    private final Label lblUpperSectionSum = new Label("Sum: ");
    private final Label lblUppersectionBonus = new Label();
    private final Label lblPointTotal = new Label();
    private int turnNumber = 0;
    private final int[] pointUpperSection = new int[6];
    private int pointSumUpperSection = 0;
    private int pointTotal = 0;
    private boolean diceThrown = false;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Yatzy");
        stage.setMinWidth(400);
        stage.setMinHeight(850);
        GridPane pane = new GridPane();
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        initDice(pane);
        initScoreBoard(pane);
        stage.show();
    }

    private void initDice(GridPane pane) {
//        pane.setGridLinesVisible(true);
        pane.setHgap(10);
        pane.setVgap(10);
        pane.setPadding(new Insets(20));

        HBox diceBox = new HBox();
        pane.add(diceBox, 0, 0, 2, 1);
        diceBox.setSpacing(10);
        diceBox.setPadding(new Insets(0, 0, 0, 20));

        for (int i = 0; i < 5; i++) {
            Rectangle diceRectangle = new Rectangle(50, 50);
            diceRectangle.setArcHeight(20);
            diceRectangle.setArcWidth(20);
            diceRectangle.setFill(Color.LIGHTGRAY);
            diceRectangle.setStroke(Color.BLACK);
            rollText[i] = new Text();
            rollText[i].setStyle("-fx-font: 24 arial;");
            StackPane stackPane = new StackPane(diceRectangle, rollText[i]);
            diceBox.getChildren().add(stackPane);
        }

        HBox diceCheckBoxes = new HBox();
        pane.add(diceCheckBoxes, 0, 1, 2, 1);
        GridPane.setMargin(diceCheckBoxes, new Insets(0, 0, 0, 23));
        diceCheckBoxes.setSpacing(13);

        for (int i = 0; i < 5; i++) {
            checkBoxes[i] = new CheckBox("Hold");
            diceCheckBoxes.getChildren().add(checkBoxes[i]);
        }

        pane.add(remainingThrowsLabel, 1, 2);
        remainingThrowsLabel.setStyle("-fx-font: 14 arial;");


        Button throwDiceButton = new Button("Kast terningerne");
        pane.add(throwDiceButton, 0, 2);
        throwDiceButton.setOnAction(_ -> {
            if (!roundEnd && turnNumber < 15) {
                if (remainingThrows == 0) firstRoll();
                else otherRolls();
            }
        });
        throwDiceButton.requestFocus();
    }

    private void initScoreBoard(GridPane pane) {
        Label scoreBoardBanner = new Label("--Score Board--");
        pane.add(scoreBoardBanner, 0, 3);
        scoreBoardBanner.setStyle("-fx-font: 24 arial;");
        scoreBoardBanner.setAlignment(Pos.CENTER);
        Label[] upperSectionLabels = new Label[6];

        for (int i = 0; i < 6; i++) {
            upperSectionLabels[i] = new Label();
            pane.add(upperSectionLabels[i], 1, i + 4);
            upperSectionLabels[i].setStyle("-fx-font: 18 arial;");

            Button upperSections = new Button(i + 1 + "'ere");
            pane.add(upperSections, 0, i + 4);
            upperSections.setMinWidth(90);

            int finalI = i;
            upperSections.setOnAction(_ -> {
                if (diceThrown && pointUpperSection[finalI] == 0) upperSectionPoint(upperSectionLabels[finalI], finalI + 1);
            });
        }
        pane.add(lblUpperSectionSum, 1, 10);
        lblUpperSectionSum.setStyle("-fx-font: 18 arial;");

        pane.add(lblUppersectionBonus, 1, 11);
        lblUppersectionBonus.setStyle("-fx-font: 18 arial;");

        Label onePairLabel = new Label();
        pane.add(onePairLabel, 1, 12);
        onePairLabel.setStyle("-fx-font: 18 arial;");

        Button onePairButton = new Button("Et par");
        pane.add(onePairButton, 0, 12);
        onePairButton.setMinWidth(90);
        onePairButton.setOnAction(_ -> {
            if (diceThrown && onePairLabel.getText().isEmpty()) {
                onePairAction(onePairLabel);
            }
        });

        Label twoPairLabel = new Label();
        pane.add(twoPairLabel, 1, 13);
        twoPairLabel.setStyle("-fx-font: 18 arial;");

        Button twoPairButton = new Button("To par");
        pane.add(twoPairButton, 0, 13);
        twoPairButton.setMinWidth(90);
        twoPairButton.setOnAction(_ -> {
            if (diceThrown && twoPairLabel.getText().isEmpty()) {
                twoPairAction(twoPairLabel);
            }
        });

        Label threeOfAKindLabel = new Label();
        pane.add(threeOfAKindLabel, 1, 14);
        threeOfAKindLabel.setStyle("-fx-font: 18 arial;");

        Button threeOfAKindButton = new Button("Tre ens");
        pane.add(threeOfAKindButton, 0, 14);
        threeOfAKindButton.setMinWidth(90);
        threeOfAKindButton.setOnAction(_ -> {
            if (diceThrown && threeOfAKindLabel.getText().isEmpty()) {
                threeOfAKindAction(threeOfAKindLabel);
            }
        });

        Label fourOfAKindLabel = new Label();
        pane.add(fourOfAKindLabel, 1, 15);
        fourOfAKindLabel.setStyle("-fx-font: 18 arial;");

        Button fourOfAKindButton = new Button("Fire ens");
        pane.add(fourOfAKindButton, 0, 15);
        fourOfAKindButton.setMinWidth(90);
        fourOfAKindButton.setOnAction(_ -> {
            if (diceThrown && fourOfAKindLabel.getText().isEmpty()) {
                fourOfAKindAction(fourOfAKindLabel);
            }
        });

        Label SmallStraightLabel = new Label();
        pane.add(SmallStraightLabel, 1, 16);
        SmallStraightLabel.setStyle("-fx-font: 18 arial;");

        Button lilleStraightButton = new Button("Lille straight");
        pane.add(lilleStraightButton, 0, 16);
        lilleStraightButton.setMinWidth(90);
        lilleStraightButton.setOnAction(_ -> {
            if (diceThrown && SmallStraightLabel.getText().isEmpty()) {
                smallStraightAction(SmallStraightLabel);
            }
        });

        Label bigStraightLabel = new Label();
        pane.add(bigStraightLabel, 1, 17);
        bigStraightLabel.setStyle("-fx-font: 18 arial;");

        Button storeStraightButton = new Button("Store straight");
        pane.add(storeStraightButton, 0, 17);
        storeStraightButton.setMinWidth(90);
        storeStraightButton.setOnAction(_ -> {
            if (diceThrown && bigStraightLabel.getText().isEmpty()) {
                bigStraightAction(bigStraightLabel);
            }
        });

        Label fullHouseLabel = new Label();
        pane.add(fullHouseLabel, 1, 18);
        fullHouseLabel.setStyle("-fx-font: 18 arial;");

        Button fuldHusButton = new Button("Fuldt hus");
        pane.add(fuldHusButton, 0, 18);
        fuldHusButton.setMinWidth(90);
        fuldHusButton.setOnAction(_ -> {
            if (diceThrown && fullHouseLabel.getText().isEmpty()) {
                fullHouseAction(fullHouseLabel);
            }
        });

        Label chanceLabel = new Label();
        pane.add(chanceLabel, 1, 19);
        chanceLabel.setStyle("-fx-font: 18 arial;");

        Button chanceButten = new Button("Chance");
        pane.add(chanceButten, 0, 19);
        chanceButten.setMinWidth(90);
        chanceButten.setOnAction(_ -> {
            if (diceThrown && chanceLabel.getText().isEmpty()) {
                chanceAction(chanceLabel);
            }
        });

        Label yatzyLabel = new Label();
        pane.add(yatzyLabel, 1, 20);
        yatzyLabel.setStyle("-fx-font: 18 arial;");

        Button yatzyButton = new Button("Yatzy");
        pane.add(yatzyButton, 0, 20);
        yatzyButton.setMinWidth(90);
        yatzyButton.setOnAction(_ -> {
            if (diceThrown && yatzyLabel.getText().isEmpty()) {
                yatzyAction(yatzyLabel);
            }
        });

        pane.add(lblPointTotal, 1, 21);
        lblPointTotal.setStyle("-fx-font: 24 arial;");

        Button genstartButton = new Button("Genstart spil");
        pane.add(genstartButton, 1, 3);
        genstartButton.setOnAction(_ -> {
            restartGame();
            for (Label upperSectionLabel : upperSectionLabels) {
                upperSectionLabel.setText("");
            }
            lblUpperSectionSum.setText("Sum: ");
            lblUppersectionBonus.setText("");
            onePairLabel.setText("");
            twoPairLabel.setText("");
            threeOfAKindLabel.setText("");
            fourOfAKindLabel.setText("");
            SmallStraightLabel.setText("");
            bigStraightLabel.setText("");
            fullHouseLabel.setText("");
            chanceLabel.setText("");
            yatzyLabel.setText("");
        });
    }

    private void firstRoll() {
        raffleCup.throwDice();
        diceThrown = true;
        for (int i = 0; i < dice.length; i++) {
            rollText[i].setText(Integer.toString(dice[i].getEyes()));
            checkBoxes[i].setSelected(false);
        }
        remainingThrows = 2;
        remainingThrowsLabel.setText("Antal kast tilbage: " + remainingThrows);
    }

    private void otherRolls() {
        for (int i = 0; i < dice.length; i++) {
            if (!checkBoxes[i].isSelected()) {
                dice[i].roll();
                rollText[i].setText(Integer.toString(dice[i].getEyes()));
            }
        }
        remainingThrows--;
        if (remainingThrows > 0) {
            remainingThrowsLabel.setText("Antal kast tilbage: " + remainingThrows);

        } else {
            roundEnd = true;
            remainingThrowsLabel.setText("Ikke flere kast. Vælg point.");
        }
    }

    private void newTurn() {
        roundEnd = false;
        diceThrown = false;
        remainingThrows = 0;
        bonus = false;

        lblPointTotal.setText("Point total: " + pointTotal);
        if (turnNumber < 14) {
            remainingThrowsLabel.setText("Rul igen");
        } else {
            remainingThrowsLabel.setText("Spillet er slut");
        }
        turnNumber++;
        for (CheckBox checkBox : checkBoxes) {
            checkBox.setSelected(false);
        }
        for (Text text : rollText) {
            text.setText("");
        }

    }

    private void restartGame() {
        newTurn();
        lblPointTotal.setText("");
        remainingThrowsLabel.setText("");
        turnNumber = 0;
        pointTotal = 0;
        if (pointSumUpperSection > 0) {
            for (int i = 0; i < 6; i++) {
                pointUpperSection[i] = 0;
            }
            pointSumUpperSection = 0;
        }
    }

    private void upperSectionPoint(Label label, int eyes) {
        YatzyResultCalculator yatzyResultCalculator = new YatzyResultCalculator(this.dice);
        pointUpperSection[eyes - 1] = yatzyResultCalculator.upperSectionScore(eyes);
        label.setText(Integer.toString(pointUpperSection[eyes - 1]));
        pointSumUpperSection += yatzyResultCalculator.upperSectionScore(eyes);
        if (pointSumUpperSection >= 63 && !bonus) {
            lblUppersectionBonus.setText("Bonus: 50");
            bonus = true;
            pointTotal += 50;
        }
        lblUpperSectionSum.setText("Sum: " + pointSumUpperSection);
        pointTotal += yatzyResultCalculator.upperSectionScore(eyes);
        newTurn();
    }

    private void onePairAction(Label onePairLabel) {
        YatzyResultCalculator yatzyResultCalculator = new YatzyResultCalculator(this.dice);
        pointTotal += yatzyResultCalculator.onePairScore();
        onePairLabel.setText(Integer.toString(yatzyResultCalculator.onePairScore()));
        newTurn();
    }

    private void twoPairAction(Label twoPairLabel) {
        YatzyResultCalculator yatzyResultCalculator = new YatzyResultCalculator(dice);
        pointTotal += yatzyResultCalculator.twoPairScore();
        twoPairLabel.setText(Integer.toString(yatzyResultCalculator.twoPairScore()));
        newTurn();
    }

    private void threeOfAKindAction(Label threeOfAKindLabel) {
        YatzyResultCalculator yatzyResultCalculator = new YatzyResultCalculator(dice);
        pointTotal += yatzyResultCalculator.threeOfAKindScore();
        threeOfAKindLabel.setText(Integer.toString(yatzyResultCalculator.threeOfAKindScore()));
        newTurn();
    }

    private void fourOfAKindAction(Label label) {
        YatzyResultCalculator yatzyResultCalculator = new YatzyResultCalculator(dice);
        pointTotal += yatzyResultCalculator.fourOfAKindScore();
        label.setText(Integer.toString(yatzyResultCalculator.fourOfAKindScore()));
        newTurn();
    }

    private void smallStraightAction(Label label) {
        YatzyResultCalculator yatzyResultCalculator = new YatzyResultCalculator(dice);
        pointTotal += yatzyResultCalculator.smallStraightScore();
        label.setText(Integer.toString(yatzyResultCalculator.smallStraightScore()));
        newTurn();
    }

    private void bigStraightAction(Label label) {
        YatzyResultCalculator yatzyResultCalculator = new YatzyResultCalculator(dice);
        pointTotal += yatzyResultCalculator.largeStraightScore();
        label.setText(Integer.toString(yatzyResultCalculator.largeStraightScore()));
        newTurn();
    }

    private void fullHouseAction(Label label) {
        YatzyResultCalculator yatzyResultCalculator = new YatzyResultCalculator(dice);
        pointTotal += yatzyResultCalculator.fullHouseScore();
        label.setText(Integer.toString(yatzyResultCalculator.fullHouseScore()));
        newTurn();
    }

    private void chanceAction(Label label) {
        YatzyResultCalculator yatzyResultCalculator = new YatzyResultCalculator(dice);
        pointTotal += yatzyResultCalculator.chanceScore();
        label.setText(Integer.toString(yatzyResultCalculator.chanceScore()));
        newTurn();
    }

    private void yatzyAction(Label label) {
        YatzyResultCalculator yatzyResultCalculator = new YatzyResultCalculator(dice);
        pointTotal += yatzyResultCalculator.yatzyScore();
        label.setText(Integer.toString(yatzyResultCalculator.yatzyScore()));
        newTurn();
    }
}