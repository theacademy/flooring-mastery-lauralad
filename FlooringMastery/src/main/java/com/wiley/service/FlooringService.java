package com.wiley.service;

import com.wiley.dao.OrderDataPersistenceException;
import com.wiley.model.Order;
import com.wiley.model.Product;
import com.wiley.model.Tax;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * FlooringService defines the business logic for managing flooring orders,
 * handling calculations, validations, and interacting with data persistence layers.
 */
public interface FlooringService {

    /**
     * This method is to be called once when the service is created
     * In the case of already existing order files in the folder,
     * this method will iterate through all of them and retrieve the
     * maximum orderNumber present and assign it to the class attribute
     *
     */
    void init();
    /**
     * Retrieves the current maximum order ID in the system.
     *
     * @return the highest order ID
     */
    int getMaxId();
    /**
     * Calculates the material cost for an order.
     *
     * @param order the order to calculate material costs for
     * @return the total material cost as a BigDecimal
     */
    BigDecimal calculateMaterialCost(Order order);
    /**
     * Calculates the labor cost for an order.
     *
     * @param order the order to calculate labor costs for
     * @return the total labor cost as a BigDecimal
     */
    BigDecimal calculateLaborCost(Order order);
    /**
     * Calculates the tax amount for an order.
     *
     * @param order the order to calculate taxes for
     * @return the tax amount as a BigDecimal
     */
    BigDecimal calculateTax(Order order);
    /**
     * Calculates the total cost for an order, including material, labor, and taxes.
     *
     * @param order the order to calculate the total cost for
     * @return the total cost as a BigDecimal
     */
    BigDecimal calculateTotal(Order order);

    //ORDER DAO
    /**
     * Creates a new order and assigns it a unique order number.
     *
     * @return the newly created order object
     */
    Order createOrderAndAssignOrderNumber();
    /**
     * Retrieves all orders for a specific date.
     *
     * @param date the date to retrieve orders for
     * @return a list of orders for the specified date
     * @throws OrderDataPersistenceException if there is an error retrieving the data
     */
    List<Order> getAllOrdersForDate(LocalDate date) throws OrderDataPersistenceException;
    /**
     * Adds a new order for a given date.
     *
     * @param order the order to add
     * @param date the date associated with the order
     * @return the previous order object at that date with order number, null if none
     * @throws OrderDataPersistenceException if there is an error during data persistence
     */
    Order addOrder(Order order, LocalDate date) throws OrderDataPersistenceException;
    /**
     * Edits an existing order.
     *
     * @param date the date associated with the order
     * @param order the updated order object
     * @return the previous order object that got updated
     * @throws OrderDataPersistenceException if the order cannot be found or updated
     */
    Order editOrder(LocalDate date, Order order) throws OrderDataPersistenceException;
    /**
     * Removes an order by its order number and date.
     *
     * @param orderNumber the number of the order to remove
     * @param date the date associated with the order
     * @return the removed order object
     * @throws OrderDataPersistenceException if the order cannot be found or removed
     */
    Order removeOrder(int orderNumber, LocalDate date) throws OrderDataPersistenceException;
    /**
     * Retrieves a specific order by order number and date.
     *
     * @param orderNumber the number of the order to retrieve
     * @param date the date associated with the order
     * @return the retrieved order object
     * @throws OrderDataPersistenceException if the order is not found
     */
    Order getOrder(int orderNumber, LocalDate date) throws OrderDataPersistenceException;
    /**
     * Exports all order data across all dates into a single file or external system.
     * Useful for backups or generating comprehensive reports.
     */
    void exportAllData() throws OrderDataPersistenceException;

    //VALIDATION
    /**
     * Validates the order date.
     *
     * @param date the date to validate
     * @return true if the date is valid
     * @throws OrderInformationInvalidException if the date is invalid
     */
    boolean validateDateInformation(LocalDate date) throws OrderInformationInvalidException;
    /**
     * Validates the customer name.
     *
     * @param name the customer name to validate
     * @return true if the name is valid
     * @throws OrderInformationInvalidException if the name is invalid
     */
    boolean validateCustomerNameInformation(String name) throws OrderInformationInvalidException;
    /**
     * Validates the state information.
     *
     * @param state the state abbreviation to validate
     * @return true if the state is valid
     * @throws OrderDataPersistenceException if there is an error retrieving state data
     * @throws TaxInformationInvalidException if the state information is invalid
     */
    boolean validateStateInformation(String state) throws
            OrderDataPersistenceException,
            TaxInformationInvalidException;
    /**
     * Validates the area information for an order.
     *
     * @param areaDecimal the area value to validate
     * @return true if the area is valid
     * @throws OrderInformationInvalidException if the area is invalid
     */
    boolean validateAreaInformation(BigDecimal areaDecimal) throws OrderInformationInvalidException;


    //TAX DAO
    /**
     * Retrieves tax information by state abbreviation.
     *
     * @param taxAbbreviation the state abbreviation to retrieve tax data for
     * @return the tax object containing tax rates and details
     * @throws OrderDataPersistenceException if tax data cannot be retrieved
     */
    Tax geByStateAbbreviation(String taxAbbreviation) throws OrderDataPersistenceException;
    /**
     * Retrieves all valid state abbreviations for taxation.
     *
     * @return a set of state abbreviations
     * @throws OrderDataPersistenceException if there is an error retrieving state data
     */
    Set<String> getAllStates() throws OrderDataPersistenceException;

    // PRODUCT DAO
    /**
     * Retrieves a list of all available products.
     *
     * @return a list of product objects
     * @throws OrderDataPersistenceException if there is an error retrieving product data
     */
    List<Product> getAllProducts() throws OrderDataPersistenceException;
    /**
     * Retrieves product details by product type.
     *
     * @param productType the type of product to retrieve
     * @return the product object
     * @throws OrderDataPersistenceException if the product is not found
     */
    Product getByProductType(String productType) throws OrderDataPersistenceException;
    /**
     * Retrieves a list of all available product types.
     *
     * @return a list of product type names
     * @throws OrderDataPersistenceException if there is an error retrieving product data
     */
    List<String> getAllProductTypes() throws OrderDataPersistenceException;


}
