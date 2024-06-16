package com.example.selectionofperformers.Controllers;

import com.example.selectionofperformers.DataBase.DBWorker;
import com.example.selectionofperformers.Presentation.TableModel.Model;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
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

public class AddDealWindowController implements Initializable {
    @FXML
    public ComboBox<String> developerName_fld_deal;
    @FXML
    public TextField developerEmail_fld_deal;
    @FXML
    public TextField developerPhone_fld_deal;
    @FXML
    public TextField clientName_fld_deal;
    @FXML
    public TextField clientEmail_fld_deal;
    @FXML
    public TextField clientPhone_fld_deal;
    @FXML
    public Button AddDeal_btn;
    @FXML
    public Button cancelAddDeal_btn;
    @FXML
    public TextField dealAmount_fld_deal;
    @FXML
    public ComboBox<String> paymentStatus_fld_deal;
    @FXML
    public TextField dealDate_fld_deal;
    @FXML
    public TextField dealName_fld_deal;

    private DBWorker repository;
    private ObservableList<String[]> developersData;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        repository = new DBWorker();
        paymentStatus_fld_deal.getItems().addAll("Оплачено", "Не оплачено");

        try {
            developersData = repository.getDeveloperData();
            populateDevelopersComboBox();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        setupDeveloperComboBox();

        AddDeal_btn.setOnAction(event -> {
            try {
                if (developerName_fld_deal.getSelectionModel().isEmpty() || developerEmail_fld_deal.getText().isEmpty() || developerPhone_fld_deal.getText().isEmpty() ||
                        clientName_fld_deal.getText().isEmpty() || clientEmail_fld_deal.getText().isEmpty() || clientPhone_fld_deal.getText().isEmpty() ||
                        dealAmount_fld_deal.getText().isEmpty() || paymentStatus_fld_deal.getValue() == null || dealDate_fld_deal.getText().isEmpty() || dealName_fld_deal.getText().isEmpty()) {
                    showErrorMessage("Пожалуйста, заполните все обязательные поля");
                    return;
                }

                if (!isValidPhoneNumber(developerPhone_fld_deal.getText())) {
                    showErrorMessage("Неверный формат номера телефона");
                    return;
                }

                if (!isValidPhoneNumber(clientPhone_fld_deal.getText())) {
                    showErrorMessage("Неверный формат номера телефона");
                    return;
                }

                if (!isValidDate(dealDate_fld_deal.getText())) {
                    showErrorMessage("Неверный формат даты. Используйте формат ДД.ММ.ГГГГ");
                    return;
                }

                if (!isDateNotInFuture(dealDate_fld_deal.getText())) {
                    showErrorMessage("Неверная дата. Пожалуйста, укажите верную дату");
                    return;
                }



                addDeal();
                Model.getInstance().getViewFactory().showDealWindow();
                closeAllWindows();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        cancelAddDeal_btn.setOnAction(event -> {
           Model.getInstance().getViewFactory().showDealWindow();
            Stage stage = (Stage) cancelAddDeal_btn.getScene().getWindow();
            Model.getInstance().getViewFactory().closeStage(stage);
        });

        addPhoneValidation(developerPhone_fld_deal);
        addPhoneValidation(clientPhone_fld_deal);
        addDateValidation(dealDate_fld_deal);
        addAmountValidation(dealAmount_fld_deal);
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
        developerName_fld_deal.setCellFactory(cellFactory);
        developerName_fld_deal.setButtonCell(cellFactory.call(null));

        developerName_fld_deal.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                try {
                    for (String[] developerData : developersData) {
                        if (developerData[0].equals(newValue)) {
                            developerEmail_fld_deal.setText(developerData[1]);
                            developerPhone_fld_deal.setText(developerData[2]);
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
            developerName_fld_deal.getItems().add(developerData[0]);
        }
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

        public  void addAmountValidation(TextField dealAmountField) {
            dealAmountField.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if (!Character.isDigit(event.getCharacter().charAt(0))) {
                        event.consume(); // Игнорировать все символы, кроме цифр
                    }
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

    private void addDeal() throws SQLException {
        String developerName = developerName_fld_deal.getSelectionModel().getSelectedItem();
        String developerEmail = developerEmail_fld_deal.getText().trim();
        String developerPhone = developerPhone_fld_deal.getText().trim();
        String dealName = dealName_fld_deal.getText().trim();
        String dealDate = dealDate_fld_deal.getText().trim();
        String dealAmount = dealAmount_fld_deal.getText().trim();
        String dealPaymentStatus = paymentStatus_fld_deal.getValue();
        String clientName = clientName_fld_deal.getText().trim();
        String clientEmail = clientEmail_fld_deal.getText().trim();
        String clientPhone = clientPhone_fld_deal.getText().trim();

        repository.addDeal(developerName, developerEmail, developerPhone, dealName, dealDate, Double.parseDouble(dealAmount), dealPaymentStatus, clientName, clientEmail, clientPhone);
        Model.getInstance().getViewFactory().showDealWindow();
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "^\\+7\\(\\d{3}\\)\\d{7}$";
        return Pattern.matches(regex, phoneNumber);
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
        Stage stage = (Stage) AddDeal_btn.getScene().getWindow();
        stage.close();
    }
    private void closeAllWindows() {
        closeWindow();
        Model.getInstance().getViewFactory().closeStage((Stage) paymentStatus_fld_deal.getScene().getWindow());
    }
}
