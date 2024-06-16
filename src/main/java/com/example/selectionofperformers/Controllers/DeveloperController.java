package com.example.selectionofperformers.Controllers;

import com.example.selectionofperformers.DataBase.DBWorker;
import com.example.selectionofperformers.DataBase.Repository;
import com.example.selectionofperformers.Presentation.TableModel.Model;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class DeveloperController implements Initializable {
    @FXML
    public Label tools_lbl_developer;
    @FXML
    public Button add_btn_developer;
    @FXML
    public Button edit_btn_developer;
    @FXML
    public TableView<String[]> developerTable;
    @FXML
    public TableColumn<String[], String> developerName_col_developer;
    @FXML
    public TableColumn<String[], String> developerEmail_col_developer;
    @FXML
    public TableColumn<String[], String> developerPhone_col_developer;
    @FXML
    public TableColumn<String[], String> developerRating_col_developer;
    @FXML
    public Button back_btn_developer;
    @FXML
    public TableColumn<String[], String> skills_col_developer;
    @FXML
    public TableColumn<String[], String> status_col_developer;
    @FXML
    public TextField developerSearch_fld;
    @FXML
    public Button developerSearch_btn;
    @FXML
    public Label resultSearch_lbl; // Добавляем Label для вывода количества найденных строк
    @FXML
    public ObservableList<String[]> fullData;
    private DBWorker repository;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        repository = new DBWorker();
        back_btn_developer.setOnAction(event -> {
            Model.getInstance().getViewFactory().showMenuWindow();
            Stage stage = (Stage) back_btn_developer.getScene().getWindow();
            Model.getInstance().getViewFactory().closeStage(stage);
        });
        //back_btn_developer.setOnAction(event -> Model.getInstance().getViewFactory().showMenuWindow());
        add_btn_developer.setOnAction(event -> {
            Model.getInstance().getViewFactory().showAddDeveloperWindow();
            Stage stage = (Stage) add_btn_developer.getScene().getWindow();
            Model.getInstance().getViewFactory().closeStage(stage);
        });


        developerSearch_btn.setOnAction(event -> searchDevelopers());

        // Добавляем слушателя изменений текста в текстовом поле
        developerSearch_fld.textProperty().addListener((observable, oldValue, newValue) -> searchDevelopers());

        setupDeveloperTable();
    }

    private void setupDeveloperTable() {
        developerName_col_developer.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[0]));
        developerEmail_col_developer.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[1]));
        developerPhone_col_developer.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[2]));
        developerRating_col_developer.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[3]));
        status_col_developer.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[4]));
        skills_col_developer.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[5]));

        try {
            loadData();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void loadData() throws SQLException {
        //DBWorker.initDB();
        fullData = repository.getDeveloperData();
        developerTable.setItems(fullData);
        developerTable.refresh();

    }
    private void searchDevelopers() {
        String searchText = developerSearch_fld.getText().toLowerCase();
        ObservableList<String[]> filteredData;
        if (searchText.isEmpty()) {
            filteredData = fullData;
            resultSearch_lbl.setText("Введите текст для поиска"); // Сообщение для пустого текстового поля
        } else {
            filteredData = FXCollections.observableArrayList(
                    fullData.stream().filter(developer ->
                                    developer[0].toLowerCase().contains(searchText) ||
                                            developer[1].toLowerCase().contains(searchText) ||
                                            developer[2].toLowerCase().contains(searchText) ||
                                            developer[3].toLowerCase().contains(searchText) ||
                                            developer[4].toLowerCase().contains(searchText) ||
                                            developer[5].toLowerCase().contains(searchText))
                            .collect(Collectors.toList())
            );
            resultSearch_lbl.setText("Найдено записей: " + filteredData.size()); // Обновляем Label с количеством найденных строк
        }
        developerTable.setItems(filteredData);
    }
}