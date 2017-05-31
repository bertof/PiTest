package com.bertof.pitestremote;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Scanner;

public class SetupConnection extends AppCompatActivity {

    private static final int CONNECTION_TIMEOUT = 2000;
    private static final int DATA_RETRIEVAL_TIMEOUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setup_connection);
    }

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
            System.out.println(result);

            return true;

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
