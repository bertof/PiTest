package com.bertof.pitestremote;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bertof.pitestremote.API_client.SendScript;

import org.apache.http.conn.HttpHostConnectException;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

public class ExecuteScriptAirodumpScanActivity extends AppCompatActivity {

    private class ExecuteScriptAirodumpScanActivityException extends Exception {
        ExecuteScriptAirodumpScanActivityException(String message) {
            super(message);
        }
    }

    TextView outputTextView;
    ExpandableListView expandableListView;

    MyExpandableListAdapter expandableListAdapter;

    ArrayList<String> listHeaders = new ArrayList<>();
    HashMap<String, List<String>> listContent = new HashMap<>();

    String hostname;
    Integer port;
    String token;
    String script;

    AsyncTask<Void, Void, String> scriptTask;

    SharedPreferences sharedPreferences;

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        outputTextView.setText(savedInstanceState.getString("outputTextView"));
        listHeaders = savedInstanceState.getStringArrayList("headers");
        listContent = (HashMap<String, List<String>>) savedInstanceState.getSerializable("contents");

        Log.d("listHeaders", listHeaders.toString());
        Log.d("listContent", listContent.toString());

        expandableListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("outputTextView", outputTextView.getText().toString());
        outState.putStringArrayList("headers", listHeaders);
        outState.putSerializable("contents", listContent);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_execute_airodump_scan_script);

        outputTextView = (TextView) findViewById(R.id.outputTextView);
        expandableListView = (ExpandableListView) findViewById(R.id.expandableOutputList);

        expandableListAdapter = new MyExpandableListAdapter(this, listHeaders, listContent);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListAdapter.notifyDataSetChanged();

        if (savedInstanceState == null) {
            try {

                sharedPreferences = getSharedPreferences(getString(R.string.preference_key_string), Context.MODE_PRIVATE);

                hostname = sharedPreferences.getString(getString(R.string.preference_hostname), null);
                port = sharedPreferences.getInt(getString(R.string.preference_port), -1);
                token = sharedPreferences.getString(getString(R.string.preference_token), null);
                script = getIntent().getStringExtra("script");

                if (hostname == null || port < 0 || token == null) {
                    throw new ExecuteScriptAirodumpScanActivityException("Can not load preferences");
                }

                outputTextView.setText(outputTextView.getText() + "\n" + "Executing: " + script);

                scriptTask = new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... params) {

                        return executeScriptCall(hostname, port, token, script);
                    }

                    @Override
                    protected void onPostExecute(String s) {
//                        outputTextView.setText(s);

                        CSVReader reader = new CSVReader(new StringReader(s), ',');
                        ArrayList<ArrayList<String>> result1 = new ArrayList<>();
                        ArrayList<ArrayList<String>> result2 = new ArrayList<>();
                        String[] line;
                        boolean secondPart = false;
                        try {
                            while ((line = reader.readNext()) != null) {
                                ArrayList<String> arr = new ArrayList<>();
                                Collections.addAll(arr, line);
                                if (arr.get(0).equals("")) {
                                    secondPart = true;
                                    continue;
                                }
                                if (!secondPart) {
                                    result1.add(arr);
                                } else {
                                    result2.add(arr);
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        outputTextView.setVisibility(TextView.GONE);

                        result1.remove(0);

                        listHeaders.clear();
                        listContent.clear();
                        for (ArrayList<String> result1Line : result1) {
                            listHeaders.add(result1Line.get(13));
                            ArrayList<String> contents = new ArrayList<>();
                            contents.add("BSSID: " + result1Line.get(0));
                            contents.add("Channel: " + result1Line.get(3));
                            contents.add("Chipher: " + result1Line.get(5));
                            contents.add("Power: " + result1Line.get(8) + "dB");
                            listContent.put(listHeaders.get(listHeaders.size() - 1), contents);
                        }

                        expandableListAdapter.notifyDataSetChanged();

                    }
                }.execute();

            } catch (ExecuteScriptAirodumpScanActivityException e) {
                switch (e.getMessage()) {
                    case "Can not load preferences":
                        e.printStackTrace();
                        Toast.makeText(this, R.string.generic_error, Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        e.printStackTrace();
                }
            }
        }

    }

    private String executeScriptCall(String hostname, Integer port, String token, String script) {
        try {
            return SendScript.sendScriptWithRawResponse(hostname, port, token, script);
        } catch (SendScript.SendScriptException e) {
            switch (e.getMessage()) {
                case "Invalid token":
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ExecuteScriptAirodumpScanActivity.this, "Wrong token", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Intent newActivity = new Intent(ExecuteScriptAirodumpScanActivity.this, SetupConnectionActivity.class);
                    newActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(newActivity);

                    break;
                default:
                    e.printStackTrace();
                    break;
            }
        } catch (HttpHostConnectException e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ExecuteScriptAirodumpScanActivity.this, R.string.connection_failed_error, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
