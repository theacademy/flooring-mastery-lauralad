package com.wiley.dao;

import com.wiley.model.Tax;

import java.util.Set;

public interface TaxDao {
    /**
     * Returns the tax object associated with the given tax state abbreviation.
     * Returns null if no such tax exists
     *
     * @param taxAbbreviation of the product to retrieve
     * @return the tax object associated with the given tax state abbreviation,
     * null if no such product exists
     */
    Tax geByStateAbbreviation(String taxAbbreviation) throws OrderDataPersistenceException;
    /**
     * Returns the set of all state abbreviations in the file
     *
     * @return the set of all state abbreviations in the file
     */
    Set<String> getAllStates() throws OrderDataPersistenceException;
}
