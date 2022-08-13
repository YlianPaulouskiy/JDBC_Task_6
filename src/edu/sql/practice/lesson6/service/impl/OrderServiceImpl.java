package edu.sql.practice.lesson6.service.impl;

import edu.sql.practice.lesson6.dao.OrderDao;
import edu.sql.practice.lesson6.dao.impl.OrderDaoImpl;
import edu.sql.practice.lesson6.exception.order.OrderNotCreatedException;
import edu.sql.practice.lesson6.exception.order.OrderNotFoundException;
import edu.sql.practice.lesson6.exception.order.OrderNotUpdatedException;
import edu.sql.practice.lesson6.model.Order;
import edu.sql.practice.lesson6.service.OrderService;

import java.util.List;

public class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;

    public OrderServiceImpl() {
        this.orderDao = new OrderDaoImpl();
    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public Order findOne(Long id) {
        return orderDao.findOne(id).orElseThrow(OrderNotFoundException::new);
    }

    @Override
    public List<Order> findAll() {
        return orderDao.findAll();
    }

    @Override
    public Order save(Order source) {
        return source.getId() == null ?
                orderDao.create(source).orElseThrow(OrderNotCreatedException::new) :
                orderDao.update(source).orElseThrow(OrderNotUpdatedException::new);
    }

    @Override
    public void remove(Long id) {
        orderDao.remove(id);
    }
}
