package com.wiley.dao;

import com.wiley.model.Tax;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class TaxDaoFileImpl implements TaxDao{
    //Map<state_abbrev, Tax>
    private Map<String, Tax> taxMap = new HashMap<>();
    private final String TAX_FILE_PATH;
    public static final String DELIMITER = ",";

    public TaxDaoFileImpl() {
        TAX_FILE_PATH = "src/main/SampleFileData/Data/Taxes.txt";
    }

    public TaxDaoFileImpl(String TAX_FILE_PATH) {
        this.TAX_FILE_PATH = TAX_FILE_PATH;
    }

    @Override
    public Tax geByStateAbbreviation(String taxAbbreviation) throws OrderDataPersistenceException {
        load();
        return taxMap.get(taxAbbreviation);
    }

    @Override
    public Set<String> getAllStates() throws OrderDataPersistenceException {
        load();
        return taxMap.keySet();
    }

    private void load() throws OrderDataPersistenceException {
        Scanner scanner;

        try {
            // Create scanner for reading file
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(TAX_FILE_PATH)));
        } catch (FileNotFoundException e) {
            throw new OrderDataPersistenceException("-_- Could not load tax data into memory.", e);
        }
        String currentLine;
        Tax currentTax;

        // skip the first header line
        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        // Go through file line by line, decoding each line into a
        // tax object by calling the unmarshall method.
        // Process while we have more lines in the file
        while (scanner.hasNextLine()){
            currentLine = scanner.nextLine();
            currentTax = unmarshallTax(currentLine);

            // We are going to use the state name as the map key for our tax object.
            // Put currenttax into the map using tax abbreviation as the key
            taxMap.put(currentTax.getStateAbbreviation(), currentTax);
        }

        scanner.close();
    }
    private Tax unmarshallTax(String taxAsText){
        // format is:
        // state_abbrev,state_name,tax_value
        String[] taxTokens = taxAsText.split(DELIMITER);

        Tax taxFromFile = new Tax();
        taxFromFile.setStateAbbreviation(taxTokens[0]);
        taxFromFile.setStateName(taxTokens[1]);
        //tax_value is a string so we can easily create a Big Decimal with a String
        taxFromFile.setTaxRate(new BigDecimal(taxTokens[2]));

        return taxFromFile;
    }

}
