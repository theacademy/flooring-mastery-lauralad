package com.wiley.dao;

import com.wiley.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductDaoFileImplTest {
    ProductDao testProductDao;
    private Path tempFile;

    public ProductDaoFileImplTest() {
    }

    @BeforeEach
    void setUp() throws Exception {
        //create a temporary test file to simulate different scenarios
        tempFile = Files.createTempFile("testProducts", ".txt");
        new FileWriter(tempFile.toString());
        testProductDao = new ProductDaoFileImpl(tempFile.toString());
    }

    private void writeToFile(String content) throws IOException {
        try (FileWriter writer = new FileWriter(tempFile.toFile())) {
            writer.write(content);
        }
    }

    @org.junit.jupiter.api.Test
    void testGetAllProductsValidData() throws Exception {
        //Arrange: Valid data
        String data = "ProductType,CostPerSquareFoot,LaborCostPerSquareFoot\n" +
                "Carpet,2.25,2.10\n" +
                "Laminate,1.75,2.10\n" +
                "Tile,3.50,4.15\n" +
                "Wood,5.15,4.75\n";
        writeToFile(data);
        Product carpet = new Product();
        carpet.setProductType("Carpet");
        carpet.setCostPerSquareFoot(new BigDecimal("2.25"));
        carpet.setLaborCostPerSquareFoot(new BigDecimal("2.10"));

        Product laminate = new Product();
        laminate.setProductType("Laminate");
        laminate.setCostPerSquareFoot(new BigDecimal("1.75"));
        laminate.setLaborCostPerSquareFoot(new BigDecimal("2.10"));

        Product tile = new Product();
        tile.setProductType("Tile");
        tile.setCostPerSquareFoot(new BigDecimal("3.50"));
        tile.setLaborCostPerSquareFoot(new BigDecimal("4.15"));

        Product wood = new Product();
        wood.setProductType("Wood");
        wood.setCostPerSquareFoot(new BigDecimal("5.15"));
        wood.setLaborCostPerSquareFoot(new BigDecimal("4.75"));

        //Act
        List<Product> products = testProductDao.getAllProducts();

        //Assert

        //check general list length
        assertEquals(4, products.size(), "there should be 4 products");

        //then check if each product was stored properly
        //since we cannot quarantee the order, we just check that it contains all
        //products that should be there
        assertTrue(checkProductFields(products, carpet));
        assertTrue(checkProductFields(products, laminate));
        assertTrue(checkProductFields(products, tile));
        assertTrue(checkProductFields(products, wood));

    }

    private boolean checkProductFields(List<Product> products, Product product){
        for (Product productFromList : products) {
            if (productFromList.getProductType().equals(product.getProductType()) &&
                    productFromList.getCostPerSquareFoot().equals(product.getCostPerSquareFoot()) &&
                    productFromList.getLaborCostPerSquareFoot().equals(product.getLaborCostPerSquareFoot())) {
                return true;
            }
        }
        return false;
    }

    @Test
    void testGetAllProductsOneValidData() throws Exception {
        //Arrange: Valid data
        String data = "ProductType,CostPerSquareFoot,LaborCostPerSquareFoot\n" +
                "Carpet,2.25,2.10\n";
        writeToFile(data);

        //Act
        List<Product> products = testProductDao.getAllProducts();

        //Assert

        //check general list length
        assertEquals(1, products.size(), "there should be 4 products");

        //then check if each product was stored properly
        assertEquals("Carpet", products.get(0).getProductType());
        assertEquals(new BigDecimal("2.25"), products.get(0).getCostPerSquareFoot());
        assertEquals(new BigDecimal("2.10"), products.get(0).getLaborCostPerSquareFoot());

    }


    @org.junit.jupiter.api.Test
    void testGetAllProductsEmptyFile() throws Exception {
        //Arrange: empty file
        writeToFile("ProductType,CostPerSquareFoot,LaborCostPerSquareFoot\n");

        //Act
        List<Product> products = testProductDao.getAllProducts();

        //Assert
        assertTrue(products.isEmpty(), "Product list should be empty for an empty file");
    }


    @org.junit.jupiter.api.Test
    void testGetByProductTypeValidProduct() throws Exception {
        //Arrange: Add a product
        String data = "ProductType,CostPerSquareFoot,LaborCostPerSquareFoot\n" +
                "Tile,3.50,4.15\n";
        writeToFile(data);

        //Act
        Product product = testProductDao.getByProductType("Tile");

        //Assert
        assertNotNull(product, "Product Tile should be found");
        assertEquals("Tile", product.getProductType());
        assertEquals(new BigDecimal("3.50"), product.getCostPerSquareFoot());
        assertEquals(new BigDecimal("4.15"), product.getLaborCostPerSquareFoot());
    }

    @Test
    void testGetByProductType_ProductNotFound() throws Exception {
        //Arrange: Only one product in the file
        String data = "ProductType,CostPerSquareFoot,LaborCostPerSquareFoot\n" +
                "Tile,3.50,4.15\n";
        writeToFile(data);

        //Act
        Product product = testProductDao.getByProductType("Wood");

        //Assert
        assertNull(product, "Product should be null if not found");
    }

}