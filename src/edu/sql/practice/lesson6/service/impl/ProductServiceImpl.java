package edu.sql.practice.lesson6.service.impl;

import edu.sql.practice.lesson6.dao.ProductDao;
import edu.sql.practice.lesson6.dao.impl.ProductDaoImpl;
import edu.sql.practice.lesson6.exception.product.ProductNotCreatedException;
import edu.sql.practice.lesson6.exception.product.ProductNotFoundException;
import edu.sql.practice.lesson6.exception.product.ProductNotUpdatedException;
import edu.sql.practice.lesson6.model.Product;
import edu.sql.practice.lesson6.service.ProductService;

import java.util.List;

public class ProductServiceImpl implements ProductService {

    private final ProductDao productDao;

    public ProductServiceImpl() {
        this.productDao = new ProductDaoImpl();
    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public Product findOne(Long id) {
        return productDao.findOne(id).orElseThrow(ProductNotFoundException::new);
    }

    @Override
    public List<Product> findAll() {
        return productDao.findAll();
    }

    @Override
    public Product save(Product source) {
        return source.getId() == null ?
                productDao.create(source).orElseThrow(ProductNotCreatedException::new) :
                productDao.update(source).orElseThrow(ProductNotUpdatedException::new);
    }

    @Override
    public void remove(Long id) {
        productDao.remove(id);
    }
}
