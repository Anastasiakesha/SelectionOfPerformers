package com.example.selectionofperformers.Controllers;

import com.example.selectionofperformers.DataBase.DBWorker;
import com.example.selectionofperformers.Entity.User;
import com.example.selectionofperformers.Presentation.TableModel.Model;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    public Label login_lbl;

    @FXML
    public TextField login_fld;

    @FXML
    public Label password_lbl;

    @FXML
    public TextField password_fld;

    @FXML
    public Button login_btn;

    @FXML
    public Label error_lbl;
    private DBWorker repository;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        repository = new DBWorker();

        login_btn.setOnAction(event -> {
            String loginText = login_fld.getText().trim(); // без пробелов
            String passwordText = password_fld.getText().trim(); // без пробелов

            if (!loginText.equals("") && !passwordText.equals("")) {
                try {
                    loginUser(loginText, passwordText);
                } catch (SQLException e) {
                    error_lbl.setText("Ошибка при проверке данных пользователя");

                }
            } else {
                error_lbl.setText("Введите логин и пароль");
            }
        });

    }

    private void loginUser(String loginText, String passwordText) throws SQLException {
        User user = new User();
        user.setLogin(loginText);
        user.setPassword(passwordText);
        ResultSet resultSet = repository.getUser(user);

        if (resultSet.next()) { // Проверка, есть ли пользователь в базе данных
            Stage stage = (Stage) error_lbl.getScene().getWindow();
            Model.getInstance().getViewFactory().closeStage(stage);
            Model.getInstance().getViewFactory().showMenuWindow();
        } else {
            error_lbl.setText("Пользователь не найден");

        }
    }
}
