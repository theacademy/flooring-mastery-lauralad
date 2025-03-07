package com.wiley.dao;

import com.wiley.model.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoStubImpl implements ProductDao{
    public Product onlyProduct;

    public ProductDaoStubImpl() {
        onlyProduct = new Product();
        onlyProduct.setProductType("Wood");
        onlyProduct.setCostPerSquareFoot(new BigDecimal("5.15"));
        onlyProduct.setLaborCostPerSquareFoot(new BigDecimal("4.75"));
    }
    public ProductDaoStubImpl(Product onlyProduct) {
        this.onlyProduct = onlyProduct;
    }

    //this method simply returns a List containing the one and only product.
    @Override
    public List<Product> getAllProducts() throws OrderDataPersistenceException {
        List<Product> products = new ArrayList<>();
        products.add(onlyProduct);
        return products;
    }


    @Override
    public Product getByProductType(String productType) throws OrderDataPersistenceException {
        if (productType.equals(onlyProduct.getProductType())){
            return onlyProduct;
        } else {
            return null;
        }
    }

    @Override
    public List<String> getAllProductTypes() throws OrderDataPersistenceException {
        List<String> productTypes = new ArrayList<>();
        productTypes.add(onlyProduct.getProductType());
        return productTypes;
    }
}
