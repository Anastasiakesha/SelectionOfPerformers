package com.example.selectionofperformers.Controllers;

import com.example.selectionofperformers.DataBase.DBWorker;
import com.example.selectionofperformers.Presentation.TableModel.Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class AddProjectWindowController implements Initializable {
    @FXML
    public ComboBox<String> developerName_fld_project;
    @FXML
    public TextField developerEmail_fld_project;
    @FXML
    public TextField developerPhone_fld_project;
    @FXML
    public TextField developerRating_fld_project;
    @FXML
    public TextField projectName_fld_project;
    @FXML
    public ComboBox<String> projectStatus_fld_project;
    @FXML
    public TextField startDate_fld_project;
    @FXML
    public TextField endDate_fld_project;
    @FXML
    public TextField commentRating_fld_project;
    @FXML
    public TextField commentText_fld_project;
    @FXML
    public Button AddProject_btn;
    @FXML
    public Button cancelAddProject_btn;
    @FXML
    public ComboBox<String> dealName_fld_project;
    private DBWorker repository;
    private ObservableList<String[]> developersData;
    private ObservableList<String[]> dealsData;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        repository = new DBWorker();

        projectStatus_fld_project.getItems().addAll("В работе", "На доработке", "Выполнен", "Отменен");

        try {
            developersData = repository.getDeveloperData();
            dealsData = repository.getDealData();
            populateDevelopersComboBox();
            populateDealsComboBox();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        setupDeveloperComboBox();
        setupDealComboBox();

        AddProject_btn.setOnAction(event -> {
            try {
                if (developerName_fld_project.getSelectionModel().isEmpty() || developerEmail_fld_project.getText().isEmpty() || developerPhone_fld_project.getText().isEmpty() ||
                        developerRating_fld_project.getText().isEmpty() || projectName_fld_project.getText().isEmpty() || projectStatus_fld_project.getSelectionModel().isEmpty() ||
                        startDate_fld_project.getText().isEmpty() || endDate_fld_project.getText().isEmpty() || commentRating_fld_project.getText().isEmpty() || commentText_fld_project.getText().isEmpty()) {
                    showErrorMessage("Пожалуйста, заполните все обязательные поля");
                    return;
                }

                if (!isValidPhoneNumber(developerPhone_fld_project.getText())) {
                    showErrorMessage("Неверный формат номера телефона");
                    return;
                }

                if (!isValidRating(commentRating_fld_project.getText())) {
                    showErrorMessage("Неверный формат рейтинга. Введите значение до 5");
                    return;
                }



                if (!isValidDate(startDate_fld_project.getText()) ) {
                    showErrorMessage("Неверный формат даты. Используйте формат ДД.ММ.ГГГГ");
                    return;
                }

                if (!isDateNotInFuture(startDate_fld_project.getText()) ) {
                    showErrorMessage("Неверная дата. Пожалуйста, укажите верную дату");
                    return;
                }


                addProject();
                Model.getInstance().getViewFactory().showProjectWindow();
                closeAllWindows();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        cancelAddProject_btn.setOnAction(event -> {
            Model.getInstance().getViewFactory().showProjectWindow();
            Stage stage = (Stage) cancelAddProject_btn.getScene().getWindow();
            Model.getInstance().getViewFactory().closeStage(stage);
        });


        addPhoneValidation(developerPhone_fld_project);
        addDateValidation(startDate_fld_project);
        addEndDateValidation(endDate_fld_project);
        addRatingValidation(developerRating_fld_project);
        addRatingValidation(commentRating_fld_project);
    }


    private void setupDeveloperComboBox() {
        Callback<ListView<String>, ListCell<String>> cellFactory = new Callback<>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item);
                        }
                    }
                };
            }
        };

        developerName_fld_project.setCellFactory(cellFactory);
        developerName_fld_project.setButtonCell(cellFactory.call(null));

        developerName_fld_project.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                try {
                    for (String[] developerData : developersData) {
                        if (developerData[0].equals(newValue)) {
                            developerEmail_fld_project.setText(developerData[1]);
                            developerPhone_fld_project.setText(developerData[2]);
                            developerRating_fld_project.setText(developerData[3]);
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void populateDevelopersComboBox() {
        for (String[] developerData : developersData) {
            developerName_fld_project.getItems().add(developerData[0]);
        }
    }

    private void setupDealComboBox() {
        dealName_fld_project.setItems(FXCollections.observableArrayList(getDealNames()));

        dealName_fld_project.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                try {
                    for (String[] dealData : dealsData) {
                        if (dealData[0].equals(newValue)) {
                            projectName_fld_project.setText(dealData[1]);
                            projectStatus_fld_project.setValue(dealData[2]);
                            startDate_fld_project.setText(dealData[3]);
                            endDate_fld_project.setText(dealData[4]);
                            commentRating_fld_project.setText(dealData[5]);
                            commentText_fld_project.setText(dealData[6]);
                            developerName_fld_project.setValue(dealData[7]);
                            developerEmail_fld_project.setText(dealData[8]);
                            developerPhone_fld_project.setText(dealData[9]);
                            developerRating_fld_project.setText(dealData[10]);
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private ObservableList<String> getDealNames() {
        ObservableList<String> dealNames = FXCollections.observableArrayList();
        for (String[] dealData : dealsData) {
            dealNames.add(dealData[3]);
        }
        return dealNames;
    }

    private void populateDealsComboBox() {
        for (String[] dealData : dealsData) {
            dealName_fld_project.getItems().add(dealData[0]);
        }
    }


    private void addProject() throws SQLException {
        String developerName = developerName_fld_project.getSelectionModel().getSelectedItem();
        String developerEmail = developerEmail_fld_project.getText().trim();
        String developerPhone = developerPhone_fld_project.getText().trim();
        double developerRating = Double.parseDouble(developerRating_fld_project.getText().trim());
        String projectName = projectName_fld_project.getText().trim();
        String projectStatus = projectStatus_fld_project.getSelectionModel().getSelectedItem();
        String startDate = startDate_fld_project.getText().trim();
        String endDate = endDate_fld_project.getText().trim();
        double commentRating = Double.parseDouble(commentRating_fld_project.getText().trim());
        String commentText = commentText_fld_project.getText().trim();

        repository.addProject(developerName, developerEmail, developerPhone, developerRating, projectName, projectStatus, startDate, endDate, commentRating, commentText);
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
    private void addEndDateValidation(TextField dateField) {
        dateField.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            String text = dateField.getText();
            String character = event.getCharacter();

            // Проверка на ввод "-" или цифры
            if (!character.matches("\\d|-")) {
                event.consume();
                return;
            }

            // Проверка на ввод "-"
            if (character.equals("-")) {
                // Допускаем ввод только одного "-"
                if (text.contains("-")) {
                    event.consume();
                }
                return;
            }

            // Проверка на ввод цифры
            if (character.matches("\\d")) {
                // Допускаем ввод цифр только в формате DD.MM.YYYY
                if (text.length() == 2 || text.length() == 5) {
                    dateField.setText(text + ".");
                    dateField.positionCaret(dateField.getText().length());
                }
                if (text.length() >= 10) {
                    event.consume();
                }
            }
        });
    }
    private void addDateValidation(TextField dateField) {
        dateField.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            String text = dateField.getText();
            if (!event.getCharacter().matches("\\d")) {
                event.consume();
                return;
            }
            if (text.length() == 2 || text.length() == 5) {
                dateField.setText(text + ".");
                dateField.positionCaret(dateField.getText().length());
            }
            if (text.length() >= 10) {
                event.consume();
            }
        });
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
    private boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "^\\+7\\(\\d{3}\\)\\d{3}\\d{4}$";
        return Pattern.matches(regex, phoneNumber);
    }


    private boolean isValidRating(String rating) {
        String regex = "^\\d{1}\\.\\d{1}$";
        return Pattern.matches(regex, rating);
    }


    private boolean isValidDate(String date) {
        String regex = "^\\d{2}\\.\\d{2}\\.\\d{4}$";
        return Pattern.matches(regex, date);
    }

    private boolean isDateNotInFuture(String date) {
        try {
            LocalDate inputDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            return !inputDate.isAfter(LocalDate.now());
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeWindow() {
        Stage stage = (Stage) AddProject_btn.getScene().getWindow();
        stage.close();
    }

    private void closeAllWindows() {
        closeWindow();
        Model.getInstance().getViewFactory().closeStage((Stage) projectStatus_fld_project.getScene().getWindow());
    }
}
