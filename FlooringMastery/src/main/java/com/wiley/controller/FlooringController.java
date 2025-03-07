package com.wiley.controller;

import com.wiley.dao.OrderDataPersistenceException;
import com.wiley.model.Order;
import com.wiley.model.Product;
import com.wiley.model.Tax;
import com.wiley.service.FlooringService;
import com.wiley.service.OrderInformationInvalidException;
import com.wiley.service.TaxInformationInvalidException;
import com.wiley.view.FlooringView;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Set;

public class FlooringController {
    private FlooringView view;
    private FlooringService service;

    public FlooringController(FlooringView view, FlooringService service) {
        this.view = view;
        this.service = service;
    }

    public void run(){
        boolean keepGoing = true;
        int menuSelection = 0;
        //initialize the maxId with pre-existing files
        initializeService();

        while (keepGoing){
            try {
                menuSelection = menuSelection();
                switch (menuSelection){
                    case 1:
                        displayOrders();
                        break;
                    case 2:
                        addOrder();
                        break;
                    case 3:
                        editOrder();
                        break;
                    case 4:
                        removeOrder();
                        break;
                    case 5:
                        exportAllData();
                        break;
                    case 6:
                        keepGoing = false;
                        break;
                    default:
                        unknownCommand();
                }
            }catch (OrderDataPersistenceException e) {
                view.displayErrorMessage(e.getMessage());
            }

        }
        quit();
    }

    private int menuSelection(){
        return view.menuSelection();
    }
    private void initializeService() {service.init();}
    private void displayOrders() throws OrderDataPersistenceException {
        view.displayOrdersBanner();
        LocalDate date = view.askDate();
        List<Order> orders = service.getAllOrdersForDate(date);
        view.displayOrders(orders);
    }
    private void addOrder() throws OrderDataPersistenceException {
        //add order banner
        view.askOrderBanner();

        LocalDate date = addOrderAskDate();
        String customerName = addOrderAskName();
        String state = addOrderAskState(service.getAllStates());
        String productType = addOrderAskProduct();
        BigDecimal area = addOrderAskArea();

        Order order = service.createOrderAndAssignOrderNumber();
        order.setDate(date);
        order.setCustomerName(customerName);
        order.setState(state);
        order.setProductType(productType);
        order.setArea(area);

        Tax tax = service.geByStateAbbreviation(state);
        order.setTaxRate(tax.getTaxRate());

        Product product = service.getByProductType(productType);
        order.setCostPerSquareFoot(product.getCostPerSquareFoot());
        order.setLaborCostPerSquareFoot(product.getLaborCostPerSquareFoot());

        order.setMaterialCost(service.calculateMaterialCost(order));
        order.setLaborCost(service.calculateLaborCost(order));
        order.setTax(service.calculateTax(order));
        order.setTotal(service.calculateTotal(order));


        Order addedOrder = service.addOrder(order, date);
        if (addedOrder == null) {
            view.addOrderSuccessBanner();
        }


    }
    // helper methods for addOrder
    private LocalDate addOrderAskDate(){
        //ask for date until it's valid
        boolean hasErrors = false;
        LocalDate date;
        do {
            //askDate() also checks for date format validity
            date = view.askDate();
            try {
                service.validateDateInformation(date);
                hasErrors = false;
            } catch (OrderInformationInvalidException e) {
                //date format is not valid, need to re-ask
                view.displayErrorMessage(e.getMessage());
                hasErrors = true;
            }

        }while (hasErrors);
        return date;
    }
    private String addOrderAskName() {
        //ask for name until it's valid
        boolean hasErrors = false;
        String newName;
        do {
            newName = view.askCustomerName();
            //check if user did not just hit Enter i.e. wrote a value
            if (!newName.trim().isEmpty()){
                try {
                    service.validateCustomerNameInformation(newName);
                    hasErrors = false;
                } catch (OrderInformationInvalidException e) {
                    //name format is not valid, need to re-ask
                    view.displayErrorMessage(e.getMessage());
                    hasErrors = true;
                }
            }
        }while (hasErrors);
        return newName;
    }
    private String addOrderAskState(Set<String> states) throws OrderDataPersistenceException {
        //ask user for state until it's valid
        boolean hasErrors = false;
        String newState;
        do {
            newState = view.askState(states);
            //check if user did not just hit Enter i.e. wrote a value
            if (!newState.trim().isEmpty()){
                try {
                    service.validateStateInformation(newState);
                    hasErrors = false;
                } catch (TaxInformationInvalidException e) {
                    //tax format is not valid, need to re-ask
                    view.displayErrorMessage(e.getMessage());
                    hasErrors = true;
                }

            } else {
                //hit enter not allowed here
                hasErrors = true;
            }
        }while (hasErrors);

        return newState;
    }
    private String addOrderAskProduct() throws OrderDataPersistenceException {
        //ask user for product until it's valid
        List<String> productTypes = service.getAllProductTypes();
        view.printProductList(productTypes);
        int newProductTypeIndex;

        newProductTypeIndex = view.askProductType(productTypes.size());
        String newProductType;

        //it's already validated since user is not allowed to choose non-existing product option
        newProductType = productTypes.get(newProductTypeIndex-1);

        return newProductType;
    }
    private BigDecimal addOrderAskArea(){
        //ask user for area until it's valid
        String newArea;
        BigDecimal newAreaDecimal;
        boolean hasErrors= false;

        do {
            newArea = view.askArea();
            newAreaDecimal = new BigDecimal(newArea);
            try {
                service.validateAreaInformation(newAreaDecimal);
                hasErrors = false;
            }catch (OrderInformationInvalidException e){
                // loop again until input is valid
                view.displayErrorMessage(e.getMessage());
                hasErrors = true;
            }

        }while (hasErrors);
        return newAreaDecimal;
    }


    // helper methods for editOrder
    private String editOrderAskName(String prevName) {
        // returns either previousName or newName
        boolean hasErrors = false;
        String newName;
        do {
            newName = view.askNewCustomerName(prevName);
            //check if user did not just hit Enter i.e. wrote a value
            if (!newName.trim().isEmpty()){
                try {
                    service.validateCustomerNameInformation(newName);
                    hasErrors = false;
                } catch (OrderInformationInvalidException e) {
                    //name format is not valid, need to re-ask
                    view.displayErrorMessage(e.getMessage());
                    hasErrors = true;
                }

            } else {
                //enter is allowed, no change
                newName = prevName;
                hasErrors = false;
            }
        }while (hasErrors);

        return newName;
    }

    private String editOrderAskState(Set<String> states, String state) throws OrderDataPersistenceException{
        //returns new or previous state
        boolean hasErrors = false;
        String newState;
        do {
            newState = view.askNewState(states);
            //check if user did not just hit Enter i.e. wrote a value
            if (!newState.trim().isEmpty()){
                try {
                    service.validateStateInformation(newState);
                    hasErrors = false;
                } catch (TaxInformationInvalidException e) {
                    //tax format is not valid, need to re-ask
                    view.displayErrorMessage(e.getMessage());
                    hasErrors = true;
                }

            } else {
                //there has been no change, no need to save anything
                newState = state;
                hasErrors = false;
            }
        }while (hasErrors);

        return newState;
    }

    private String editOrderAskProduct(String prevProduct) throws OrderDataPersistenceException {
        //returns new or previous product
        List<String> productTypes = service.getAllProductTypes();
        view.printProductList(productTypes);
        int newProductTypeIndex;

        newProductTypeIndex = view.askNewProductType(prevProduct, productTypes.size());
        String newProductType;
        // if user hit Enter ==> index is 0
        if (newProductTypeIndex == 0){
            newProductType = prevProduct;
        }
        else {
            //it's already validated since user is not allowed to choose non-existing product option besides Enter/0
            newProductType = productTypes.get(newProductTypeIndex - 1);
        }
        return newProductType;
    }

    private BigDecimal editOrderAskArea(String prevArea){
        //returns either prev or new area
        String newArea;
        BigDecimal newAreaDecimal;
        boolean hasErrors= false;

        do {
            newArea = view.askNewArea(prevArea);
            // if user didn't hit Enter, validate the new input
            if (!newArea.isEmpty()){
                newAreaDecimal = new BigDecimal(newArea);
                try {
                    service.validateAreaInformation(newAreaDecimal);
                    hasErrors = false;
                }catch (OrderInformationInvalidException e){
                    // loop again until input is valid
                    view.displayErrorMessage(e.getMessage());
                    hasErrors = true;
                }
            } else{
                newAreaDecimal = new BigDecimal(prevArea);
                hasErrors = false;
            }
        }while (hasErrors);

        return newAreaDecimal;
    }

    private void editOrder() throws OrderDataPersistenceException {
        view.editOrderBanner();
        //checks if it's valid date format
        LocalDate date = view.askDate();
        int orderNumber = view.askOrderNumber();
        //retrieve the existing order
        Order previousOrder = service.getOrder(orderNumber, date);

        Order newOrder = new Order(orderNumber);
        newOrder.setDate(date);

        boolean isChangeMade = false;
        // we compare previous with new just in case user actually enters a valid input that
        // is the same as before instead of just hitting enter, which means no change as well

        // EDIT CUSTOMER NAME
        String newName = editOrderAskName(previousOrder.getCustomerName());
        newOrder.setCustomerName(newName);

        //EDIT STATE
        //returns either new or previous state
        String newState = editOrderAskState(service.getAllStates(), previousOrder.getState());
        newOrder.setState(newState);
        if (!newState.equals(previousOrder.getState())){
            isChangeMade = true;
            Tax tax = service.geByStateAbbreviation(newState);
            newOrder.setTaxRate(tax.getTaxRate());
        } else {
            newOrder.setTaxRate(previousOrder.getTaxRate());
        }


        //EDIT PRODUCT TYPE
        //returns either new or previous product type
        String newProductType = editOrderAskProduct(previousOrder.getProductType());
        newOrder.setProductType(newProductType);
        if (!newProductType.equals(previousOrder.getProductType())){
            isChangeMade = true;
            Product product = service.getByProductType(newProductType);
            newOrder.setCostPerSquareFoot(product.getCostPerSquareFoot());
            newOrder.setLaborCostPerSquareFoot(product.getLaborCostPerSquareFoot());
        } else {
            newOrder.setCostPerSquareFoot(previousOrder.getCostPerSquareFoot());
            newOrder.setLaborCostPerSquareFoot(previousOrder.getLaborCostPerSquareFoot());
        }


        //EDIT AREA
        //returns either new or previous area
        BigDecimal newArea = editOrderAskArea(previousOrder.getArea().toString());
        newOrder.setArea(newArea);
        if (newArea.compareTo(previousOrder.getArea()) != 0){
            isChangeMade = true;
        }

        // Add date field as well
        newOrder.setDate(date);

        // RECALCULATE COSTS IF CHANGES HAVE BEEN MADE (EXCEPT NAME)
        if (isChangeMade) {
            newOrder.setMaterialCost(service.calculateMaterialCost(newOrder));
            newOrder.setLaborCost(service.calculateLaborCost(newOrder));
            newOrder.setTax(service.calculateTax(newOrder));
            newOrder.setTotal(service.calculateTotal(newOrder));
        } else {
            newOrder.setMaterialCost(previousOrder.getMaterialCost());
            newOrder.setLaborCost(previousOrder.getLaborCost());
            newOrder.setTax(previousOrder.getTax());
            newOrder.setTotal(previousOrder.getTotal());
        }

        // DISPLAY ORDER AND CONFIRM
        view.displayOrder(newOrder);
        if(view.doesUserConfirm()){
            service.editOrder(date, newOrder);
            view.editOrderSuccessBanner();
        }


    }
    private void removeOrder() throws OrderDataPersistenceException {
        view.removeOrderBanner();
        LocalDate date = view.askDate();
        int orderNumber = view.askOrderNumber();

        //if order not found, displays error message and goes back to main menu
        Order retrievedOrder = service.getOrder(orderNumber, date);

        //display order details and ask to confirm
        view.displayOrder(retrievedOrder);

        if (view.doesUserConfirm()){
            Order removedOrder = service.removeOrder(orderNumber, date);
            //if there was an order removed, display success
            if (removedOrder != null){
                view.removeOrderSuccessBanner();
            }
        }
    }
    private void exportAllData() throws OrderDataPersistenceException {
        view.exportAllDataBanner();
        service.exportAllData();
        view.exportAllDataSuccessBanner();
    }
    private void unknownCommand(){
        view.displayUnknownCommandBanner();
    }

    private void quit(){
        view.displayExitBanner();
    }
}
