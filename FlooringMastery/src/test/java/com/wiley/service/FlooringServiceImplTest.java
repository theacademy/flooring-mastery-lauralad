package com.wiley.service;

import com.wiley.dao.*;
import com.wiley.model.Order;
import com.wiley.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FlooringServiceImplTest {
    FlooringService service;

    public FlooringServiceImplTest() {
        ApplicationContext ctx =
                new ClassPathXmlApplicationContext("applicationContext.xml");
        service =
                ctx.getBean("serviceLayer", FlooringService.class);
    }

    @Test
    void testCalculateMaterialCost() {
        Order order = new Order(1);
        order.setArea(new BigDecimal("200"));
        order.setCostPerSquareFoot(new BigDecimal("3.50"));

        BigDecimal materialCost = service.calculateMaterialCost(order);

        assertEquals(new BigDecimal("700.00"), materialCost, "200 * 3.50 = 700");
    }

    @Test
    void testCalculateLaborCost() {
        Order order = new Order(1);
        order.setArea(new BigDecimal("200"));
        order.setLaborCostPerSquareFoot(new BigDecimal("4.00"));

        BigDecimal laborCost = service.calculateLaborCost(order);

        assertEquals(new BigDecimal("800.00"), laborCost);
    }

    @Test
    void testCalculateTax() {
        Order order = new Order(1);
        order.setMaterialCost(new BigDecimal("700.00"));
        order.setLaborCost(new BigDecimal("800.00"));
        order.setTaxRate(new BigDecimal("25.00"));

        BigDecimal tax = service.calculateTax(order);

        assertEquals(new BigDecimal("375.00"), tax);
    }
    @Test
    void testCalculateTotal() {
        Order order = new Order(1);
        order.setMaterialCost(new BigDecimal("700.00"));
        order.setLaborCost(new BigDecimal("830.00"));
        order.setTax(new BigDecimal("382.50"));

        BigDecimal total = service.calculateTotal(order);

        assertNotNull(total);
        assertEquals(new BigDecimal("1912.50"), total);
    }


    @Test
    void testCreateOrderAndAssignOrderNumber() {
        service.init();
        Order newOrder = service.createOrderAndAssignOrderNumber();

        assertNotNull(newOrder);
        assertEquals(2, newOrder.getOrderNumber(), "with a second order created, maxId = 2");
    }

    @Test
    void testValidateCustomerNameInformation() {
        assertDoesNotThrow(() -> service.validateCustomerNameInformation("John Doe"));
    }
    @Test
    void testValidateInvalidCustomerNameInformation() {
        assertThrows(OrderInformationInvalidException.class, () -> service.validateCustomerNameInformation("John@Doe"), "no @ should be allowed");
    }
    @Test
    void testValidateAreaInformationValidArea() {
        assertDoesNotThrow(() -> service.validateAreaInformation(new BigDecimal("150")));
    }
    @Test
    void testValidateAreaInformationInvalidArea() {
        assertThrows(OrderInformationInvalidException.class, () -> service.validateAreaInformation(new BigDecimal("50")));
    }
    @Test
    void testValidateDateInformation() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        assertDoesNotThrow(() -> service.validateDateInformation(futureDate), "future date should be allowed");

        LocalDate pastDate = LocalDate.now().minusDays(1);
        assertThrows(OrderInformationInvalidException.class, () -> service.validateDateInformation(pastDate), "past date should not be allowed here");
    }
    @Test
    void testValidateStateInformation() {
        assertDoesNotThrow(() -> service.validateStateInformation("CA"), "CA should be allowed, we cover that state");
        assertThrows(TaxInformationInvalidException.class, () -> service.validateStateInformation("NY"), "NY is not allowed");
    }

    @Test
    void testGetAllOrdersForDate() throws OrderDataPersistenceException {
        //match the date in stub
        LocalDate date = LocalDate.of(2025, 3, 7);

        List<Order> result = service.getAllOrdersForDate(date);

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getOrderNumber());
        assertEquals("Lada Lovelace", result.get(0).getCustomerName());
    }

    @Test
    void testGetAllOrdersForDateNoMatch() throws OrderDataPersistenceException {
        //don't match the date in stub
        LocalDate date = LocalDate.of(2026, 3, 7);

        List<Order> result = service.getAllOrdersForDate(date);

        assertEquals(0, result.size());
        assertTrue(result.isEmpty());
    }

    @Test
    void testAddOrder() {
        Order newOrder = new Order(2);
        newOrder.setCustomerName("Alan Turing");
        LocalDate date = LocalDate.of(2025, 3, 8);

        try {
            service.addOrder(newOrder, date);
        } catch (OrderDataPersistenceException e) {
            fail("order was valid technically");
        }
    }

    @Test
    void testEditOrder() throws OrderDataPersistenceException {
        LocalDate date = LocalDate.of(2025, 3, 7);
        Order updatedOrder = new Order(1);
        updatedOrder.setCustomerName("Ada Bee");

        Order result = service.editOrder(date, updatedOrder);
        assertNotNull(result);
        assertEquals("Ada Bee", result.getCustomerName(), "Updated order should now be Ada");
    }

    @Test
    void testRemoveOrder() throws OrderDataPersistenceException {
        LocalDate date = LocalDate.of(2025, 3, 7);
        Order removedOrder = service.removeOrder(1, date);

        assertNotNull(removedOrder);
        assertEquals(1, removedOrder.getOrderNumber(), "order #1 should have been removed");
    }

    @Test
    void testGetOrder() throws OrderDataPersistenceException {
        LocalDate date = LocalDate.of(2025, 3, 7);
        Order order = service.getOrder(1, date);

        assertNotNull(order);
        assertEquals(1, order.getOrderNumber(), "Order #1 should be in there");
    }

    @Test
    void testGetAllStates() throws OrderDataPersistenceException {
        Set<String> states = service.getAllStates();
        assertNotNull(states);
        assertTrue(states.contains("CA"), "CA is in the list of states");
    }

    @Test
    void testGetAllProducts() throws OrderDataPersistenceException {
        List<Product> products = service.getAllProducts();
        assertNotNull(products);
        assertFalse(products.isEmpty(), "there should be one product in the list");
    }
    @Test
    void testGetByProductType() throws OrderDataPersistenceException {
        Product product = service.getByProductType("Wood");
        assertNotNull(product);
        assertEquals("Wood", product.getProductType(), "Wood should be in the list");
    }

    @Test
    void testGetAllProductTypes() throws OrderDataPersistenceException {
        List<String> productTypes = service.getAllProductTypes();
        assertNotNull(productTypes);
        assertEquals(1,productTypes.size());
        assertTrue(productTypes.contains("Wood"), "Wood should be in the list");
    }






}