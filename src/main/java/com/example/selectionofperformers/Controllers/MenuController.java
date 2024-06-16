package com.example.selectionofperformers.Controllers;

import com.example.selectionofperformers.Presentation.TableModel.Model;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {
    @FXML
    public Button developerWork_btn;
    @FXML
    public Button dealWork_btn;
    @FXML
    public Button projectWork_btn;
    @FXML
    public Button quit_btn;
    @FXML
    public Button feedback_btn;
    @FXML
    public Label welcome_lbl;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        developerWork_btn.setOnAction(event -> {
            Model.getInstance().getViewFactory().showDeveloperWindow();
            Stage stage = (Stage) developerWork_btn.getScene().getWindow();
            Model.getInstance().getViewFactory().closeStage(stage);
        });
        dealWork_btn.setOnAction(event -> {
            Model.getInstance().getViewFactory().showDealWindow();
            Stage stage = (Stage) dealWork_btn.getScene().getWindow();
            Model.getInstance().getViewFactory().closeStage(stage);
        });
        projectWork_btn.setOnAction(event -> {
            Model.getInstance().getViewFactory().showProjectWindow();
            Stage stage = (Stage) projectWork_btn.getScene().getWindow();
            Model.getInstance().getViewFactory().closeStage(stage);
        });
        feedback_btn.setOnAction(event -> {
            try {
                java.awt.Desktop.getDesktop().browse(new URI("mailto:gegel.nastya@mail.ru"));
            } catch (URISyntaxException | IOException e) {
                e.printStackTrace();
            }

        });
        quit_btn.setOnAction(event -> {
            Stage currentStage = (Stage) quit_btn.getScene().getWindow();

            currentStage.close();

            Model.getInstance().getViewFactory().showLoginWindow();
        });
    }

}