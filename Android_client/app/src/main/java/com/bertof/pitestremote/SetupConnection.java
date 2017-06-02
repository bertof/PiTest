package com.bertof.pitestremote;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bertof.pitestremote.API_client.ConnectionTester;

import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;

public class SetupConnection extends AppCompatActivity {

    private EditText urlFieldEditText;
    private EditText portFiledEditText;
    private Button connectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_connection);

        urlFieldEditText = (EditText) findViewById(R.id.hostnameTextEdit);
        portFiledEditText = (EditText) findViewById(R.id.portTextEdit);
        connectButton = (Button) findViewById(R.id.connectButton);

    }


    public void connectButton_TestConnection(View v) throws MalformedURLException {

        URL target = new URL("http", urlFieldEditText.getText().toString(), Integer.parseInt(portFiledEditText.getText().toString()), "/");

        new AsyncTask<URL, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(URL... target) {
                return ConnectionTester.testConnection(target[0]);
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                if (aBoolean) {
                    Toast.makeText(SetupConnection.this, "Connection successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SetupConnection.this, "Something is wrong", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute(target);
    }

}
