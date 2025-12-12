package com.skeletoncoder.project.gui;

import com.skeletoncoder.project.fileHandling.FileInputLoader;
import com.skeletoncoder.project.fileHandling.HistoryManager;
import com.skeletoncoder.project.input.InputParser;
import com.skeletoncoder.project.input.ParsedResult;
import com.skeletoncoder.project.output.OutputProcessing;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;


public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) {

        Label inputLabel = new Label("Enter:");
        TextArea inputArea = new TextArea();
        inputArea.setPrefRowCount(5);

        Button generateBtn = new Button("Generate Code");
        Button loadFileBtn = new Button("File Input");
        Button saveHistoryBtn = new Button("Save History");
        Button viewHistoryBtn = new Button("View History");

        Label outputLabel = new Label("Code:");
        TextArea outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setPrefRowCount(15);


        generateBtn.setOnAction(e -> {
            try {
                String input = inputArea.getText().trim();
                if (input.isEmpty()) {
                    showError("Input is empty.");
                    return;
                }

                ParsedResult result = InputParser.validate(input);
                String code = OutputProcessing.generateCode(result);
                outputArea.setText(code);


                HistoryManager.addHistory(input);

            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });


        loadFileBtn.setOnAction(e -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Open Input File");
            File file = chooser.showOpenDialog(stage);

            if (file != null) {
                inputArea.setText(FileInputLoader.loadInput(file.getAbsolutePath()));
            }
        });


        saveHistoryBtn.setOnAction(e -> {
            HistoryManager.saveHistoryToFile();
            showInfo("Saved", "History has been saved successfully.");
        });


        viewHistoryBtn.setOnAction(e -> showHistoryWindow());


        VBox root = new VBox(10,
                inputLabel, inputArea,
                generateBtn, loadFileBtn, saveHistoryBtn, viewHistoryBtn,
                outputLabel, outputArea
        );

        root.setPadding(new Insets(15));

        stage.setTitle("Skeleton Coder");
        stage.setScene(new Scene(root, 750, 600));
        stage.show();
    }


    private void showHistoryWindow() {
        List<String> history = HistoryManager.loadHistory();

        Stage popup = new Stage();
        popup.setTitle("Input History");

        ListView<String> listView = new ListView<>();
        listView.getItems().addAll(history);

        VBox box = new VBox(10, new Label("Previous Inputs:"), listView);
        box.setPadding(new Insets(10));

        popup.setScene(new Scene(box, 400, 300));
        popup.show();
    }


    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Invalid Input");
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void showInfo(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
