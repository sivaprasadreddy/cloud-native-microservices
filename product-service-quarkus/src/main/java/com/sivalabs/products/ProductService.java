package com.sivalabs.products;

import java.util.List;
import javax.inject.Singleton;
import javax.transaction.Transactional;

@Singleton
@Transactional
public class ProductService {

    public List<Product> getAllProducts() {
        return Product.listAll();
    }

    public Product getProductById(Long id) {
        return Product.findById(id);
    }

    public Product createTodo(Product product) {
        product.setId(null);
        Product.persist(product);
        return product;
    }
}
