package com.wiley.view;

import com.wiley.model.Order;
import com.wiley.model.Product;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Set;

public class FlooringView {
    private UserIO io;
    private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MMddyyyy");
    public FlooringView(UserIO io) {
        this.io = io;
    }

    public int menuSelection(){
        io.print("* * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        io.print("<<Flooring Program>>");
        io.print("* 1. Display Orders");
        io.print("* 2. Add an Order");
        io.print("* 3. Edit an Order");
        io.print("* 4. Remove an Order");
        io.print("* 5. Export All Data");
        io.print("* 6. Quit");
        io.print("*");
        io.print("* * * * * * * * * * * * * * * * * * * * * * * * * * * * *");

        return io.readInt("Please select from the"
                + " above choices.", 1, 6);
    }
    public void printProductList(List<String> products){
        io.print("Current available products: ");
        for (int i = 0; i < products.size(); i++) {
            io.print((i+1) + ". " + products.get(i));
        }
        io.print("");

    }

    public boolean doesUserConfirm(){
        String confirmation;
        boolean isConfirmed = false;
        boolean keepGoing = false;
        do {

            confirmation = io.readString("Do you confirm the order [Y\\N]: ");
            if (confirmation.equalsIgnoreCase("Y")) {
                isConfirmed = true;
                keepGoing = false;
            } else if (confirmation.equalsIgnoreCase("N")) {
                isConfirmed = false;
                keepGoing = false;
            } else {
                io.print("Invalid input. Please enter Y to confirm or N to reject the order.");
                keepGoing = true;
            }
        }while (keepGoing);

        return isConfirmed;
    }

    public int askOrderNumber(){
        int orderNumber;
        while (true) {
            try {
                orderNumber = io.readInt("* Enter the order number: ");
                break;
            } catch (NumberFormatException e) {
                io.print("Invalid input. Please enter a number.");
            }
        }
        return orderNumber;
    }

    public LocalDate askDate(){
        String stringDate;
        LocalDate date ;
        // only check if date is a valid date
        while (true){
            stringDate = io.readString("* Enter a date in MMDDYYYY format, no spaces: ");

            try {
                date = LocalDate.parse(stringDate, FORMATTER);
                break;
            } catch (DateTimeParseException e) {
                io.print("Invalid date format.");
            }
        }
        return date;
    }

    //methods for addOrder Information
    public String askCustomerName() {
        return io.readString("* Enter customer name: ");
    }
    public String askState(Set<String> states){
        io.print("Here are the states we currently cover: ");
        io.print(String.join(", ", states));
        return io.readString("* Enter a state abbreviation from the list of states above: ");
    }
    public int askProductType(int maxChoice) {
        int newProductIndex;
        String input;
        int inputIndex = 0;
        boolean keepGoing = true;
        do {
            input = io.readString("* Please select number of the product type you wish from the"
                    + " above choices: ");
            if (!input.trim().isEmpty()) {

                try {
                    inputIndex = Integer.parseInt(input);
                    if (inputIndex < 1 || inputIndex > maxChoice) {
                        io.print("Please select a number between 1 and " + maxChoice + ".");
                    } else {
                        keepGoing = false;
                    }
                } catch (NumberFormatException e) {
                    io.print("Invalid input. Please enter a number from the above choices.");
                }
            }
        } while (keepGoing);
        newProductIndex = inputIndex;
        return newProductIndex;
    }
    public String askArea() {
        // return empty string if user hits Enter or new value
        double newArea = 0;
        String stringNewArea = "";
        String input;
        boolean keepGoing = true;
        do {
            input = io.readString("* Enter area of min 100 sq.ft.: ");
            // if user did not hit Enter, check if input is a number
            if (!input.trim().isEmpty()) {
                try {
                    // must convert to Double to verify that user input a number
                    newArea = Double.parseDouble(input);
                    stringNewArea = String.format("%.2f", newArea);
                    keepGoing = false;
                } catch (NumberFormatException e) {
                    io.print("Invalid input. Please enter a number.");
                }
            }
        } while (keepGoing);
        return stringNewArea;

    }

    // methods for editOrder Information
    public String askNewCustomerName(String name){
        return io.readString("* Enter new customer name (current: " + name + ") or hit enter if you don't wish to change: ");
    }

    public String askNewState(Set<String> states){
        io.print("Here are the states we currently cover: ");
        io.print(String.join(", ", states));
        return io.readString("* Enter a state abbreviation from the list of states above or hit enter if you don't wish to change: ");
    }
    public int askNewProductType(String productType, int maxChoice){
        int newProductIndex;
        String input;
        int inputIndex = 0;
        boolean keepGoing = true;
        do {
            input = io.readString("* Please select number of the product type you wish (current: " + productType + ") from the"
                    + " above choices, or hit Enter if you don't wish to change this.");
            if (!input.trim().isEmpty()) {

                try {
                    inputIndex = Integer.parseInt(input);
                    if (inputIndex < 1 || inputIndex > maxChoice){
                        io.print("Please select a number between 1 and " + maxChoice + ".");
                    } else {
                        keepGoing = false;
                    }
                } catch (NumberFormatException e) {
                    io.print("Invalid input. Please enter a number from the above choices.");
                }
            } else{
                inputIndex = 0;
                keepGoing = false;
            }
        } while (keepGoing);
        newProductIndex = inputIndex;
        return newProductIndex;
    }

    public String askNewArea(String prevArea) {
        // return empty string if user hits Enter or new value
        double newArea = 0;
        String stringNewArea = "";
        String input;
        boolean keepGoing = true;
        do {
            input = io.readString("* Enter new area (current: " + prevArea + ") of min 100 sq.ft. or hit Enter if you don't wish to change: ");
            // if user did not hit Enter, check if input is a number
            if (!input.trim().isEmpty()) {
                try {
                    // must convert to Double to verify that user input a number
                    newArea = Double.parseDouble(input);
                    stringNewArea = String.format("%.2f", newArea);
                    keepGoing = false;
                } catch (NumberFormatException e) {
                    io.print("Invalid input. Please enter a number.");
                }
            } else {
                keepGoing = false;
            }

        } while (keepGoing);
        return stringNewArea;

    }


    public void displayOrdersBanner(){io.print("=== DISPLAY ORDERS ===");}


    public void askOrderBanner(){

        io.print("=== ADD ORDER ===");
    }
    public void addOrderSuccessBanner(){
        io.print("Order created successfully.");
        io.print("");
    }
    public void editOrderBanner() {

        io.print("=== EDIT ORDER ===");
    }

    public void editOrderSuccessBanner() {
        io.print("Order updated successfully.");
        io.print("");
    }

    public void removeOrderBanner() {

        io.print("=== REMOVE ORDER ===");
    }

    public void removeOrderSuccessBanner() {
        io.print("Order removed successfully.");
    }

    public void exportAllDataBanner(){

        io.print("=== EXPORT ALL DATA ===");
    }

    public void exportAllDataSuccessBanner() {
        io.print("Backup saved successfully.");
    }

    public void displayOrder(Order order){
        io.print(formatOrderInfo(order));
    }

    private String formatOrderInfo(Order order){
        String orderInfo = String.format(
                "************\n" +
                        "Order #%s : \n" +
                        "Customer name: %s\n" +
                        "State: %s\n" +
                        "Tax rate: %s\n" +
                        "Product type: %s\n" +
                        "Area: %s\n" +
                        "Cost per sq. ft.: $%s\n" +
                        "Labor cost per sq. ft.: $%s\n" +
                        "Material cost: $%s\n" +
                        "Labor cost: $%s\n" +
                        "Tax: $%s\n" +
                        "Total: $%s\n" +
                        "Date: %s",
                order.getOrderNumber(),
                order.getCustomerName(),
                order.getState(),
                order.getTaxRate(),
                order.getProductType(),
                order.getArea(),
                order.getCostPerSquareFoot(),
                order.getLaborCostPerSquareFoot(),
                order.getMaterialCost(),
                order.getLaborCost(),
                order.getTax(),
                order.getTotal(),
                order.getDate()
        );
        return orderInfo;
    }

    public void displayOrders(List<Order> orders){
        for (Order order: orders
             ) {
            io.print(formatOrderInfo(order));
        }
    }

    public void displayExitBanner() {
        io.print("Good Bye!!!");
    }

    public void displayUnknownCommandBanner() {
        io.print("Unknown Command!!!");
    }

    public void displayErrorMessage(String errorMsg) {
        io.print("=== ERROR ===");
        io.print(errorMsg);
        io.print("");
    }



}
