package com.wiley.dao;

import com.wiley.model.Order;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

public interface OrderDao {
    /**
     * Adds a new order to the system and associates it with the given date.
     *
     * @param order the order object to add
     * @param date the date associated with the order
     * @return the previous object in the map, null if none
     * @throws OrderDataPersistenceException if there is an error during persistence
     */
    Order addOrder(Order order, LocalDate date) throws OrderDataPersistenceException;
    /**
     * Retrieves all orders for a given date.
     *
     * @param date the date for which to retrieve orders
     * @return a list of orders for the specified date
     * @throws OrderDataPersistenceException if there is an error during data retrieval
     */
    List<Order> getAllOrdersByDay(LocalDate date) throws OrderDataPersistenceException;
    /**
     * Removes an order by its order number and date.
     *
     * @param orderNumber the number of the order to remove
     * @param date the date associated with the order
     * @return the removed order object
     * @throws OrderDataPersistenceException if the order is not found or cannot be removed
     */
    Order removeOrder(int orderNumber, LocalDate date) throws OrderDataPersistenceException;
    /**
     * Updates an order by its order number and date with new data.
     *
     * @param orderNumber the number of the order to retrieve
     * @param date the date associated with the order
     * @param newOrder new data that will overwrite the current
     * @return the previous order object that was overwritten
     * @throws OrderDataPersistenceException if the order is not found or cannot be updated
     */
    Order updateOrder(int orderNumber, LocalDate date ,Order newOrder) throws OrderDataPersistenceException;
    /**
     * Retrieves an order by its order number and date.
     *
     * @param orderNumber the number of the order to retrieve
     * @param date the date associated with the order
     * @return the order object if found
     * @throws OrderDataPersistenceException if the order is not found
     */
    Order getByOrderAndDate(int orderNumber, LocalDate date) throws OrderDataPersistenceException;
    /**
     * Exports all order data across all dates into a single file or external system.
     * Useful for backups or generating comprehensive reports.
     */
    void exportAll() throws OrderDataPersistenceException;
    /**
     * Retrieves the maximum order number in the system.
     *
     * @return the highest order number
     * @throws OrderDataPersistenceException if there is an error during data retrieval
     */
    int getMaxOrderNumber() throws OrderDataPersistenceException;

}
