package com.bertof.pitestremote;

import android.content.Context;
import android.content.Intent;
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

            outputTextView.setText(outputTextView.getText() + "\n" + "Executing: " + command);

            commandTask = new AsyncTask<Void, Void, String>() {

                @Override
                protected String doInBackground(Void... params) {
                    return executeCommandCall(hostname, port, token, command);
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

    public String executeCommandCall(String hostname, int port, String token, String command) {
        try {
            return SendCommand.sendCommandWithRawResponse(hostname, port, token, command);
        } catch (SendCommand.SendCommandException e) {
            switch (e.getMessage()) {
                case "Invalid token":
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ExecuteCommandActivity.this, "Wrong token", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Intent newActivity = new Intent(ExecuteCommandActivity.this, SetupConnectionActivity.class);
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
                    Toast.makeText(ExecuteCommandActivity.this, R.string.connection_failed_error, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
