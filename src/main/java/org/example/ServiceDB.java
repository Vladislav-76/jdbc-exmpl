package org.example;

import org.postgresql.ds.PGSimpleDataSource;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Date;
import java.util.Properties;


public class ServiceDB {
    final private static String PROPERTIES_PATH = "src/main/resources/config.properties";

    private static Connection getConnection() throws SQLException {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        Properties properties = new Properties();
        Connection connection = null;
        try (FileInputStream fileStream = new FileInputStream(PROPERTIES_PATH)) {
            properties.load(fileStream);
            dataSource.setServerNames(new String[]{properties.getProperty("db.serverName")});
            dataSource.setPortNumbers(new int[]{Integer.parseInt(properties.getProperty("db.portNumber"))});
            dataSource.setDatabaseName(properties.getProperty("db.databaseName"));
            dataSource.setUser(properties.getProperty("db.user"));
            dataSource.setPassword(properties.getProperty("db.password"));
            connection = dataSource.getConnection();
        } catch (IOException error) {
            System.err.println("Отсутствует файл параметров подключения к БД");
        } catch (SQLException error) {
            System.out.println("Невозможно подключиться к БД");
            throw new SQLException(error);
        }
        return connection;
    }

    public User userSelect(String login) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection();
            statement = connection.createStatement();
            String query = String.format(
                    "SELECT password, email, date " +
                    "FROM users_credentials " +
                    "JOIN users_emails ON users_credentials.login = users_emails.login " +
                    "WHERE users_credentials.login = '%s'", login
            );
            resultSet = statement.executeQuery(query);
            resultSet.next();
            String password = resultSet.getString(1);
            String email = resultSet.getString(2);
            Date date = resultSet.getDate(3);
            return new User(login, password, email, date);
        } catch (SQLException error) {
            return null;
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }
    }

    public int userInsert(User user) {
        String query = (
                "INSERT INTO users_credentials (login, password, date) VALUES (?, ?, ?); " +
                "INSERT INTO users_emails (login, email) VALUES (?, ?);"
        );
        try (
                Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            preparedStatement.setString(4, user.getLogin());
            preparedStatement.setString(5, user.getEmail());
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
