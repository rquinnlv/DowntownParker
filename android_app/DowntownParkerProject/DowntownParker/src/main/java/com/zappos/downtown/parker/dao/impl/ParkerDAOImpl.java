package com.zappos.downtown.parker.dao.impl;

import android.util.Log;

import com.zappos.downtown.parker.dao.ParkerDAO;
import com.zappos.downtown.parker.model.GarageInfo;
import com.zappos.downtown.parker.model.exception.DaoException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Implementation of the {@link ParkerDAO} that retrieves data from the web
 */
public class ParkerDAOImpl implements ParkerDAO {

    private final String url;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Define a {@link ParkerDAO} with a provided URL from which to retrieve information
     * @param url the URL from which to retrieve information
     */
    public ParkerDAOImpl(String url) {
        this.url = url;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GarageInfo getGarageInfo() throws DaoException {
        InputStream jsonInputStream;

        try {
            jsonInputStream = retrieveStream(url);
        } catch (IOException e) {
            String message = "Error for URL " + url;
            Log.w(getClass().getSimpleName(), message, e);
            throw new DaoException(message, e);
        }

        Reader reader = new InputStreamReader(jsonInputStream);

        try {
            return objectMapper.readValue(reader, GarageInfo.class);
        } catch (IOException e) {
            Log.w(getClass().getSimpleName(), e.getMessage(), e);
            throw new DaoException(e.getMessage(), e);
        }
    }

    /**
     * Retrieve an {@link InputStream} from a provided URL
     * @param url the URL from which to generate an {@link InputStream}
     * @return the {@link InputStream} providing data from the URL
     * @throws IOException if unable to read from the provided URL
     */
    private InputStream retrieveStream(String url) throws IOException {

        final DefaultHttpClient client = new DefaultHttpClient();
        final HttpGet getRequest = new HttpGet(url);

        HttpResponse getResponse = null;
        int statusCode = 0;
        try {
            getResponse = client.execute(getRequest);
            statusCode = getResponse.getStatusLine().getStatusCode();
        } catch(Throwable t) {
            Log.e(getClass().getSimpleName(), t.getMessage(), t);
        }

        if (statusCode != HttpStatus.SC_OK) {
            throw new IOException("Error " + statusCode + " for URL " + url);
        }

        HttpEntity getResponseEntity = getResponse.getEntity();
        return getResponseEntity.getContent();
    }

}
