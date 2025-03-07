package com.wiley.dao;

import com.wiley.model.Tax;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TaxDaoStubImpl implements TaxDao{
    public Tax onlyTax;
     public TaxDaoStubImpl() {
         onlyTax = new Tax();
         onlyTax.setStateAbbreviation("CA");
         onlyTax.setStateName("California");
         onlyTax.setTaxRate(new BigDecimal("25.00"));
     }
    public TaxDaoStubImpl(Tax onlyTax) {
         this.onlyTax = onlyTax;
    }

    @Override
    public Tax geByStateAbbreviation(String taxAbbreviation) throws OrderDataPersistenceException {
         if (taxAbbreviation.equals(onlyTax.getStateAbbreviation())){
             return onlyTax;
         } else {
             return null;
         }
    }

    @Override
    public Set<String> getAllStates() throws OrderDataPersistenceException {
        Set<String> states = new HashSet<>();
        states.add(onlyTax.getStateAbbreviation());
        return states;
    }
}
