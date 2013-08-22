package com.zappos.downtown.parker.dao;

import com.zappos.downtown.parker.model.GarageInfo;
import com.zappos.downtown.parker.model.exception.DaoException;

/**
 * This DAO provides the contract for reading garage information from a data source
 */
public interface ParkerDAO {

    /**
     * Retrieve {@link GarageInfo} from a data source
     * @return the {@link GarageInfo} provided from the data source
     * @throws DaoException if unable to retrieve the information
     */
    GarageInfo getGarageInfo() throws DaoException;
}
