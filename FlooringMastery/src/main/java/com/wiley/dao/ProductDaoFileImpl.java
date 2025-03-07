package com.wiley.dao;

import com.wiley.model.Product;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class ProductDaoFileImpl implements ProductDao{
    //Map<productType, Product>
    private Map<String, Product> productMap = new HashMap<>();
    private final String PRODUCT_FILE_PATH;
    public static final String DELIMITER = ",";

    public ProductDaoFileImpl() {
        this.PRODUCT_FILE_PATH = "src/main/SampleFileData/Data/Products.txt";
    }

    public ProductDaoFileImpl(String PRODUCT_FILE_PATH) {
        this.PRODUCT_FILE_PATH = PRODUCT_FILE_PATH;
    }


    @Override
    public List<Product> getAllProducts() throws OrderDataPersistenceException {
        load();
        return new ArrayList<Product>(productMap.values());
    }

    @Override
    public Product getByProductType(String productType) throws OrderDataPersistenceException {
        load();
        return productMap.get(productType);
    }

    @Override
    public List<String> getAllProductTypes() throws OrderDataPersistenceException {
        return getAllProducts().stream()
                .map((p) -> p.getProductType())
                .collect(Collectors.toList());
    }

    private void load() throws OrderDataPersistenceException {
        Scanner scanner;

        try {
            // Create scanner for reading file
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(PRODUCT_FILE_PATH)));
        } catch (FileNotFoundException e) {
            throw new OrderDataPersistenceException("-_- Could not load product data into memory.", e);
        }
        String currentLine;
        Product currentProduct;

        // skip the first header line
        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        // Go through file line by line, decoding each line into a
        // product object by calling the unmarshall method.
        // Process while we have more lines in the file
        while (scanner.hasNextLine()){
            currentLine = scanner.nextLine();

            currentProduct = unmarshallProduct(currentLine);

            // We are going to use the state name as the map key for our product object.
            // Put current product into the map using product type as the key
            productMap.put(currentProduct.getProductType(), currentProduct);
        }

        scanner.close();
    }

    private Product unmarshallProduct(String productAsText){
        //file format:
        //ProductType,CostPerSquareFoot,LaborCostPerSquareFoot
        String[] productTokens = productAsText.split(DELIMITER);

        Product productFromFile = new Product();
        productFromFile.setProductType(productTokens[0]);
        // since the tokens are String, we can use the big decimal String constructor
        productFromFile.setCostPerSquareFoot(new BigDecimal(productTokens[1]));
        productFromFile.setLaborCostPerSquareFoot(new BigDecimal(productTokens[2]));

        return productFromFile;
    }
}
