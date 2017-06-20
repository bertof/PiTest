package com.bertof.pitestremote.API_client;

import android.util.Log;

import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by bertof on 18/06/17.
 */

public class ListScripts {

    class ListScriptsException extends Exception {
    }

    public class ListScriptsScripts {
        String script;
        ArrayList<String> calls;
        String description;

        public String getScript() {
            return script;
        }

        public ArrayList<String> getCalls() {
            return calls;
        }

        public String getDescription() {
            return description;
        }
    }

    private static final String SCRIPTS_PATH = "/scripts";

    public static ListScriptsScripts[] listScripts(String hostname, int port, String token) throws Exception {
        Gson gson = new Gson();

        String stringAnswer = listScriptsCall(hostname, port, token);

        ListScriptsScripts answer[] = gson.fromJson(stringAnswer, ListScriptsScripts[].class);

        return answer;
    }

    private static String listScriptsCall(String hostname, int port, String token) throws Exception {
        HttpClient httpClient = new DefaultHttpClient();

        URI target = new URI("http", null, hostname, port, SCRIPTS_PATH, "token=" + token, null);
        HttpGet request = new HttpGet(target);
        HttpResponse response = httpClient.execute(request);
        HttpEntity entity = response.getEntity();

        String responseString = EntityUtils.toString(entity, "UTF-8");

        Log.d("responseString", responseString);

        return responseString;
    }

}
