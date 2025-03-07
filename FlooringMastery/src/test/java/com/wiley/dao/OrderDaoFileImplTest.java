package com.wiley.dao;

import com.wiley.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderDaoFileImplTest {

    OrderDao testOrderDao;
    private final String TEST_ORDER_FOLDER = "src/test/TestFileData/Orders";
    private final String TEST_EXPORT_FOLDER = "src/test/TestFileData/Backup";
    public OrderDaoFileImplTest() {
    }

    @BeforeEach
    void setUp() throws Exception {
        //ensure test directories exist
        Files.createDirectories(Paths.get(TEST_ORDER_FOLDER));
        Files.createDirectories(Paths.get(TEST_EXPORT_FOLDER));

        testOrderDao = new OrderDaoFileImpl(TEST_ORDER_FOLDER, TEST_EXPORT_FOLDER);

        //create sample file
        LocalDate testDate = LocalDate.now().plusDays(1);
        String filePath = TEST_ORDER_FOLDER + "/Orders_" + testDate.format(DateTimeFormatter.ofPattern("MMddyyyy")) + ".txt";

        //start with fresh file every test
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, false))) {
            //write header
            writer.println("OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total");
            //add sample order
            writer.println("1,Ada Lovelace,CA,25.00,Tile,249.00,3.50,4.15,871.50,1033.35,476.21,2381.06");
            writer.flush();

        }

    }

    @Test
    void testAddGetOrderValid() throws Exception {
        //Arrange
        Order order = new Order(1);
        order.setCustomerName("Lada Lovelace");
        order.setState("CA");
        order.setTaxRate(new BigDecimal("25.00"));
        order.setProductType("Tile");
        order.setArea(new BigDecimal("249.00"));
        order.setCostPerSquareFoot(new BigDecimal("3.50"));
        order.setLaborCostPerSquareFoot(new BigDecimal("4.15"));
        order.setMaterialCost(new BigDecimal("871.50"));
        order.setLaborCost(new BigDecimal("1033.35"));
        order.setTax(new BigDecimal("476.21"));
        order.setTotal(new BigDecimal("2381.06"));
        order.setDate(LocalDate.now().plusDays(1));

        //Act
        testOrderDao.addOrder(order, order.getDate());

        Order retrievedOrder = testOrderDao.getByOrderAndDate(1, order.getDate());

        //Assert
        assertNotNull(retrievedOrder, "order should not be null.");
        assertEquals("Lada Lovelace", retrievedOrder.getCustomerName(), "the order should be Ada's");
    }


    @Test
    void testGetAllOrdersByDay() throws Exception {
        //Arrange
        LocalDate date = LocalDate.now().plusDays(1);
        String filePath = TEST_ORDER_FOLDER + "/Orders_" + date.format(DateTimeFormatter.ofPattern("MMddyyyy")) + ".txt";
        //Act
        List<Order> orders = testOrderDao.getAllOrdersByDay(date);
        //Assert
        assertNotNull(orders, "there should be one order in the file, not null");
        assertEquals(1, orders.size(), "there should be one order in the file");
    }

    @Test
    void testUpdateOrder() throws Exception {
        //Arrange
        LocalDate date = LocalDate.now().plusDays(1);
        Order order = testOrderDao.getByOrderAndDate(1, date);

        //Act
        order.setCustomerName("Jane Smith");
        testOrderDao.updateOrder(1, date, order);
        Order updatedOrder = testOrderDao.getByOrderAndDate(1, date);

        //Assert
        assertEquals("Jane Smith", updatedOrder.getCustomerName(), "order should have changed from Ada to Jane");
    }

    @Test
    void testRemoveOrder() throws Exception {
        //Arrange
        LocalDate date = LocalDate.now().plusDays(1);
        //Act
        Order removedOrder = testOrderDao.removeOrder(1, date);
        //Assert
        assertNotNull(removedOrder, "Ada should have been removed");
        assertThrows(OrderDataPersistenceException.class, () -> testOrderDao.getByOrderAndDate(1, date), "Ada is removed, should display order not found");
    }

    @Test
    void testGetByOrderAndDateOrderNotFound() {
        LocalDate date = LocalDate.now().plusDays(1);
        assertThrows(OrderDataPersistenceException.class, () -> testOrderDao.getByOrderAndDate(999, date), "order #999 should not be in the file");
    }

    @Test
    void testGetAllOrdersByDateEmptyList() throws Exception{
        testOrderDao.removeOrder(1, LocalDate.now().plusDays(1));
        List<Order> orders = testOrderDao.getAllOrdersByDay(LocalDate.now().plusDays(1));
        assertTrue(orders.isEmpty());
    }

    @Test
    void testGetMaxOrderNumber() throws Exception {
        int maxOrder = testOrderDao.getMaxOrderNumber();
        assertTrue(maxOrder == 1, "Ada is #1");
    }











}