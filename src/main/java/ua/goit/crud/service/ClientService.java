package ua.goit.crud.service;

import ua.goit.crud.Database;
import ua.goit.crud.model.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientService {
    private static final int NAME_MIN_LENGTH = 2;
    private static final int NAME_MAX_LENGTH = 1000;
    private Connection connection;

    public ClientService() {
        connection = Database.getInstance().getConnection();
    }

    public long create(String name) throws Exception {
        if (name == null || name.length() < NAME_MIN_LENGTH || name.length() > NAME_MAX_LENGTH) {
            throw new Exception("Invalid client name");
        }
        String query = "INSERT INTO client (name) VALUES (?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, name);
            statement.executeUpdate();
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    return resultSet.getLong(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to create a client");
            e.printStackTrace();
        }
        return -1;
    }

    public String getById(long id) {
        String query = "SELECT name FROM client WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("name");
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to get client by id");
            e.printStackTrace();
        }
        return null;
    }

    public void setName(long id, String name) throws Exception {
        if (name == null || name.length() < NAME_MIN_LENGTH || name.length() > NAME_MAX_LENGTH) {
            throw new Exception("Invalid client name");
        }
        String query = "UPDATE client SET name = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            statement.setLong(2, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Failed to set client name");
            e.printStackTrace();
        }
    }

    public void deleteById(long id) {
        String query = "DELETE FROM client WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException
                e) {
            e.printStackTrace();
        }
    }

    public List<Client> listAll() {
        List<Client> clients = new ArrayList<>();
        String query = "SELECT id, name FROM client";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                clients.add(new Client((int) id, name));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }
}