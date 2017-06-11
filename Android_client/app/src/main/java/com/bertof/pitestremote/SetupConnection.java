package com.bertof.pitestremote;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.net.MalformedURLException;
import java.net.URL;

public class SetupConnection extends AppCompatActivity {

    EditText hostnameFieldEditText;
    EditText portFiledEditText;
    EditText tokenFieldEditText;
    Button connectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_connection);

        hostnameFieldEditText = (EditText) findViewById(R.id.hostnameTextEdit);
        portFiledEditText = (EditText) findViewById(R.id.portTextEdit);
        tokenFieldEditText = (EditText) findViewById(R.id.tokenTextEdit);
        connectButton = (Button) findViewById(R.id.connectButton);

        if (savedInstanceState != null) {
            hostnameFieldEditText.setText(savedInstanceState.getString("host"));
            portFiledEditText.setText(savedInstanceState.getString("port"));
            tokenFieldEditText.setText(savedInstanceState.getString("token"));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("host", hostnameFieldEditText.getText().toString());
        outState.putString("port", portFiledEditText.getText().toString());
        outState.putString("token", tokenFieldEditText.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        hostnameFieldEditText.setText(savedInstanceState.getString("host"));
        portFiledEditText.setText(savedInstanceState.getInt("port"));
        tokenFieldEditText.setText(savedInstanceState.getString("token"));
    }

    public void connectButton_TestConnection(View v) throws MalformedURLException {

        final String hostname = hostnameFieldEditText.getText().toString();
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

                        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();

                        editor.putString("host", hostname);
                        editor.putInt("port", port);
                        editor.putString("token", token);

                        editor.apply();

                        Intent intent = new Intent(getApplicationContext(), ChooseActionActivity.class);
                        Log.d("INTENT", intent.toString());
                        startActivity(intent);


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
