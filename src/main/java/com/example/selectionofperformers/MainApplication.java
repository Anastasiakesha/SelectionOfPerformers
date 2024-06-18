package com.example.selectionofperformers;

import com.example.selectionofperformers.domain.DataBase.DBWorker;
import com.example.selectionofperformers.Presentation.Model.Model;
import com.example.selectionofperformers.Presentation.Views.ViewFactory;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

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