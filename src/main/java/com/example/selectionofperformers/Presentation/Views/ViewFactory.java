package com.example.selectionofperformers.Presentation.Views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;


public class ViewFactory {
    public ViewFactory(){}

   public void showDealWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/selectionofperformers/FXMLs/Deal.fxml"));
        Scene scene = null;
        try{
            scene = new Scene(loader.load());
        } catch (Exception e){
            e.printStackTrace();
        }
        Stage stage  = new Stage();
        stage.setScene(scene);
        stage.setTitle("Работа со сделками");
        stage.show();
    }

    public void showProjectWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/selectionofperformers/FXMLs/Project.fxml"));
        Scene scene = null;
        try{
            scene = new Scene(loader.load());
        } catch (Exception e){
            e.printStackTrace();
        }
        Stage stage  = new Stage();
        stage.setScene(scene);
        stage.setTitle("Работа с проектами");
        stage.show();
    }


    public void showDeveloperWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/selectionofperformers/FXMLs/Developer.fxml"));
        Scene scene = null;
        try{
            scene = new Scene(loader.load());
        } catch (Exception e){
            e.printStackTrace();
        }
        Stage stage  = new Stage();
        stage.setScene(scene);
        stage.setTitle("Работа с исполнителями");
        stage.show();
    }

    public void showLoginWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/selectionofperformers/FXMLs/Login.fxml"));
        Scene scene = null;
        try{
            scene = new Scene(loader.load());
        } catch (Exception e){
            e.printStackTrace();
        }
        Stage stage  = new Stage();
        stage.setScene(scene);
        stage.setTitle("Войти");
        stage.show();
    }


    public void showMenuWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/selectionofperformers/FXMLs/Menu.fxml"));
        Scene scene = null;
        try{
            scene = new Scene(loader.load());
        } catch (Exception e){
            e.printStackTrace();
        }
        Stage stage  = new Stage();
        stage.setScene(scene);
        stage.setTitle("Меню");
        stage.show();
    }
    public void showAddDealWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/selectionofperformers/FXMLs/AddDealWindow.fxml"));
        Scene scene = null;
        try{
            scene = new Scene(loader.load());
        } catch (Exception e){
            e.printStackTrace();
        }
        Stage stage  = new Stage();
        stage.setScene(scene);
        stage.setTitle("Добавить сделку");
        stage.show();
    }

    public void showAddDeveloperWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/selectionofperformers/FXMLs/AddDeveloperWindow.fxml"));
        Scene scene = null;
        try{
            scene = new Scene(loader.load());
        } catch (Exception e){
            e.printStackTrace();
        }
        Stage stage  = new Stage();
        stage.setScene(scene);
        stage.setTitle("Добавить исполнителя");
        stage.show();
    }

    public void showAddProjectWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/selectionofperformers/FXMLs/AddProjectWindow.fxml"));
        Scene scene = null;
        try{
            scene = new Scene(loader.load());
        } catch (Exception e){
            e.printStackTrace();
        }
        Stage stage  = new Stage();
        stage.setScene(scene);
        stage.setTitle("Добавить проект");
        stage.show();
    }



    public void closeStage(Stage stage){
        stage.close();
    }
}
