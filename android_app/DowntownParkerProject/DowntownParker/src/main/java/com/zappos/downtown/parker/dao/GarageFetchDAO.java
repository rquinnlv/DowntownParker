package com.zappos.downtown.parker.dao;

import com.zappos.downtown.parker.model.GarageData;
import com.zappos.downtown.parker.model.exception.DaoException;

/**
 * This DAO provides the contract for reading garage information from a data source
 */
public interface GarageFetchDAO {

    /**
     * Retrieve {@link com.zappos.downtown.parker.model.GarageData} from a data source
     * @return the {@link com.zappos.downtown.parker.model.GarageData} provided from the data source
     * @throws DaoException if unable to retrieve the information
     */
    GarageData getGarageInfo() throws DaoException;
}
