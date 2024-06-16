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

public class DealController implements Initializable {
    @FXML
    public Label tools_lbl_deal;
    @FXML
    public Button add_btn_deal;
    @FXML
    public Button edit_btn_deal;
    @FXML
    public TableView<String[]> dealTable;
    @FXML
    public TableColumn<String[], String> developerName_col_deal;
    @FXML
    public TableColumn<String[], String> developerEmail_col_deal;
    @FXML
    public TableColumn<String[], String> developerPhone_col_deal;
    @FXML
    public TableColumn<String[], String> orderName_col_deal;
    @FXML
    public TableColumn<String[], String> orderDate_col_deal;
    @FXML
    public TableColumn<String[], String> orderAmount_col_deal;
    @FXML
    public TableColumn<String[], String> paymentStatus_col_deal;
    @FXML
    public TableColumn<String[], String> clientName_col_deal;
    @FXML
    public TableColumn<String[], String> clientEmail_col_deal;
    @FXML
    public TableColumn<String[], String> clientPhone_col_deal;
    @FXML
    public Button back_btn_deal;
    @FXML
    public TextField dealSearch_fld;
    @FXML
    public Button dealSearch_btn;
    @FXML
    public Label dealResultSearch_lbl;
    @FXML
    public ObservableList<String[]> fullData;
    private DBWorker repository;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        repository = new DBWorker();

        back_btn_deal.setOnAction(event -> {
            Model.getInstance().getViewFactory().showMenuWindow();
            Stage stage = (Stage) back_btn_deal.getScene().getWindow();
            Model.getInstance().getViewFactory().closeStage(stage);
        });
        add_btn_deal.setOnAction(event -> {
            Model.getInstance().getViewFactory().showAddDealWindow();
            Stage stage = (Stage) add_btn_deal.getScene().getWindow();
            Model.getInstance().getViewFactory().closeStage(stage);
        });
        /*edit_btn_deal.setOnAction(event -> {
            String[] selectedDeal = dealTable.getSelectionModel().getSelectedItem();
            if (selectedDeal != null) {
                try {
                    Model.getInstance().getViewFactory().showEditDealWindow(selectedDeal);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });*/

        dealSearch_btn.setOnAction(event -> searchDeals());
        dealSearch_fld.textProperty().addListener((observable, oldValue, newValue) -> searchDeals());
        setupDealTable();
    }

    private void setupDealTable() {
        developerName_col_deal.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[0]));
        developerEmail_col_deal.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[1]));
        developerPhone_col_deal.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[2]));
        orderName_col_deal.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[3]));
        orderDate_col_deal.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[4]));
        orderAmount_col_deal.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[5]));
        paymentStatus_col_deal.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[6]));
        clientName_col_deal.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[7]));
        clientEmail_col_deal.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[8]));
        clientPhone_col_deal.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[9]));

        try {
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadData() throws SQLException {
        //DBWorker.initDB();
        fullData = repository.getDealData();
        dealTable.setItems(fullData);
        dealTable.refresh();

        /*developerName_col_deal.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[0]));
        developerEmail_col_deal.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[1]));
        developerPhone_col_deal.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[2]));
        orderName_col_deal.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[3]));
        orderDate_col_deal.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[4]));
        orderAmount_col_deal.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[5]));
        paymentStatus_col_deal.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[6]));
        clientName_col_deal.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[7]));
        clientEmail_col_deal.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[8]));
        clientPhone_col_deal.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[9]));

         */
    }

    private void searchDeals() {
        String searchText = dealSearch_fld.getText().toLowerCase();
        ObservableList<String[]> filteredData;
        if (searchText.isEmpty()) {
            filteredData = fullData;
            dealResultSearch_lbl.setText("Введите текст для поиска"); // Сообщение для пустого текстового поля
        } else {
            filteredData = FXCollections.observableArrayList(
                    fullData.stream().filter(deal ->
                                    deal[0].toLowerCase().contains(searchText) ||
                                            deal[1].toLowerCase().contains(searchText) ||
                                            deal[2].toLowerCase().contains(searchText) ||
                                            deal[3].toLowerCase().contains(searchText) ||
                                            deal[4].toLowerCase().contains(searchText) ||
                                            deal[5].toLowerCase().contains(searchText) ||
                                            deal[6].toLowerCase().contains(searchText) ||
                                            deal[7].toLowerCase().contains(searchText) ||
                                            deal[8].toLowerCase().contains(searchText) ||
                                            deal[9].toLowerCase().contains(searchText))
                            .collect(Collectors.toList())
            );
            dealResultSearch_lbl.setText("Найдено записей: " + filteredData.size()); // Обновляем Label с количеством найденных строк
        }
        dealTable.setItems(filteredData);
    }
}
