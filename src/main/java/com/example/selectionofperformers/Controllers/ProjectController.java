package com.example.selectionofperformers.Controllers;

import com.example.selectionofperformers.DataBase.DBWorker;
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

public class ProjectController implements Initializable {
    @FXML
    public Label tools_lbl_project;
    @FXML
    public Button add_btn_project;
    @FXML
    public Button delete_btn_project;
    @FXML
    public Button edit_btn_project;
    @FXML
    public TableView<String[]> projectTable;
    @FXML
    public TableColumn<String[], String> developerName_col_project;
    @FXML
    public TableColumn<String[], String> developerEmail_col_project;
    @FXML
    public TableColumn<String[], String> developerPhone_col_project;
    @FXML
    public TableColumn<String[], String> developerRating_col_project;
    @FXML
    public TableColumn<String[], String> projectName_col_project;
    @FXML
    public TableColumn<String[], String> projectStatus_col_project;
    @FXML
    public TableColumn<String[], String> startDate_col_project;
    @FXML
    public TableColumn<String[], String> endDate_col_project;
    @FXML
    public TableColumn<String[], String> reviewRating_col_project;
    @FXML
    public TableColumn<String[], String> comment_col_project;
    @FXML
    public Button back_btn_project;
    @FXML
    public TextField projectSearch_fld;
    @FXML
    public Button projectSearch_btn;
    @FXML
    public Label projectResultSearch_lbl;
    @FXML
    public ObservableList<String[]> fullData;
    private DBWorker repository;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        repository = new DBWorker();

        back_btn_project.setOnAction(event -> {
            Model.getInstance().getViewFactory().showMenuWindow();
            Stage stage = (Stage) back_btn_project.getScene().getWindow();
            Model.getInstance().getViewFactory().closeStage(stage);
        });

        add_btn_project.setOnAction(event -> {
            Model.getInstance().getViewFactory().showAddProjectWindow();
            Stage stage = (Stage) add_btn_project.getScene().getWindow();
            Model.getInstance().getViewFactory().closeStage(stage);
        });

        projectSearch_btn.setOnAction(event -> searchProjects());
        projectSearch_fld.textProperty().addListener((observable, oldValue, newValue) -> searchProjects());
        setupProjectTable();
    }

    private void setupProjectTable() {
        developerName_col_project.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[0]));
        developerEmail_col_project.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[1]));
        developerPhone_col_project.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[2]));
        developerRating_col_project.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[3]));
        projectName_col_project.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[4]));
        projectStatus_col_project.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[5]));
        startDate_col_project.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[6]));
        endDate_col_project.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[7]));
        reviewRating_col_project.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[8]));
        comment_col_project.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[9]));

        try {
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadData() throws SQLException {
        fullData = repository.getProjectData();
        projectTable.setItems(fullData);
        projectTable.refresh();
    }

    private void searchProjects() {
        String searchText = projectSearch_fld.getText().toLowerCase();
        ObservableList<String[]> filteredData;
        if (searchText.isEmpty()) {
            filteredData = fullData;
            projectResultSearch_lbl.setText("Введите текст для поиска"); // Сообщение для пустого текстового поля
        } else {
            filteredData = FXCollections.observableArrayList(
                    fullData.stream().filter(project ->
                                    project[0].toLowerCase().contains(searchText) ||
                                            project[1].toLowerCase().contains(searchText) ||
                                            project[2].toLowerCase().contains(searchText) ||
                                            project[3].toLowerCase().contains(searchText) ||
                                            project[4].toLowerCase().contains(searchText) ||
                                            project[5].toLowerCase().contains(searchText) ||
                                            project[6].toLowerCase().contains(searchText) ||
                                            project[7].toLowerCase().contains(searchText) ||
                                            project[8].toLowerCase().contains(searchText) ||
                                            project[9].toLowerCase().contains(searchText))
                            .collect(Collectors.toList())
            );
            projectResultSearch_lbl.setText("Найдено записей: " + filteredData.size()); // Обновляем Label с количеством найденных строк
        }
        projectTable.setItems(filteredData);
    }
}
