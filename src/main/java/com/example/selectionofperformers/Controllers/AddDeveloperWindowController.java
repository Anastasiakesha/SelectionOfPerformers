package com.example.selectionofperformers.Controllers;

import com.example.selectionofperformers.DataBase.DBWorker;
import com.example.selectionofperformers.Presentation.TableModel.Model;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class AddDeveloperWindowController implements Initializable {
    @FXML
    public TextField developerName_fld_developer;
    @FXML
    public TextField developerEmail_fld_developer;
    @FXML
    public TextField developerPhone_fld_developer;
    @FXML
    public TextField developerRating_fld_developer;
    @FXML
    public ComboBox<String> developerStatus_fld_developer;
    @FXML
    public Button AddDeveloper_btn;
    @FXML
    public Button cancelAddDeveloper_btn;
    @FXML
    public TextField skills_fld_developer;

    private DBWorker repository;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        repository = new DBWorker();
        developerStatus_fld_developer.getItems().addAll("Работает", "На больничном", "В отпуске", "Уволен");

        AddDeveloper_btn.setOnAction(event -> {

            try {
                if (developerName_fld_developer.getText().isEmpty() || developerEmail_fld_developer.getText().isEmpty() || developerPhone_fld_developer.getText().isEmpty() ||
                        developerRating_fld_developer.getText().isEmpty() || developerStatus_fld_developer.getValue() == null || skills_fld_developer.getText().isEmpty()) {
                    showErrorMessage("Пожалуйста, заполните все обязательные поля");
                    return;
                }

                if (!isValidPhoneNumber(developerPhone_fld_developer.getText())) {
                    showErrorMessage("Неверный формат номера телефона");
                    return;
                }


                if (!isValidRating(developerRating_fld_developer.getText())) {
                    showErrorMessage("Неверный формат рейтинга. Введите значение до 5");
                    return;
                }

                if (!isValidEmail(developerEmail_fld_developer.getText())) {
                    showErrorMessage("Неверный формат электронной почты");
                    return;
                }

                addDeveloper();
                Model.getInstance().getViewFactory().showDeveloperWindow();
                closeAllWindows();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        cancelAddDeveloper_btn.setOnAction(event -> {
           Model.getInstance().getViewFactory().showDeveloperWindow();
            Stage stage = (Stage) cancelAddDeveloper_btn.getScene().getWindow();
            Model.getInstance().getViewFactory().closeStage(stage);
        });
        addRatingValidation(developerRating_fld_developer);
        addPhoneValidation(developerPhone_fld_developer);
    }

    private void addRatingValidation(TextField ratingField) {
        ratingField.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            String text = ratingField.getText();
            if (!event.getCharacter().matches("\\d")) {
                event.consume();
                return;
            }
            if (text.length() == 1 && !text.contains(".")) {
                ratingField.setText(text + ".");
                ratingField.positionCaret(ratingField.getText().length());
            }
            if (text.length() >= 3) {
                event.consume();
            }
        });
    }

    private void addPhoneValidation(TextField phoneField) {
        final boolean[] hasOpeningBracket = {false};

        phoneField.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (Character.isLetter(event.getCharacter().charAt(0))) {
                event.consume();
                return;
            }

            if (phoneField.getText().length() < 14) {
                if (phoneField.getText().isEmpty() && !hasOpeningBracket[0]) {
                    phoneField.setText("+7(");
                    phoneField.positionCaret(3);
                    hasOpeningBracket[0] = true;
                } else if (phoneField.getText().length() == 6 && hasOpeningBracket[0]) {
                    phoneField.setText(phoneField.getText() + ")");
                    phoneField.positionCaret(7);
                    hasOpeningBracket[0] = false;
                }
            } else {
                event.consume();
            }
        });
    }

    private void addDeveloper() throws SQLException {
        String developerName = developerName_fld_developer.getText().trim();
        String developerEmail = developerEmail_fld_developer.getText().trim();
        String developerPhone = developerPhone_fld_developer.getText().trim();
        double developerRating = Double.parseDouble(developerRating_fld_developer.getText().trim());
        String developerStatus = developerStatus_fld_developer.getValue();
        String skills = skills_fld_developer.getText().trim();

        repository.addDeveloper(developerName, developerEmail, developerPhone, developerRating, developerStatus, skills);
        Model.getInstance().getViewFactory().showDeveloperWindow();
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "^\\+7\\(\\d{3}\\)\\d{7}$";
        return Pattern.matches(regex, phoneNumber);
    }

    private boolean isValidRating(String rating) {
        String regex = "^\\d{1}\\.\\d{1}$";
        return Pattern.matches(regex, rating);
    }
    private boolean isValidEmail(String email) {
        return email.contains("@");
    }



    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void closeWindow() {
        Stage stage = (Stage) AddDeveloper_btn.getScene().getWindow();
        stage.close();
    }
    private void closeAllWindows() {
        closeWindow();
        Model.getInstance().getViewFactory().closeStage((Stage) developerStatus_fld_developer.getScene().getWindow());
    }
}
