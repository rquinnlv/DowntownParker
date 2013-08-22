package com.zappos.downtown.parker.dao.impl;

import android.util.Log;

import com.zappos.downtown.parker.ParkingApp;
import com.zappos.downtown.parker.R;
import com.zappos.downtown.parker.dao.GarageFetchDAO;
import com.zappos.downtown.parker.model.GarageData;
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

import android.content.res.Resources;

/**
 * Implementation of the {@link com.zappos.downtown.parker.dao.GarageFetchDAO} that retrieves data from the web
 */
public class GarageFetchDAOImpl implements GarageFetchDAO {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * {@inheritDoc}
     */
    @Override
    public GarageData getGarageInfo() throws DaoException {

        final String url = ParkingApp.getContext().getString(R.string.parker_json_endpoint);

        InputStream jsonInputStream;

        try {
            jsonInputStream = retrieveStream(url);
        } catch (IOException e) {
            final String message = "Error for URL " + url + ": " + e.getMessage();
            Log.w(getClass().getSimpleName(), message, e);
            throw new DaoException(message, e);
        }

        Reader reader = new InputStreamReader(jsonInputStream);

        try {
            return objectMapper.readValue(reader, GarageData.class);
        } catch (IOException e) {
            final String message = "Error for URL " + url + ": " + e.getMessage();
            Log.w(getClass().getSimpleName(), message, e);
            throw new DaoException(message, e);
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

        final HttpResponse getResponse = client.execute(getRequest);
        final int statusCode = getResponse.getStatusLine().getStatusCode();

        if (statusCode != HttpStatus.SC_OK) {
            throw new IOException("Error " + statusCode + " for URL " + url);
        }

        return getResponse.getEntity().getContent();
    }

}
