package com.wiley.service;

import com.wiley.dao.OrderDao;
import com.wiley.dao.OrderDataPersistenceException;
import com.wiley.dao.ProductDao;
import com.wiley.dao.TaxDao;
import com.wiley.model.Order;
import com.wiley.model.Product;
import com.wiley.model.Tax;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FlooringServiceImpl implements FlooringService{
    private int maxId;
    private static final int SCALE = 2;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
    private OrderDao orderDao;
    private ProductDao productDao;
    private TaxDao taxDao;

    public FlooringServiceImpl(
            OrderDao orderDao,
            ProductDao productDao,
            TaxDao taxDao) {
        this.orderDao = orderDao;
        this.productDao = productDao;
        this.taxDao = taxDao;
    }

    @Override
    public int getMaxId() {
        return maxId;
    }

    @Override
    public void init() {
        try {
            this.maxId = orderDao.getMaxOrderNumber();
        } catch (OrderDataPersistenceException e){
            this.maxId = 0;
        }
    }

    @Override
    public BigDecimal calculateMaterialCost(Order order){
        return order.getArea()
                .multiply(order.getCostPerSquareFoot())
                .setScale(SCALE, ROUNDING_MODE);
    }

    @Override
    public BigDecimal calculateLaborCost(Order order) {
        return order.getArea()
                .multiply(order.getLaborCostPerSquareFoot())
                .setScale(SCALE, ROUNDING_MODE);
    }

    @Override
    public BigDecimal calculateTax(Order order) {
        BigDecimal costs = order.getMaterialCost().add(order.getLaborCost());
        // even though tax rates are supposedly stored as whole numbers and the tax calc. shows that fact,
        //the sample files show the actual tax rate saved, but in the calculations they have used the whole number
        // thus: we store the exact tax rate in the object and only round it to whole number for the calculation
        BigDecimal percentage = order.getTaxRate().setScale(0, ROUNDING_MODE).divide(new BigDecimal("100"), SCALE, ROUNDING_MODE);
        return costs.multiply(percentage).setScale(SCALE, ROUNDING_MODE);
    }

    @Override
    public BigDecimal calculateTotal(Order order) {
        return order.getMaterialCost().add(order.getLaborCost()).add(order.getTax());
    }


    @Override
    public List<Order> getAllOrdersForDate(LocalDate date) throws OrderDataPersistenceException {
        return orderDao.getAllOrdersByDay(date);
    }

    @Override
    public Order createOrderAndAssignOrderNumber(){
        //increment the maxId and assign it
        this.maxId++;
        return new Order(getMaxId());
    }

    @Override
    public Order addOrder(Order order, LocalDate date) throws OrderDataPersistenceException {
        return orderDao.addOrder(order, date);
    }

    @Override
    public Order editOrder(LocalDate date, Order order) throws OrderDataPersistenceException {
        return orderDao.updateOrder(order.getOrderNumber(), date, order);
    }


    @Override
    public boolean validateDateInformation(LocalDate date) throws OrderInformationInvalidException {
        // only check left is if date is in the future
        if (date.isBefore(LocalDate.now())) {
            throw new OrderInformationInvalidException("Date should be in the future.");
        }
        return true;
    }

    @Override
    public boolean validateCustomerNameInformation(String name) throws OrderInformationInvalidException {
        if (name == null || !name.matches("^[a-zA-Z0-9., ]+$")){
            throw new OrderInformationInvalidException("Customer name may not be blank and must only contain letters, numbers, periods, commas, and spaces.");
        }
        return true;
    }

    @Override
    public boolean validateStateInformation(String state) throws OrderDataPersistenceException, TaxInformationInvalidException {
        Tax taxData = taxDao.geByStateAbbreviation(state);
        //if there is no tax data for the given state key, returns null
        if(taxData == null){
            throw new TaxInformationInvalidException("We don't cover that state at the moment");
        }
        return true;
    }

    @Override
    public boolean validateAreaInformation(BigDecimal areaDecimal) throws OrderInformationInvalidException {
        if (areaDecimal.compareTo(new BigDecimal("100")) < 0) {
            throw new OrderInformationInvalidException("Area must be at least 100 sq.ft.");
        }
        return true;
    }

    @Override
    public Order removeOrder(int orderNumber, LocalDate date) throws OrderDataPersistenceException {
        return orderDao.removeOrder(orderNumber,date);
    }

    @Override
    public Order getOrder(int orderNumber, LocalDate date) throws
            OrderDataPersistenceException {
        return orderDao.getByOrderAndDate(orderNumber, date);
    }

    @Override
    public Tax geByStateAbbreviation(String taxAbbreviation) throws OrderDataPersistenceException {
        return taxDao.geByStateAbbreviation(taxAbbreviation);
    }

    @Override
    public Set<String> getAllStates() throws OrderDataPersistenceException {
        return taxDao.getAllStates();
    }

    @Override
    public List<Product> getAllProducts() throws OrderDataPersistenceException {
        return productDao.getAllProducts();
    }

    @Override
    public Product getByProductType(String productType) throws OrderDataPersistenceException {
        return productDao.getByProductType(productType);
    }

    @Override
    public List<String> getAllProductTypes() throws OrderDataPersistenceException {
        return productDao.getAllProductTypes();
    }


}
