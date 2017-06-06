package com.bertof.pitestremote;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bertof.pitestremote.API_client.AuthenticationTester;
import com.bertof.pitestremote.API_client.ConnectionTester;

import java.net.ConnectException;
import java.net.InterfaceAddress;
import java.net.MalformedURLException;
import java.net.URL;

public class SetupConnection extends AppCompatActivity {

    private EditText urlFieldEditText;
    private EditText portFiledEditText;
    private EditText tokenFieldEditText;
    private Button connectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_connection);

        urlFieldEditText = (EditText) findViewById(R.id.hostnameTextEdit);
        portFiledEditText = (EditText) findViewById(R.id.portTextEdit);
        tokenFieldEditText = (EditText) findViewById(R.id.tokenTextEdit);
        connectButton = (Button) findViewById(R.id.connectButton);

    }


    public void connectButton_TestConnection(View v) throws MalformedURLException {


        final String hostname = urlFieldEditText.getText().toString();
        final Integer port = Integer.parseInt(portFiledEditText.getText().toString());
        final String token = tokenFieldEditText.getText().toString();

        (new AsyncTask<String, String, Integer>() {

            @Override
            protected Integer doInBackground(String... params) {
                try {
                    URL target = new URL("http", hostname, port, "/");

                    Boolean result = ConnectionTester.testConnection(target);

                    if (!result) {
                        return 1;
                    }

                    result = AuthenticationTester.testAuthentication(hostname, port, token);

//                    result = ConnectionTester.testConnection(target);

                    if (!result) {
                        return 2;
                    }

                    return 0;

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                return 3;
            }

            @Override
            protected void onPostExecute(Integer integer) {
                switch (integer) {
                    case 0:
                        Toast.makeText(SetupConnection.this, "Token ok", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(SetupConnection.this, "Wrong hostname or port", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(SetupConnection.this, "Wrong token", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(SetupConnection.this, "Something is wrong", Toast.LENGTH_SHORT).show();
                }
            }
        }).execute();
    }

}
