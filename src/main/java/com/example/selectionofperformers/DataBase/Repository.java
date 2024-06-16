package com.example.selectionofperformers.DataBase;

import com.example.selectionofperformers.Entity.User;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface Repository {
    ResultSet getUser(User user);
    ObservableList<String[]> getProjectData()  throws SQLException;
    void addProject(String developerName, String developerEmail, String developerPhone, double developerRating, String projectName, String projectStatus, String startDate, String endDate, double commentRating, String commentText) throws SQLException;
    ObservableList<String[]> getDealData() throws SQLException;
    void addDeveloper(String developerName, String developerEmail, String developerPhone, double developerRating, String developerStatus, String skills) throws SQLException;
    void addDeal(String developerName, String developerEmail, String developerPhone, String dealName, String dealDate, double dealAmount, String paymentStatus, String clientName, String clientEmail, String clientPhone) throws SQLException;
    ObservableList<String[]> getDeveloperData() throws SQLException;
}
