package com.bertof.pitestremote;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.bertof.pitestremote.API_client.ConnectionTester;
import com.bertof.pitestremote.API_client.SendCommand;

import org.apache.http.conn.HttpHostConnectException;

public class ExecuteCommandActivity extends AppCompatActivity {

    class ExecuteCommandActivityException extends Exception {
        ExecuteCommandActivityException(String message) {
            super(message);
        }
    }

    EditText commandInputTextView;
    Button sendCommandButton;
    TextView outputTextView;

    String hostname;
    Integer port;
    String token;

    AsyncTask commandTask;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_execute_command);

        commandInputTextView = (EditText) findViewById(R.id.commandInputTextView);
        sendCommandButton = (Button) findViewById(R.id.sendButton);
        outputTextView = (TextView) findViewById(R.id.outputTextView);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        try {

            sharedPreferences = getSharedPreferences(getString(R.string.preference_key_string), Context.MODE_PRIVATE);

            hostname = sharedPreferences.getString(getString(R.string.preference_hostname), null);
            port = sharedPreferences.getInt(getString(R.string.preference_port), -1);
            token = sharedPreferences.getString(getString(R.string.preference_token), null);

            if (hostname == null || port < 0 || token == null) {
                throw new ExecuteCommandActivityException("Can not load preferences");
            }

        } catch (ExecuteCommandActivityException e) {
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

    public void sendButton_onClick(View v) {
        try {

            if (hostname == null || port < 0 || token == null) {
                throw new ExecuteCommandActivityException("Can not load preferences");
            }

            final String command = commandInputTextView.getText().toString().trim();

            if (command.isEmpty()) {
                throw new ExecuteCommandActivityException("Empty command");
            }

            if (commandTask != null) {
                commandTask.cancel(true);
            }

            commandTask = new AsyncTask<Void, Void, String>() {

                @Override
                protected String doInBackground(Void... params) {
                    try {
                        return SendCommand.sendCommandWithRawResponse(hostname, port, token, command);
                    } catch (HttpHostConnectException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ExecuteCommandActivity.this, R.string.connection_failed_error, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPreExecute() {
                    sendCommandButton.setEnabled(false);
                }

                @Override
                protected void onPostExecute(String s) {
                    outputTextView.setText(s);
                    sendCommandButton.setEnabled(true);
                }

            }.execute();

        } catch (ExecuteCommandActivityException e) {
            switch (e.getMessage()) {
                case "Can not load preferences":
                    e.printStackTrace();
                    Toast.makeText(this, R.string.generic_error, Toast.LENGTH_SHORT).show();
                    break;
                case "Empty command":
                    Toast.makeText(this, R.string.empty_command_error, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    e.printStackTrace();
            }
        }

    }
}
