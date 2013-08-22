package com.zappos.downtown.parker.model.exception;

/**
 * An exception coming from a DAO
 */
public class DaoException extends Exception {

    public DaoException() {
    }

    public DaoException(String detailMessage) {
        super(detailMessage);
    }

    public DaoException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public DaoException(Throwable throwable) {
        super(throwable);
    }

}
