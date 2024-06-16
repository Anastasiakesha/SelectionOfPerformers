package com.example.selectionofperformers;

import com.example.selectionofperformers.DataBase.DBWorker;
import com.example.selectionofperformers.Presentation.TableModel.Model;
import com.example.selectionofperformers.Presentation.Views.ViewFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        DBWorker.initDB();
        ViewFactory viewFactory = new ViewFactory();
        Model.getInstance().getViewFactory().showLoginWindow();

    }

    public static void main(String[] args) {
        launch();
    }
}