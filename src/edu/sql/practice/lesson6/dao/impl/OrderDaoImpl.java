package edu.sql.practice.lesson6.dao.impl;

import edu.sql.practice.lesson6.connection.ConnectionProvider;
import edu.sql.practice.lesson6.dao.OrderDao;
import edu.sql.practice.lesson6.dao.ProductDao;
import edu.sql.practice.lesson6.exception.product.ProductNotFoundException;
import edu.sql.practice.lesson6.model.Order;
import edu.sql.practice.lesson6.model.Product;

import java.sql.*;
import java.util.*;

public class OrderDaoImpl implements OrderDao {

    private final Optional<Connection> connection;
    private final ProductDao productDao;

    public OrderDaoImpl() {
        connection = new ConnectionProvider().getConnection();
        productDao = new ProductDaoImpl();
    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public Optional<Order> findOne(Long id) {
        if (connection.isPresent()) {
            String query = "SELECT products_id, date, order_id FROM `order` " +
                    "INNER JOIN order_products AS op on `order`.id = op.order_id " +
                    "WHERE order_id = ?";
            try (PreparedStatement preparedStatement = connection.get().prepareStatement(query)) {
                preparedStatement.setLong(1, id);
                return Optional.of(getResults(preparedStatement.executeQuery()).get(0));
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Order> findAll() {
        if (connection.isPresent()) {
            String query = "SELECT products_id, date, order_id FROM `order` " +
                    "INNER JOIN order_products AS op on `order`.id = op.order_id";
            try (Statement statement = connection.get().createStatement()) {
                return getResults(statement.executeQuery(query));
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
        return Collections.emptyList();
    }

    @Override
    public Optional<Order> create(Order source) {
        if (connection.isPresent()) {
            String query = "INSERT INTO `order`(date) VALUES (?)";
            try (PreparedStatement preparedStatement = connection.get().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setDate(1, source.getDate());
                preparedStatement.executeUpdate();
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    Long orderId = resultSet.getLong(1);
                    //добавление записей в таблицу order_products
                    createProductsInOrders(orderId, source);
                    return findOne(orderId);
                }
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Order> update(Order source) {
        if (connection.isPresent()) {
            String query = "UPDATE `order` SET date = ? WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.get().prepareStatement(query)) {
                preparedStatement.setDate(1, source.getDate());
                preparedStatement.setLong(2, source.getId());
                //update таблицы order
                preparedStatement.executeUpdate();
                //update таблицы order_products
                updateProductsInOrders(source);
                return findOne(source.getId());
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
        return Optional.empty();
    }

    @Override
    public void remove(Long id) {
        if (connection.isPresent()) {
            String query = "DELETE FROM `order` WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.get().prepareStatement(query)) {
                preparedStatement.setLong(1, id);
                preparedStatement.executeUpdate();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
    }

    //------------------------------------------------------------------------------------------------------------------

    private List<Order> getResults(ResultSet resultSet) {
        try {
            List<Order> orderList = new ArrayList<>();
            while (resultSet.next()) {
                Order order = new Order();
                order.setId(resultSet.getLong("order_id"));
                order.setDate(resultSet.getDate("date"));
                order.setProducts(getProductMap(order.getId()));// <--
                orderList.add(order);
            }
            //удаляем повторяющиеся заказы
            Set<Order> orderSet = new HashSet<>(orderList);
            return new ArrayList<>(orderSet);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * Для получения мапы продуктов
     *
     * @param orderId id заказа
     * @return мапу продуктов и их количества
     */
    private Map<Product, Long> getProductMap(Long orderId) {
        if (connection.isPresent()) {
            Map<Product, Long> productMap = new HashMap<>();
            String query = "SELECT products_id FROM order_products WHERE order_id = ?";
            try (PreparedStatement preparedStatement = connection.get().prepareStatement(query)) {
                preparedStatement.setLong(1, orderId);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Product product = productDao.findOne(resultSet.getLong("products_id"))
                            .orElseThrow(ProductNotFoundException::new);
                    productMap.put(product, getAmount(orderId, product.getId()));
                }
                return productMap;
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
        return Collections.emptyMap();
    }

    /**
     * Считает количество  одного продукта в заказе
     *
     * @param productId id продукта, количество которого необходимо посчитать
     * @return Количество определенноко продукта
     */
    private Long getAmount(Long orderId, Long productId) {
        if (connection.isPresent()) {
            String query = "SELECT COUNT(*) FROM order_products WHERE products_id = ? AND order_id = ?";
            try (PreparedStatement preparedStatement = connection.get().prepareStatement(query)) {
                preparedStatement.setLong(1, productId);
                preparedStatement.setLong(2, orderId);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getLong(1);
                }
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
        return 0L;
    }

    /**
     * Добаляем значения в таблицу order_products до тех пор пока не переберем
     * все продукты в заказе
     *
     * @param orderId номер заказа(приходит из вне, т.к. он только сгенерирован)
     * @param order   заказ
     */
    private void createProductsInOrders(Long orderId, Order order) {
        for (Product product : order.getProducts().keySet()) {
            Long amount = order.getProducts().get(product);
            while (amount > 0) {
                addProductsInOrders(orderId, product.getId());
                amount--;
            }
        }
    }

    /**
     * Добавляет одну запись в таблицу order_products
     *
     * @param orderId   id заказа
     * @param productId id продукта
     */
    private void addProductsInOrders(Long orderId, Long productId) {
        if (connection.isPresent()) {
            String query = "INSERT INTO order_products(order_id, products_id) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.get().prepareStatement(query)) {
                preparedStatement.setLong(1, orderId);
                preparedStatement.setLong(2, productId);
                preparedStatement.executeUpdate();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
    }

    /**
     * Обновляет все записи в таблице order_products
     *
     * @param order заказ, который обновляем
     */
    private void updateProductsInOrders(Order order) {
        for (Product product : order.getProducts().keySet()) {
            Long amount = order.getProducts().get(product);
            while (amount > 0) {
                updateRowProductsInOrders(order.getId(), product.getId());
                amount--;
            }
        }
    }

    /**
     * обновляем одну строку в таблице order_products
     *
     * @param orderId   id заказа
     * @param productId id продукта
     */
    private void updateRowProductsInOrders(Long orderId, Long productId) {
        if (connection.isPresent()) {
            String query = "UPDATE order_products SET products_id = ? WHERE order_id = ?";
            try (PreparedStatement preparedStatement = connection.get().prepareStatement(query)) {
                preparedStatement.setLong(1, productId);
                preparedStatement.setLong(2, orderId);
                preparedStatement.executeUpdate();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
    }

}
