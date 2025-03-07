package com.wiley.dao;

import com.wiley.model.Order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderDaoStubImpl implements OrderDao{
    public Order onlyOrder;

    public OrderDaoStubImpl() {
        onlyOrder = new Order(1);
        onlyOrder.setCustomerName("Lada Lovelace");
        onlyOrder.setState("CA");
        onlyOrder.setTaxRate(new BigDecimal("25.00"));
        onlyOrder.setProductType("Tile");
        onlyOrder.setArea(new BigDecimal("249.00"));
        onlyOrder.setCostPerSquareFoot(new BigDecimal("3.50"));
        onlyOrder.setLaborCostPerSquareFoot(new BigDecimal("4.15"));
        onlyOrder.setMaterialCost(new BigDecimal("871.50"));
        onlyOrder.setLaborCost(new BigDecimal("1033.35"));
        onlyOrder.setTax(new BigDecimal("476.21"));
        onlyOrder.setTotal(new BigDecimal("2381.06"));
        onlyOrder.setDate(LocalDate.of(2025, 3, 7));
    }

    public OrderDaoStubImpl(Order onlyOrder) {
        this.onlyOrder = onlyOrder;
    }
    @Override
    public Order addOrder(Order order, LocalDate date) throws OrderDataPersistenceException {
        if (order.equals(onlyOrder) && date.equals(onlyOrder.getDate())){
            return onlyOrder;
        }
        else {
            return null;
        }
    }

    @Override
    public List<Order> getAllOrdersByDay(LocalDate date) throws OrderDataPersistenceException {
        List<Order> orders = new ArrayList<>();
        if (date.equals(onlyOrder.getDate())){
            orders.add(onlyOrder);
        }
        return orders;
    }

    @Override
    public Order removeOrder(int orderNumber, LocalDate date) throws OrderDataPersistenceException {
        if (orderNumber == onlyOrder.getOrderNumber() && date.equals(onlyOrder.getDate())){
            return onlyOrder;
        } else{
            return null;
        }

    }

    @Override
    public Order updateOrder(int orderNumber, LocalDate date, Order newOrder) throws OrderDataPersistenceException {
        if (orderNumber == onlyOrder.getOrderNumber() && date.equals(onlyOrder.getDate())){
            onlyOrder.setDate(date);
            onlyOrder.setCustomerName(newOrder.getCustomerName());
            onlyOrder.setState(newOrder.getState());
            onlyOrder.setTaxRate(newOrder.getTaxRate());
            onlyOrder.setProductType(newOrder.getProductType());
            onlyOrder.setArea(newOrder.getArea());
            onlyOrder.setCostPerSquareFoot(newOrder.getCostPerSquareFoot());
            onlyOrder.setLaborCostPerSquareFoot(newOrder.getLaborCostPerSquareFoot());
            onlyOrder.setMaterialCost(newOrder.getMaterialCost());
            onlyOrder.setLaborCost(newOrder.getLaborCost());
            onlyOrder.setTax(newOrder.getTax());
            onlyOrder.setTotal(newOrder.getTotal());

            return onlyOrder;
        }else {
            return null;
        }
    }

    @Override
    public Order getByOrderAndDate(int orderNumber, LocalDate date) throws OrderDataPersistenceException {
        if (orderNumber == onlyOrder.getOrderNumber() && date.equals(onlyOrder.getDate())){
            return onlyOrder;
        }
        else {
            return null;
        }
    }

    @Override
    public void exportAll() {

    }

    @Override
    public int getMaxOrderNumber() throws OrderDataPersistenceException {
        return onlyOrder.getOrderNumber();
    }
}
