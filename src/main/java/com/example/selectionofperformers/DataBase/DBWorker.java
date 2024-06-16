package com.example.selectionofperformers.DataBase;

import com.example.selectionofperformers.Entity.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class DBWorker implements Repository {
    private static String jdbcUrl = "jdbc:sqlite:C:\\Users\\Семья.DESKTOP-G1U9L7F\\.m2\\repository\\org\\xerial\\sqlite-jdbc\\3.36.0.3\\dev.db";
    private static Connection connection;

    public static void initDB() {
        try {
            connection = DriverManager.getConnection(jdbcUrl);
            createTable();
            System.out.println("Подключение прошло успешно");
        } catch (SQLException e) {
            System.out.println("Ошибка подключения БД");
            e.printStackTrace();
        }
    }

    public static void createTable() {
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS user (idUser INTEGER PRIMARY KEY AUTOINCREMENT, login TEXT, password TEXT);");
            statement.execute("CREATE TABLE IF NOT EXISTS skills (idSkill INTEGER PRIMARY KEY, skillName TEXT);");
            statement.execute("CREATE TABLE IF NOT EXISTS client (idClient INTEGER PRIMARY KEY, clientName TEXT, clientEmail TEXT, clientPhone INTEGER);");
            statement.execute("CREATE TABLE IF NOT EXISTS developer (idDeveloper INTEGER PRIMARY KEY, developerName TEXT, developerEmail TEXT, developerPhone INTEGER, developerRating REAL, developerStatus TEXT);");
            statement.execute("CREATE TABLE IF NOT EXISTS deal (idDeal INTEGER PRIMARY KEY, idProject INTEGER, dealName TEXT, dealDate TEXT, dealAmount REAL, paymentStatus TEXT, FOREIGN KEY (idProject) REFERENCES project(idProject));");
            statement.execute("CREATE TABLE IF NOT EXISTS project (idProject INTEGER PRIMARY KEY, projectName TEXT, projectStatus TEXT, startDate TEXT, endDate TEXT, idDeveloper INTEGER, idClient INTEGER, FOREIGN KEY (idDeveloper) REFERENCES developer(idDeveloper), FOREIGN KEY (idClient) REFERENCES client(idClient));");
            statement.execute("CREATE TABLE IF NOT EXISTS review (idReview INTEGER PRIMARY KEY, reviewRating REAL, reviewComment TEXT, idDeal INTEGER, idClient INTEGER, FOREIGN KEY (idDeal) REFERENCES deal(idDeal), FOREIGN KEY (idClient) REFERENCES client(idClient));");
            statement.execute("CREATE TABLE IF NOT EXISTS developer_skills (idDeveloper INTEGER, idSkill INTEGER, PRIMARY KEY (idDeveloper, idSkill), FOREIGN KEY (idDeveloper) REFERENCES developer(idDeveloper), FOREIGN KEY (idSkill) REFERENCES skills(idSkill));");
            statement.execute("CREATE TABLE IF NOT EXISTS developer_has_project (idDeveloper INTEGER, idProject INTEGER, PRIMARY KEY (idDeveloper, idProject), FOREIGN KEY (idDeveloper) REFERENCES developer(idDeveloper), FOREIGN KEY (idProject) REFERENCES project(idProject));");

            // Вставка данных
            statement.execute("INSERT INTO user (idUser, login, password) VALUES (1, 'papug', '123')");
            statement.execute("INSERT INTO skills (idSkill, skillName) VALUES (1, 'Работа с базами данных(MySQL)')");
            statement.execute("INSERT INTO skills (idSkill, skillName) VALUES (2, 'Работа с PHP')");
            statement.execute("INSERT INTO client (idClient, clientName, clientEmail, clientPhone) VALUES (1, 'Морозова Ирина Анатольевна', 'irishka1987@mail.ru', +7(908)6753784)");
            statement.execute("INSERT INTO developer (idDeveloper, developerName, developerEmail, developerPhone, developerRating, developerStatus) VALUES (1, 'Яблочкин Алексей Андреевич', 'yablochkin2000@yandex.ru', +7(902)3478594, 4.9, 'В отпуске')");
            statement.execute("INSERT INTO project (idProject, projectName, projectStatus, startDate, endDate, idDeveloper, idClient) VALUES (1, 'Интернет-магазин на Tilda для BakeryHouse', 'Закончен', '13.03.2024', '02.06.2024', 1, 1)");
            statement.execute("INSERT INTO deal (idDeal, idProject, dealName, dealDate, dealAmount, paymentStatus) VALUES (1, 1, 'Веб-сайт на Tilda для пекарни', '01.03.2024', 50000, 'Оплачено')");
            statement.execute("INSERT INTO review (idReview, reviewRating, reviewComment, idDeal, idClient) VALUES (1, 4.9, 'Всё отлично! Алексей мастер своего дела!', 1, 1)");
            statement.execute("INSERT INTO developer_skills (idDeveloper, idSkill) VALUES (1, 1)");
            statement.execute("INSERT INTO developer_has_project (idDeveloper, idProject) VALUES (1, 1)");

            System.out.println("Данные получены успешно");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    private static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ResultSet getUser(User user) {
        ResultSet resultSet = null;
        String select = "SELECT * FROM user WHERE login =? AND password =? ";
        try {
            connection = DriverManager.getConnection(jdbcUrl);
            PreparedStatement prSt = connection.prepareStatement(select);
            prSt.setString(1, user.getLogin());
            prSt.setString(2, user.getPassword());

            resultSet = prSt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
        return resultSet;
    }

    @Override
    public ObservableList<String[]> getDeveloperData() throws SQLException {
        ObservableList<String[]> data = FXCollections.observableArrayList();
        String query = "SELECT d.developerName, d.developerEmail, d.developerPhone, d.developerRating, d.developerStatus, s.skillName " +
                "FROM developer d " +
                "JOIN developer_skills ds ON d.idDeveloper = ds.idDeveloper " +
                "JOIN skills s ON ds.idSkill = s.idSkill";

        connection = DriverManager.getConnection(jdbcUrl);
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {

            while (rs.next()) {
                String[] row = new String[6];
                row[0] = rs.getString("developerName");
                row[1] = rs.getString("developerEmail");
                row[2] = rs.getString("developerPhone");
                row[3] = String.valueOf(rs.getDouble("developerRating"));
                row[4] = rs.getString("developerStatus");
                row[5] = rs.getString("skillName");
                data.add(row);
            }
        }
        return data;
    }

    public void addDeveloper(String developerName, String developerEmail, String developerPhone, double developerRating, String developerStatus, String skills) throws SQLException {
        try (PreparedStatement developerStmt = connection.prepareStatement(
                "INSERT INTO developer (developerName, developerEmail, developerPhone, developerRating, developerStatus) VALUES (?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
            developerStmt.setString(1, developerName);
            developerStmt.setString(2, developerEmail);
            developerStmt.setString(3, developerPhone);
            developerStmt.setDouble(4, developerRating);
            developerStmt.setString(5, developerStatus);

            int affectedRows = developerStmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Failed to add developer, no rows affected.");
            }

            try (ResultSet generatedKeys = developerStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long developerId = generatedKeys.getLong(1);

                    // Add skills for the developer
                    String[] skillArray = skills.split(",");
                    for (String skill : skillArray) {
                        int skillId = getOrCreateSkillId(skill.trim());
                        addDeveloperSkill(developerId, skillId);
                    }
                } else {
                    throw new SQLException("Failed to add developer, no ID obtained.");
                }
            }
        }
    }

    private int getOrCreateSkillId(String skillName) throws SQLException {
        int skillId = -1;
        try (PreparedStatement checkSkillStmt = connection.prepareStatement("SELECT idSkill FROM skills WHERE skillName = ?")) {
            checkSkillStmt.setString(1, skillName);
            try (ResultSet resultSet = checkSkillStmt.executeQuery()) {
                if (resultSet.next()) {
                    skillId = resultSet.getInt("idSkill");
                }
            }
        }

        if (skillId == -1) {
            try (PreparedStatement insertSkillStmt = connection.prepareStatement("INSERT INTO skills (skillName) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {
                insertSkillStmt.setString(1, skillName);
                insertSkillStmt.executeUpdate();

                try (ResultSet generatedKeys = insertSkillStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        skillId = generatedKeys.getInt(1);
                    }
                }
            }
        }

        return skillId;
    }

    private void addDeveloperSkill(long developerId, int skillId) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO developer_skills (idDeveloper, idSkill) VALUES (?, ?)")) {
            stmt.setLong(1, developerId);
            stmt.setInt(2, skillId);
            stmt.executeUpdate();
        }
    }

    @Override
    public ObservableList<String[]> getDealData() throws SQLException {
        ObservableList<String[]> data = FXCollections.observableArrayList();
        String query = "SELECT d.developerName, d.developerEmail, d.developerPhone, de.dealName, de.dealDate, de.dealAmount, de.paymentStatus, c.clientName, c.clientEmail, c.clientPhone " +
                "FROM developer d " +
                "JOIN project p ON d.idDeveloper = p.idDeveloper " +
                "JOIN deal de ON p.idProject = de.idProject " +
                "JOIN client c ON p.idClient = c.idClient";

        connection = DriverManager.getConnection(jdbcUrl);
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {

            while (rs.next()) {
                String[] row = new String[10];
                row[0] = rs.getString("developerName");
                row[1] = rs.getString("developerEmail");
                row[2] = rs.getString("developerPhone");
                row[3] = rs.getString("dealName");
                row[4] = rs.getString("dealDate");
                row[5] = String.valueOf(rs.getDouble("dealAmount"));
                row[6] = rs.getString("paymentStatus");
                row[7] = rs.getString("clientName");
                row[8] = rs.getString("clientEmail");
                row[9] = rs.getString("clientPhone");
                data.add(row);
            }
        } finally {
            closeConnection();
        }
        return data;
    }

    public void addDeal(String developerName, String developerEmail, String developerPhone, String dealName, String dealDate, double dealAmount, String paymentStatus, String clientName, String clientEmail, String clientPhone) throws SQLException {
        connection = DriverManager.getConnection(jdbcUrl);
        try {
            connection.setAutoCommit(false);

            // Добавление клиента, если его не существует
            int clientId = getClientId(clientName, clientEmail, clientPhone);
            if (clientId == -1) {
                try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO client (clientName, clientEmail, clientPhone) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setString(1, clientName);
                    stmt.setString(2, clientEmail);
                    stmt.setString(3, clientPhone);
                    stmt.executeUpdate();
                    ResultSet generatedKeys = stmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        clientId = generatedKeys.getInt(1);
                    }
                }
            }

            // Добавление исполнителя если его не существует
            int developerId = getDeveloperId(developerName, developerEmail, developerPhone);
            if (developerId == -1) {
                try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO developer (developerName, developerEmail, developerPhone) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setString(1, developerName);
                    stmt.setString(2, developerEmail);
                    stmt.setString(3, developerPhone);
                    stmt.executeUpdate();
                    ResultSet generatedKeys = stmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        developerId = generatedKeys.getInt(1);
                    }
                }
            }

            // Добавление проекта
            int projectId = getProjectId(developerId, clientId);
            if (projectId == -1) {
                try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO project (idDeveloper, idClient) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setInt(1, developerId);
                    stmt.setInt(2, clientId);
                    stmt.executeUpdate();
                    ResultSet generatedKeys = stmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        projectId = generatedKeys.getInt(1);
                    }
                }
            }

            // Добавление сделки
            try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO deal (idProject, dealName, dealDate, dealAmount, paymentStatus) VALUES (?, ?, ?, ?, ?)")) {
                stmt.setInt(1, projectId);
                stmt.setString(2, dealName);
                stmt.setString(3, dealDate);
                stmt.setDouble(4, dealAmount);
                stmt.setString(5, paymentStatus);
                stmt.executeUpdate();
            }

            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            closeConnection();
        }
    }


    private int getClientId(String clientName, String clientEmail, String clientPhone) throws SQLException {
        String query = "SELECT idClient FROM client WHERE clientName = ? AND clientEmail = ? AND clientPhone = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, clientName);
            stmt.setString(2, clientEmail);
            stmt.setString(3, clientPhone);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("idClient");
                }
            }
        }
        return -1;
    }

    public int getDeveloperId(String developerName, String developerEmail, String developerPhone) throws SQLException {
        String query = "SELECT idDeveloper FROM developer WHERE developerName = ? AND developerEmail = ? AND developerPhone = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, developerName);
            stmt.setString(2, developerEmail);
            stmt.setString(3, developerPhone);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("idDeveloper");
                }
            }
        }
        return -1;
    }

    private int getProjectId(int developerId, int clientId) throws SQLException {
        String query = "SELECT idProject FROM project WHERE idDeveloper = ? AND idClient = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, developerId);
            stmt.setInt(2, clientId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("idProject");
                }
            }
        }
        return -1;
    }
    @Override
    public ObservableList<String[]> getProjectData() throws SQLException {
        ObservableList<String[]> data = FXCollections.observableArrayList();
        String query = "SELECT d.developerName, d.developerEmail, d.developerPhone, d.developerRating, p.projectName, p.projectStatus, p.startDate, p.endDate, r.reviewRating, r.reviewComment " +
                "FROM developer d " +
                "JOIN project p ON d.idDeveloper = p.idDeveloper " +
                "JOIN review r ON p.idProject = r.idDeal";

        connection = DriverManager.getConnection(jdbcUrl);
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {

            while (rs.next()) {
                String[] row = new String[10];
                row[0] = rs.getString("developerName");
                row[1] = rs.getString("developerEmail");
                row[2] = rs.getString("developerPhone");
                row[3] = String.valueOf(rs.getDouble("developerRating"));
                row[4] = rs.getString("projectName");
                row[5] = rs.getString("projectStatus");
                row[6] = rs.getString("startDate");
                row[7] = rs.getString("endDate");
                row[8] = String.valueOf(rs.getDouble("reviewRating"));
                row[9] = rs.getString("reviewComment");
                data.add(row);
            }
        } finally {
            closeConnection();
        }
        return data;
    }


    public void addProject(String developerName, String developerEmail, String developerPhone, double developerRating, String projectName, String projectStatus, String startDate, String endDate, double commentRating, String commentText) throws SQLException {
        connection = DriverManager.getConnection(jdbcUrl);
        try {
            connection.setAutoCommit(false);

            int developerId = getDeveloperId(developerName, developerEmail, developerPhone);
            if (developerId == -1) {
                try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO developer (developerName, developerEmail, developerPhone, developerRating) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setString(1, developerName);
                    stmt.setString(2, developerEmail);
                    stmt.setString(3, developerPhone);
                    stmt.setDouble(4, developerRating);
                    stmt.executeUpdate();
                    ResultSet generatedKeys = stmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        developerId = generatedKeys.getInt(1);
                    }
                }
            }

            int projectId = getProjectId(developerId, -1);
            if (projectId == -1) {
                try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO project (projectName, projectStatus, startDate, endDate, idDeveloper) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setString(1, projectName);
                    stmt.setString(2, projectStatus);
                    stmt.setString(3, startDate);
                    stmt.setString(4, endDate);
                    stmt.setInt(5, developerId);
                    stmt.executeUpdate();
                    ResultSet generatedKeys = stmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        projectId = generatedKeys.getInt(1);
                    }
                }
            }

            try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO review (reviewRating, reviewComment, idDeal, idClient) VALUES (?, ?, ?, ?)")) {
                stmt.setDouble(1, commentRating);
                stmt.setString(2, commentText);
                stmt.setInt(3, projectId);
                stmt.setNull(4, java.sql.Types.INTEGER);
                stmt.executeUpdate();
            }

            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            closeConnection();
        }
    }

}
