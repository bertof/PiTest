package com.bertof.pitestremote;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.bertof.pitestremote.API_client.SendScript;

import org.apache.http.conn.HttpHostConnectException;

public class ExecuteScriptActivity extends AppCompatActivity {

    private class ExecuteScriptActivityException extends Exception {
        ExecuteScriptActivityException(String message) {
            super(message);
        }
    }

    TextView outputTextView;

    String hostname;
    Integer port;
    String token;
    String script;

    AsyncTask scriptTask;

    SharedPreferences sharedPreferences;

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        outputTextView.setText(savedInstanceState.getString("outputTextView"));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("outputTextView", outputTextView.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_execute_script);

        outputTextView = (TextView) findViewById(R.id.outputTextView);

        if (savedInstanceState == null) {
            try {

                sharedPreferences = getSharedPreferences(getString(R.string.preference_key_string), Context.MODE_PRIVATE);

                hostname = sharedPreferences.getString(getString(R.string.preference_hostname), null);
                port = sharedPreferences.getInt(getString(R.string.preference_port), -1);
                token = sharedPreferences.getString(getString(R.string.preference_token), null);
                script = getIntent().getStringExtra("script");

                Toast.makeText(this, "Executing " + script, Toast.LENGTH_SHORT).show();

                if (hostname == null || port < 0 || token == null) {
                    throw new ExecuteScriptActivityException("Can not load preferences");
                }

                scriptTask = new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... params) {

                        try {
                            return SendScript.sendScriptWithRawResponse(hostname, port, token, script);
                        } catch (HttpHostConnectException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ExecuteScriptActivity.this, R.string.connection_failed_error, Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        outputTextView.setText(s);
                    }
                }.execute();

            } catch (ExecuteScriptActivityException e) {
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
}
