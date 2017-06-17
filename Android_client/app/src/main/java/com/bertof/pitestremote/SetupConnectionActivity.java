package com.bertof.pitestremote;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bertof.pitestremote.API_client.AuthenticationTester;
import com.bertof.pitestremote.API_client.ConnectionTester;

import org.apache.http.conn.HttpHostConnectException;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.Set;

public class SetupConnectionActivity extends AppCompatActivity {

    class SetupConnectionException extends Exception {
        public SetupConnectionException(String message) {
            super(message);
        }
    }

    EditText hostnameFieldEditText;
    EditText portFiledEditText;
    EditText tokenFieldEditText;
    Button connectButton;

    AsyncTask connectionTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_connection);

        hostnameFieldEditText = (EditText) findViewById(R.id.hostnameTextEdit);
        portFiledEditText = (EditText) findViewById(R.id.portTextEdit);
        tokenFieldEditText = (EditText) findViewById(R.id.tokenTextEdit);
        connectButton = (Button) findViewById(R.id.connectButton);

    }


    public void connectButton_test_connection_and_authentication(View v) {

        try {

            if (hostnameFieldEditText.getText().toString().isEmpty()) {
                throw new SetupConnectionException("Empty hostname");
            }
            if (portFiledEditText.getText().toString().isEmpty()) {
                throw new SetupConnectionException("Empty port");
            }

            final String hostname = hostnameFieldEditText.getText().toString();
            final Integer port = Integer.parseInt(portFiledEditText.getText().toString());
            final String token = tokenFieldEditText.getText().toString();

            if (port < 0) {
                throw new SetupConnectionException("Negative port");
            }

            /*if (connectionTask.getStatus() == AsyncTask.Status.RUNNING || connectionTask.getStatus() == AsyncTask.Status.PENDING) {
                connectionTask.cancel(true);
            }*/

            if (connectionTask != null) {
                connectionTask.cancel(true);
            }

            connectionTask = new AsyncTask<Void, Void, Boolean>() {

                @NonNull
                @Override
                protected Boolean doInBackground(Void... params) {

                    try {
                        if (!ConnectionTester.testConnection(hostname, port)) {
                            throw new SetupConnectionException("Connection failed");
                        }

                        if (!AuthenticationTester.testAuthentication(hostname, port, token)) {
                            throw new SetupConnectionException("Wrong token");
                        }

                        //TODO save settings

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SetupConnectionActivity.this, R.string.connection_succeeded, Toast.LENGTH_SHORT).show();
                                Intent startMainMenuIntent = new Intent(SetupConnectionActivity.this, MainMenuActivity.class);
                                startActivity(startMainMenuIntent);
                            }
                        });

                    } catch (SetupConnectionException e) {
                        switch (e.getMessage()) {
                            case "Connection failed":
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(SetupConnectionActivity.this, R.string.connection_failed_error, Toast.LENGTH_SHORT).show();
                                    }
                                });
                                break;
                            case "Wrong token":
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(SetupConnectionActivity.this, R.string.wrong_token_error, Toast.LENGTH_SHORT).show();
                                    }
                                });
                                break;
                            default:
                                e.printStackTrace();

                        }
                    } catch (HttpHostConnectException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SetupConnectionActivity.this, R.string.connection_refused_error, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (@NonNull URISyntaxException | UnknownHostException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SetupConnectionActivity.this, R.string.invalid_uri_error, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return true;

                }

                @Override
                protected void onPostExecute(Boolean aBoolean) {

                }
            }.execute();

        } catch (SetupConnectionException e) {
            switch (e.getMessage()) {
                case "Empty hostname":
                    Toast.makeText(this, R.string.empty_hostname_error, Toast.LENGTH_SHORT).show();
                    break;
                case "Empty port":
                    Toast.makeText(this, R.string.empty_port_error, Toast.LENGTH_SHORT).show();
                    break;
                case "Negative port":
                    Toast.makeText(this, R.string.negative_port_error, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    e.printStackTrace();
                    break;
            }
        }

    }

}
