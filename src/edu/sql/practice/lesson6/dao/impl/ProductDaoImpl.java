package edu.sql.practice.lesson6.dao.impl;

import edu.sql.practice.lesson6.connection.ConnectionProvider;
import edu.sql.practice.lesson6.dao.ProductDao;
import edu.sql.practice.lesson6.exception.connection.ConnectionException;
import edu.sql.practice.lesson6.model.Product;

import java.sql.*;
import java.util.*;

public class ProductDaoImpl implements ProductDao {

    private Connection connection;

    public ProductDaoImpl() {
        connection = new ConnectionProvider().getConnection()
                .orElseThrow(ConnectionException::new);
    }

    @Override
    public Optional<Product> findOne(Long id) {
        String query = "SELECT id, name, description, price FROM products where id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            return Optional.of(getResults(preparedStatement.executeQuery()).get(0));
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Product> findAll() {
        String query = "SELECT id, name, description, price FROM products";
        try (Statement statement = connection.createStatement()) {
            return getResults(statement.executeQuery(query));
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return Collections.emptyList();
    }

    @Override
    public Optional<Product> create(Product source) {
        String query = "INSERT INTO products(name, description, price) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, source.getName());
            preparedStatement.setString(2, source.getDescription());
            preparedStatement.setDouble(3, source.getPrice());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                return findOne(resultSet.getLong(1));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Product> update(Product source) {
        String query = "UPDATE products SET name = ?, description = ?, price = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, source.getName());
            preparedStatement.setString(2, source.getDescription());
            preparedStatement.setDouble(3, source.getPrice());
            preparedStatement.setLong(4,source.getId());
            preparedStatement.executeUpdate();
            return findOne(source.getId());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void remove(Long id) {
        String query = "DELETE FROM products WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    //------------------------------------------------------------------------------------------------------------------

    private List<Product> getResults(ResultSet resultSet) {
        try {
            List<Product> productList = new ArrayList<>();
            while (resultSet.next()) {
                Product product = new Product();
                product.setId(resultSet.getLong("id"));
                product.setName(resultSet.getString("name"));
                product.setDescription(resultSet.getString("description"));
                product.setPrice(resultSet.getDouble("price"));
                productList.add(product);
            }
            return productList;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return Collections.emptyList();
    }


}
