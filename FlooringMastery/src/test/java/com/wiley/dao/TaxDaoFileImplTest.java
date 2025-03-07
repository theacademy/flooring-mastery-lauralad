package com.wiley.dao;

import com.wiley.model.Tax;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class TaxDaoFileImplTest {
    TaxDao testTaxDao;
    private Path tempFile;

    public TaxDaoFileImplTest() {
    }

    @BeforeEach
    void setUp() throws Exception {
        //create a temporary test file to simulate different scenarios
        tempFile = Files.createTempFile("testProducts", ".txt");
        new FileWriter(tempFile.toString());
        testTaxDao = new TaxDaoFileImpl(tempFile.toString());
    }

    private void writeToFile(String content) throws Exception {
        try (FileWriter writer = new FileWriter(tempFile.toFile())) {
            writer.write(content);
        }
    }

    @Test
    void testGeByStateAbbreviationValidData() throws Exception {
        String data = "State,StateName,TaxRate\n" +
                "TX,Texas,4.45\n" +
                "WA,Washington,9.25\n";
        writeToFile(data);

        Tax txTax = testTaxDao.geByStateAbbreviation("TX");
        assertNotNull(txTax, "Tax for TX should be found and not null");
        assertEquals("TX", txTax.getStateAbbreviation(), "State abbreviation should match");
        assertEquals("Texas", txTax.getStateName(), "State name should match");
        assertEquals(new BigDecimal("4.45"), txTax.getTaxRate(), "Tax rate for TX should be correct");

        Tax waTax = testTaxDao.geByStateAbbreviation("WA");
        assertNotNull(waTax, "Tax for WA should be found and not null");
        assertEquals("WA", waTax.getStateAbbreviation(), "State abbreviation should match");
        assertEquals("Washington", waTax.getStateName(), "State name should match");
        assertEquals(new BigDecimal("9.25"), waTax.getTaxRate(), "Tax rate for WA should be correct");
    }

    @Test
    void testGeByStateAbbreviationNonExistentData() throws Exception {
        String data = "State,StateName,TaxRate\n" +
                "TX,Texas,4.45\n" +
                "WA,Washington,9.25\n";
        writeToFile(data);

        Tax nyTax = testTaxDao.geByStateAbbreviation("NY");
        assertNull(nyTax, "Tax for non-existent state abbreviation should be null");

    }

    @Test
    void testGeByStateAbbreviationEmptyFile() throws Exception {
        String data = "State,StateName,TaxRate\n";
        writeToFile(data);

        Tax emptyTax = testTaxDao.geByStateAbbreviation("NY");
        assertNull(emptyTax, "Tax for empty file should be null");

    }

    @Test
    void geByStateAbbreviationAfterFileUpdate() throws Exception {
        //Write some initial data
        String data = "State,StateName,TaxRate\n" +
                "TX,Texas,4.45\n";
        writeToFile(data);

        //Load the data
        Tax txTax1 = testTaxDao.geByStateAbbreviation("TX");
        assertNotNull(txTax1, "TX data should not be null");
        assertEquals(new BigDecimal("4.45"), txTax1.getTaxRate());
        assertEquals("Texas", txTax1.getStateName());


        //Update the file with new data
        data = "State,StateName,TaxRate\n" +
                "TX,Texas,5.50\n";
        writeToFile(data);

        //Reload and get the updated tax data
        Tax txTax2 = testTaxDao.geByStateAbbreviation("TX");
        assertNotNull(txTax2);
        assertEquals(new BigDecimal("5.50"), txTax2.getTaxRate());
        assertEquals("Texas", txTax2.getStateName());
    }



}