package com.autokeyboard.autoclicker;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.*;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import java.util.logging.Level;
import java.util.logging.Logger;

public class autoClicker extends Application {
    @Override
    public void start(Stage stage) {
        GridPane root = new GridPane();
        root.setHgap(10);
        root.setVgap(10);

        //Click interval
        TextField milliseconds = new TextField("100");
        milliseconds.setPrefWidth(45);
        TextField seconds = new TextField("0");
        seconds.setPrefWidth(45);
        TextField minutes = new TextField("0");
        minutes.setPrefWidth(45);
        TextField hours = new TextField("0");
        hours.setPrefWidth(45);
        Label millisecondsLabel = new Label("milliseconds", milliseconds);
        Label secondsLabel = new Label("seconds", seconds);
        Label minutesLabel = new Label("minutes", minutes);
        Label hoursLabel = new Label("hours", hours);

        //Click options
        Text mouseOptionsText = new Text("Mouse button :");
        Text clickOptionsText = new Text("Click type :");
        ObservableList<String> mouseOptions =
                FXCollections.observableArrayList(
                        "Left",
                        "Right"
                );
        ObservableList<String> clickOptions =
                FXCollections.observableArrayList(
                        "Single",
                        "Double"
                );

        final ComboBox mouseOptionsBox = new ComboBox(mouseOptions);
        mouseOptionsBox.getSelectionModel().selectFirst();
        final ComboBox clickOptionsBox = new ComboBox(clickOptions);
        clickOptionsBox.getSelectionModel().selectFirst();

        //Click repeat
        ToggleGroup optionsGroup = new ToggleGroup();
        RadioButton repeatUntilStopped = new RadioButton("Repeat until stopped");
        RadioButton repeat = new RadioButton("Repeat");
        TextField times = new TextField("1");
        times.setPrefWidth(45);
        Label repeatLabel = new Label("times", times);
        repeatUntilStopped.setToggleGroup(optionsGroup);
        repeat.setToggleGroup(optionsGroup);
        optionsGroup.selectToggle(repeatUntilStopped);

        //Cursor position
        ToggleGroup positionGroup = new ToggleGroup();
        RadioButton current = new RadioButton("Current Position");
        RadioButton pickLocation = new RadioButton("Pick Position");
        TextField xLocation = new TextField("0");
        xLocation.setPrefWidth(45);
        TextField yLocation = new TextField("0");
        yLocation.setPrefWidth(45);
        Label xLabel = new Label("X", xLocation);
        Label yLabel = new Label("Y", yLocation);
        current.setToggleGroup(positionGroup);
        pickLocation.setToggleGroup(positionGroup);
        positionGroup.selectToggle(current);

        //Start and Stop
        Button startBtn = new Button("Start (F6)");
        startBtn.setPrefSize(220, 50);
        Button stopBtn = new Button("Stop (F6)");
        stopBtn.setPrefSize(220, 50);
        stopBtn.setDisable(true);
        startBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stopBtn.setDisable(false);
                startBtn.setDisable(true);
                autoClick.start(hours.getText(), minutes.getText(), seconds.getText(), milliseconds.getText(), mouseOptionsBox.getItems().toString(), clickOptionsBox.getItems().toString(),
                        getSelectedRadioButtonText(optionsGroup), times.getText(), getSelectedRadioButtonText(positionGroup), xLocation.getText(), yLocation.getText());
            }
        });
        stopBtn.setOnAction(actionEvent -> {
            stopBtn.setDisable(true);
            startBtn.setDisable(false);
            autoClick.stop();
        });

        Scene scene = new Scene(root, 475, 315);

        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());
            return;
        }

        // Disable JNativeHook's logging
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);

        // Add a native key listener
        GlobalScreen.addNativeKeyListener(new NativeKeyListener() {
            @Override
            public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
                if (nativeKeyEvent.getKeyCode() == NativeKeyEvent.VC_F6) {
                    Platform.runLater(() -> {
                        if (stopBtn.isDisabled()) {
                            stopBtn.setDisable(false);
                            startBtn.setDisable(true);
                            autoClick.start(hours.getText(), minutes.getText(), seconds.getText(), milliseconds.getText(), mouseOptionsBox.getSelectionModel().getSelectedItem().toString(), clickOptionsBox.getSelectionModel().getSelectedItem().toString(),
                                    getSelectedRadioButtonText(optionsGroup), times.getText(), getSelectedRadioButtonText(positionGroup), xLocation.getText(), yLocation.getText());
                        }
                        else {
                            stopBtn.setDisable(true);
                            startBtn.setDisable(false);
                            autoClick.stop();
                        }
                    });
                }
            }
        });

        //GUI
        root.setPadding(new Insets(10, 10, 10, 10));
        root.addRow(0, new Text("Click Interval"));
        root.addRow(1, hoursLabel, minutesLabel, secondsLabel, millisecondsLabel);
        root.addRow(2, new Text("Click Options"));
        root.addRow(3, mouseOptionsText, mouseOptionsBox, clickOptionsText, clickOptionsBox);
        root.addRow(4, new Text("Click Repeat"));
        root.add(repeatUntilStopped, 0, 5, 2, 1);
        root.addRow(5, repeat, repeatLabel);
        root.addRow(6, new Text("Click Position"));
        root.addRow(7, current, pickLocation, xLabel, yLabel);
        root.add(startBtn, 0, 8, 2, 1);
        root.add(stopBtn, 2, 8, 2, 1);
        stage.setScene(scene);
        stage.setTitle("AutoClicker");
        stage.show();
    }

    private String getSelectedRadioButtonText(ToggleGroup toggleGroup) {
        RadioButton selectedRadioButton = (RadioButton) toggleGroup.getSelectedToggle();
        if (selectedRadioButton != null) {
            return selectedRadioButton.getText();
        }
        return "None selected";
    }


    public static void main(String[] args) {
        launch();
    }
}
