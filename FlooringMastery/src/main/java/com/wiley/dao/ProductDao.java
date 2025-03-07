package com.wiley.dao;

import com.wiley.model.Product;

import java.util.List;

public interface ProductDao {

    /**
     * Returns the stored list of product objects in the DAO.
     * Returns empty list if there is nothing stored
     *
     * @return the List of Product objects
     */
    List<Product> getAllProducts() throws OrderDataPersistenceException;
    /**
     * Returns the product object associated with the given productType.
     * Returns null if no such product exists
     *
     * @param productType of the product to retrieve
     * @return the Product object associated with the given product type,
     * null if no such product exists
     */
    Product getByProductType(String productType) throws OrderDataPersistenceException;
    /**
     * Returns the stored list of productTypes in the DAO.
     * Returns empty list if there is nothing stored
     *
     * @return the List of product types from the stored map
     */
    List<String> getAllProductTypes() throws OrderDataPersistenceException;


}
