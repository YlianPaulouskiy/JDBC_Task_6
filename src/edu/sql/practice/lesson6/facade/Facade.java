package edu.sql.practice.lesson6.facade;

import edu.sql.practice.lesson6.model.Order;
import edu.sql.practice.lesson6.model.Product;
import edu.sql.practice.lesson6.service.OrderService;
import edu.sql.practice.lesson6.service.ProductService;
import edu.sql.practice.lesson6.service.impl.OrderServiceImpl;
import edu.sql.practice.lesson6.service.impl.ProductServiceImpl;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Facade {

    private final OrderService orderService;
    private final ProductService productService;

    public Facade() {
        orderService = new OrderServiceImpl();
        productService = new ProductServiceImpl();
    }

    //------------------------------------------------------------------------------------------------------------------

    public void getInfoAboutOrder(Long id) {
        getInfo(orderService.findOne(id));
    }

    public void getOrdersByPriceAndAmount(Double orderPrice, Long productsAmount) {
        List<Order> orderList = orderService.findAll();
        System.out.println("Номера заказов с ценой меньше  " + orderPrice
                + "и количеством таваров = " + productsAmount);
        for (Order order : orderList) {
            Double price = 0.0;
            Long amount = 0L;
            for (Product product : order.getProducts().keySet()) {
                price += product.getPrice();
                amount += order.getProducts().get(product);
            }
            if (price <= orderPrice && productsAmount.equals(amount)) {
                System.out.print(order.getId() + " ");
            }
        }
    }

    public void foundOrdersByProduct(Product product) {
        List<Order> orderList = orderService.findAll();
        System.out.println("Номера заказов с продуктом: " + product.getName());
        for (Order order : orderList) {
            for (Product product1 : order.getProducts().keySet()) {
                if (product.equals(product1)) {
                    System.out.print(order.getId() + " ");
                    break;
                }
            }
        }
    }

    public void getOrderWithoutProductAndComingToday(Product product) {
        List<Order> orderList = orderService.findAll();
        System.out.println("Заказы без товара " + product.getName() + " и поступивших в течение дня");
        for (Order order : orderList) {
            boolean haveProduct = false;
            for (Product product1 : order.getProducts().keySet()) {
                    if (product1.equals(product)) {
                        haveProduct = true;
                    }
            }
            if (!haveProduct && order.getDate().toLocalDate().equals(LocalDate.now())) {
                System.out.print(order.getId() + " ");
            }
        }
    }

    public Long getOrderByProductInThisDate() {
        Order order = new Order();
        order.setDate(new Date(LocalDate.now().toEpochDay()));
        List<Order> orderList = orderService.findAll();
        Map<Product, Long> productMap = new HashMap<>();
        for (Order order1 : orderList) {
            if (order1.getDate().toLocalDate().equals(LocalDate.now())) {
                productMap.putAll(order1.getProducts());
            }
        }
        order.setProducts(productMap);
        orderService.save(order);
        return order.getId();
    }

    public void removeAllOrdersByProductAndAmount(Product product, Long amount) {
        List<Order> orderList = orderService.findAll();
        orderList.removeIf(order -> order.getProducts().get(product) < amount);
    }

    //------------------------------------------------------------------------------------------------------------------

    private void getInfo(Order order) {
        System.out.println("Заказ № " + order.getId());
        System.out.println("Товары в заказе: ");
        order.getProducts().forEach(
                ((product, amount) ->
                        System.out.print(product.getName() + " "
                                + product.getDescription() + " "
                                + product.getPrice() + "; количество: "
                                + amount))
        );
    }
}
