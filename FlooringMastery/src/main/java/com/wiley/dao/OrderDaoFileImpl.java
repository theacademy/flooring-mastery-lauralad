package com.wiley.dao;

import com.wiley.model.Order;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class OrderDaoFileImpl implements OrderDao{
    private final String ORDER_FOLDER_PATH;
    private final String EXPORT_FOLDER_PATH;
    public static final String DELIMITER = ",";
    private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MMddyyyy");

    public OrderDaoFileImpl() {
        this.ORDER_FOLDER_PATH = "src/main/SampleFileData/Orders";
        this.EXPORT_FOLDER_PATH = "src/main/SampleFileData/Backup";
    }

    public OrderDaoFileImpl(String ORDER_FOLDER_PATH, String EXPORT_FOLDER_PATH) {
        this.ORDER_FOLDER_PATH = ORDER_FOLDER_PATH;
        this.EXPORT_FOLDER_PATH = EXPORT_FOLDER_PATH;
    }

    private HashMap<Integer, Order> load(LocalDate date) throws OrderDataPersistenceException {
        // build the file name to load
        String orderFilePath = "Orders_" + date.format(FORMATTER) + ".txt";
        orderFilePath = ORDER_FOLDER_PATH + "/" + orderFilePath;
        HashMap<Integer, Order> orderMap = new HashMap<>();
        Scanner scanner;

        try {
            // Create scanner for reading file
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(orderFilePath)));
        } catch (FileNotFoundException e) {
            throw new OrderDataPersistenceException("-_- Could not retrieve order data.", e);
        }
        String currentLine;
        Order currentOrder;

        // skip the first header line
        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        // Go through file line by line, decoding each line into a
        // order object by calling the unmarshall method.
        // Process while we have more lines in the file
        while (scanner.hasNextLine()){
            currentLine = scanner.nextLine();
            currentOrder = unmarshallOrder(currentLine);
            currentOrder.setDate(date);

            // We are going to use the state name as the map key for our order object.
            // Put current order into the map using product type as the key
            orderMap.put(currentOrder.getOrderNumber(), currentOrder);
        }

        scanner.close();

        return orderMap;
    }

    private Order unmarshallOrder(String orderAsText){
        //file format:
        //OrderNumber,
        // CustomerName,
        // State,
        // TaxRate,
        // ProductType,
        // Area,
        // CostPerSquareFoot,
        // LaborCostPerSquareFoot,
        // MaterialCost,
        // LaborCost,
        // Tax,
        // Total
        //date
        String[] orderTokens = orderAsText.split(DELIMITER);

        int orderNumber = Integer.parseInt(orderTokens[0]);
        Order orderFromFile = new Order(orderNumber);
        orderFromFile.setCustomerName(orderTokens[1]);
        orderFromFile.setState(orderTokens[2]);
        orderFromFile.setTaxRate(new BigDecimal(orderTokens[3]));
        orderFromFile.setProductType(orderTokens[4]);
        orderFromFile.setArea(new BigDecimal(orderTokens[5]));
        orderFromFile.setCostPerSquareFoot(new BigDecimal(orderTokens[6]));
        orderFromFile.setLaborCostPerSquareFoot(new BigDecimal(orderTokens[7]));
        orderFromFile.setMaterialCost(new BigDecimal(orderTokens[8]));
        orderFromFile.setLaborCost(new BigDecimal(orderTokens[9]));
        orderFromFile.setTax(new BigDecimal(orderTokens[10]));
        orderFromFile.setTotal(new BigDecimal(orderTokens[11]));

        return orderFromFile;
    }

    private String marshallOrder(Order anOrder, boolean hasDate){
        //file format:
        //OrderNumber,
        // CustomerName,
        // State,
        // TaxRate,
        // ProductType,
        // Area,
        // CostPerSquareFoot,
        // LaborCostPerSquareFoot,
        // MaterialCost,
        // LaborCost,
        // Tax,
        // Total
        String orderAsText = anOrder.getOrderNumber() + DELIMITER;
        orderAsText += anOrder.getCustomerName() + DELIMITER;
        orderAsText += anOrder.getState() + DELIMITER;
        orderAsText += anOrder.getTaxRate() + DELIMITER;
        orderAsText += anOrder.getProductType() + DELIMITER;
        orderAsText += anOrder.getArea() + DELIMITER;
        orderAsText += anOrder.getCostPerSquareFoot() + DELIMITER;
        orderAsText += anOrder.getLaborCostPerSquareFoot() + DELIMITER;
        orderAsText += anOrder.getMaterialCost() + DELIMITER;
        orderAsText += anOrder.getLaborCost() + DELIMITER;
        orderAsText += anOrder.getTax() + DELIMITER;
        if (hasDate) {
            orderAsText += anOrder.getTotal() + DELIMITER;
            orderAsText += anOrder.getDate();
        } else {
            orderAsText += anOrder.getTotal();
        }


        return orderAsText;

    }

    private void save(LocalDate date, HashMap<Integer, Order> orderMap) throws OrderDataPersistenceException {
        String orderFilePath = "Orders_" + date.format(FORMATTER) + ".txt";
        orderFilePath =  ORDER_FOLDER_PATH + "/" + orderFilePath;

        PrintWriter out;

        try {
            out = new PrintWriter(new FileWriter(orderFilePath, false));
        } catch (IOException e){
            throw new OrderDataPersistenceException("Could not save order data to file.", e);

        }
        String orderAsText;

        // add header to file
        out.println("OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total");
        for (Order currentOrder: orderMap.values()) {
            // turn a order into a String
            orderAsText = this.marshallOrder(currentOrder, false);
            // write the order object to the file
            out.println(orderAsText);
            // force to write line to file
            out.flush();
        }
        //clean up
        out.close();
    }

    @Override
    public Order addOrder(Order order, LocalDate date) throws OrderDataPersistenceException {
        //first load the file and grab the orders (throws exception if file not found)
        //in this case we want to create a new file, so we catch the exception
        HashMap<Integer,Order> orderMap;
        try {
            orderMap = load(date);
        } catch (OrderDataPersistenceException e) {
            //if the file doesn't exist, initialize an empty map
            orderMap = new HashMap<>();
        }

        Order prevOrder = orderMap.put(order.getOrderNumber(), order);

        //now write the updated order list to the file
        save(date, orderMap);

        //if there was no value for that key before, return null
        //else return the previous value
        return prevOrder;
    }

    @Override
    public List<Order> getAllOrdersByDay(LocalDate date) throws OrderDataPersistenceException {
        return new ArrayList<>(load(date).values());
    }

    @Override
    public Order removeOrder(int orderNumber, LocalDate date) throws OrderDataPersistenceException {
        HashMap<Integer, Order> orders = load(date);
        Order removedOrder = orders.remove(orderNumber);
        save(date, orders);
        return removedOrder;
    }

    @Override
    public Order updateOrder(int orderNumber, LocalDate date ,Order newOrder) throws OrderDataPersistenceException {
        HashMap<Integer, Order> orderMap = load(date);
        Order previousOrder = orderMap.replace(orderNumber, newOrder);
        save(date, orderMap);
        //returns null if key did not already exist
        //else previous Order object
        return previousOrder;
    }

    @Override
    public Order getByOrderAndDate(int orderNumber, LocalDate date) throws OrderDataPersistenceException {
        //since load() returns a hashmap, simply retrieve the orderNumber key
        Order order = load(date).get(orderNumber);
        if (order != null){
            return order;
        } else {
            throw new OrderDataPersistenceException("No such order found.");
        }
    }

    @Override
    public void exportAll() throws OrderDataPersistenceException {
        //iterate through all the files in the folder path
        //load data for each
        //export this to export folder path

        File orderFolder = new File(ORDER_FOLDER_PATH);
        if (!orderFolder.exists() || !orderFolder.isDirectory()) {
            throw new OrderDataPersistenceException("Could not load order files.");
        }

        HashMap<Integer, Order> allOrders = new HashMap<>();

        //just making we only retrieve the Order files, although there should not be anything else there
        File[] files = orderFolder.listFiles(
                (dir, name) -> name.startsWith("Orders_") && name.endsWith(".txt")
        );
        if (files != null) {
            for (File file : files) {
                String filename = file.getName();
                // retrieve the date so we can add it
                String dateString = filename.substring(7, 15);
                LocalDate date = LocalDate.parse(dateString, FORMATTER);

                // now save all these orders in the hashmap once we call the load method to handle it for us
                allOrders.putAll(load(date));
            }
        }
        // then save all the orders into a separate file in the export folder
        saveExportFile(allOrders);
    }

    private void saveExportFile(HashMap<Integer, Order> orders) throws OrderDataPersistenceException {
        File exportFolder = new File(EXPORT_FOLDER_PATH);
        if (!exportFolder.exists()) {
            exportFolder.mkdirs();
        }

        //saving it as Backup_<date of the backup request>
        String exportFileName = "Backup_" + LocalDate.now().format(FORMATTER) + ".txt";
        File exportFile = new File(exportFolder, exportFileName);

        PrintWriter out;
        try {
            out = new PrintWriter(new FileWriter(exportFile));
        } catch (IOException e){
            throw new OrderDataPersistenceException("Could not save order data to file.", e);

        }

        //write header file
        out.println("OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total,Date");
        String orderAsText;

        //write the content
        for (Order order : orders.values()) {
            orderAsText = this.marshallOrder(order, true);
            // write the order object to the file
            out.println(orderAsText);
            // force to write line to file
            out.flush();
        }
        out.close();

    }


    public int getMaxOrderNumber() throws OrderDataPersistenceException {
        File orderFolder = new File(ORDER_FOLDER_PATH);

        if (orderFolder.exists() && orderFolder.isDirectory()){
            File[] files = orderFolder.listFiles();
            Set<Integer> orderNumbers = new HashSet<>();
            //if there are any files, then we have to iterate through them
            // and fin the max orderNumber
            if (files != null) {
                for (File file: files){
                    //retrieve the filename so we can get the date
                    String filename = file.getName();
                    // filename format: Orders_01012001.txt
                    String dateString = filename.substring(7,15);
                    LocalDate date = LocalDate.parse(dateString, FORMATTER);

                    //now we call load with the retrieved date and perform
                    //union operation on the big set of all keys (=orderNumbers)
                    Set<Integer> keySey = load(date).keySet();
                    orderNumbers.addAll(keySey);
                }
            }
            // compare the order numbers and get max, but if we don't have anything, return 0
            return orderNumbers.stream()
                    .max(Integer::compareTo)
                    .orElse(0);
        } else{
            throw new OrderDataPersistenceException("Could not load the orders.");
        }

    }

}
