package com.bertof.pitestremote.API_client;

import android.os.Build;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by pily on 01/06/17.
 */

public class ConnectionTester {

    private static final int CONNECTION_TIMEOUT = 2000;
    private static final int DATA_RETRIEVAL_TIMEOUT = 2000;
    private static final String SERVER_WELCOME_MESSAGE = "PiTest server running";

    /**
     * Test if the server is running at the addres-port given
     *
     * @param url complete url of the server
     * @return true if connection successful, false otherwise
     */
    public static boolean testConnection(URL url) {
        disableConnectionReuseIfNecessary();

        HttpURLConnection urlConnection = null;
        try {

            //Create connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
            urlConnection.setReadTimeout(DATA_RETRIEVAL_TIMEOUT);


            //Handle issues
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                //Handle unauthorization
            } else if (statusCode != HttpURLConnection.HTTP_OK) {
                //Handle any other error
            }

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String result = new Scanner(in).useDelimiter("\\A").next();

            return result.matches(SERVER_WELCOME_MESSAGE);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return false;
    }

    private static void disableConnectionReuseIfNecessary() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");
        }
    }
}
