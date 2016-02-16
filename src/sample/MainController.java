package sample;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class MainController extends Window {
    @FXML
    private TabPane tabBox;

    @FXML
    private Button startStop;

    public void onAddAction() {
        Tab t = tabBox.getSelectionModel().getSelectedItem();
        if(!t.getText().equals("+")){
            FlowPane flow = (FlowPane)((ScrollPane)((AnchorPane)t.getContent()).getChildren().get(0)).getContent();
            Pane p = newPanel(flow);
            flow.getChildren().add(p);
        }
    }

    //TODO Save function (https://docs.oracle.com/javase/tutorial/jaxb/intro/)
    //TODO Multiple threads
    //TODO Countdown timer
    //TODO Disable background when choosing path
    //TODO TickBox for running multiple threads at once
    //TODO Time implementation
    //TODO Convert min to seconds and count down on start

    public void onDeleteAction() {

    }

    public void onStartAction() {
        Utilities.startStop(startStop);
    }

    public Pane newPanel(FlowPane parent){
        FlowPane p = new FlowPane();
        Pane p2 = new Pane();
        p.setVgap(2.0);
        p.setHgap(2.0);

        p.setStyle(String.format("-fx-border-color:%s", Utilities.randomColor()));
        p.setPadding(new Insets(2,2,2,2));

        Button addNewrow = new Button("Add command");

        Button startGroup = new Button("GO");
        startGroup.setLayoutX(100);
        startGroup.setPrefWidth(50);

        Label groupLabel = new Label("Group Label");
        groupLabel.setLayoutX(155);
        groupLabel.setLayoutY(5);
        groupLabel.setFont(Font.font("Calibri", FontWeight.BOLD, 14));

        Button deleteGroup = new Button("✖");
        deleteGroup.setLayoutX(550);
        deleteGroup.setPrefWidth(30);
        deleteGroup.setTextFill(Color.DARKRED);

        startGroup.setOnAction(e -> {
            totalTime(p);
            runGroup(p);
        });

        deleteGroup.setOnAction(e -> {
            parent.getChildren().remove(p);
        });

        groupLabel.setOnMouseClicked(event -> {
            TextInputDialog dialog = new TextInputDialog(groupLabel.getText());
            dialog.setTitle("Rename");
            dialog.setHeaderText(null);
            dialog.setContentText(null);

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                groupLabel.setText(result.get());
            }
        });

        addNewrow.setOnAction(e -> p.getChildren().addAll(inputRow(p)));
        p2.getChildren().addAll(addNewrow, startGroup, groupLabel, deleteGroup);
        p.getChildren().addAll(p2, inputRow(p));
        return p;
            }

    private void runGroup(FlowPane p) {
        for (int i = 1; i < (p.getChildren().size()); i++) {
                try {
                    Pane p2 = (Pane) p.getChildren().get(i);
                    String location = ((TextField) p2.getChildren().get(0)).getText();
                    String arguments = ((TextField) p2.getChildren().get(1)).getText();

                    new ProcessBuilder(location, arguments).start();
                } catch (IOException e) {
                    System.out.println("Viga programmi asukoha või argumentide märkimisel!");
                }
        }
    }

    private void totalTime(FlowPane p) {
        int totalTime = 0;
        for (int i = 1; i < (p.getChildren().size()); i++) {
            int rowMin = Integer.parseInt(((TextField)((Pane)p.getChildren().get(i)).getChildren().get(2)).getText());
            totalTime += rowMin;
        }
        System.out.println("Total running time of this group is: " + totalTime);
    }

    public Pane inputRow (Pane p){
        Pane p2 = new Pane();
        double Y = p.getHeight();
        if(Y == 0) Y = 35;
        p2.setLayoutY(Y);

        TextField programName = new TextField();
        programName.setPrefWidth(280);
        programName.setPromptText("Program");

        TextField commandLineInput = new TextField();
        commandLineInput.setPrefWidth(200);
        commandLineInput.setLayoutX(282);
        commandLineInput.setPromptText("CLI");

        TextField timeInput = new TextField();
        timeInput.setPrefWidth(64);
        timeInput.setLayoutX(484);
        timeInput.setPromptText("min");

        Button deleteRow = new Button();
        deleteRow.setPrefWidth(30);
        deleteRow.setLayoutX(550);
        deleteRow.setText("✖");
        deleteRow.setTextFill(Color.DARKRED);

        deleteRow.setOnAction(e -> p.getChildren().remove(p2));

        programName.setOnMouseClicked(e -> {
            File file = new FileChooser().showOpenDialog(this);
            if(file != null){
                programName.setText(file.getAbsolutePath());
            }});

        p2.getChildren().addAll(programName, commandLineInput, timeInput, deleteRow);

        return p2;
    }

    public void onMouseClick() {
        Tab t = tabBox.getSelectionModel().getSelectedItem();
        if(t.getText().equals("+")){
            Tab tab = new Tab("New tab");
            AnchorPane pane = new AnchorPane();
            ScrollPane scroll = new ScrollPane();

            tab.setContextMenu(new ContextMenu());
            MenuItem close = new MenuItem("Close");
            close.setOnAction(a -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to close this tab: " + tab.getText() + "?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    tabBox.getTabs().remove(tab);
                }
            });
            MenuItem rename = new MenuItem("Rename");
            rename.setOnAction(a -> {
                TextInputDialog dialog = new TextInputDialog(tab.getText());
                dialog.setTitle("Rename");
                dialog.setHeaderText(null);
                dialog.setContentText(null);

                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()) {
                    tab.setText(result.get());
                }
            });

            tab.getContextMenu().getItems().addAll(close, rename);

            AnchorPane.setBottomAnchor(scroll, 10.0);
            AnchorPane.setTopAnchor(scroll, 10.0);
            AnchorPane.setLeftAnchor(scroll, 10.0);
            AnchorPane.setRightAnchor(scroll, 10.0);
            scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

            FlowPane flow = new FlowPane();
            flow.setVgap(5.0);
            flow.setPadding(new Insets(5,5,5,5));

            scroll.setContent(flow);
            pane.getChildren().add(scroll);
            tab.setContent(pane);
            tabBox.getTabs().add(tab);
            tabBox.getSelectionModel().select(tab);
        }
    }
}