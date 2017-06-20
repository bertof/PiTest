package com.bertof.pitestremote;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bertof.pitestremote.API_client.ListScripts;

import java.util.ArrayList;

public class GetScriptsActivity extends ListActivity {

    class GetScriptsActivityException extends Exception {
        public GetScriptsActivityException(String message) {
            super(message);
        }
    }

    ListView scriptsListView;

    ArrayList<String> scriptListDescriptionArray = new ArrayList<>();
    ArrayList<String> scriptListCodeArray = new ArrayList<>();

    ArrayAdapter<String> scriptListAdapter;

    String hostname;
    Integer port;
    String token;

    AsyncTask updateListTask;

    SharedPreferences sharedPreferences;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.get_scripts_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_scripts);

        scriptsListView = getListView();

        scriptListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, scriptListDescriptionArray);

        setListAdapter(scriptListAdapter);

        scriptsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent newActivity;

                switch (scriptListCodeArray.get(position)) {
                    case "airodump_scan":
                        newActivity = new Intent(GetScriptsActivity.this, ExecuteScriptAirodumpScanActivity.class);
                        break;
                    default:
                        newActivity = new Intent(GetScriptsActivity.this, ExecuteScriptActivity.class);
                        break;
                }

                String scriptName = scriptListCodeArray.get(position);
                newActivity.putExtra("script", scriptName);

                startActivity(newActivity);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateList();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        try {

            sharedPreferences = getSharedPreferences(getString(R.string.preference_key_string), Context.MODE_PRIVATE);

            hostname = sharedPreferences.getString(getString(R.string.preference_hostname), null);
            port = sharedPreferences.getInt(getString(R.string.preference_port), -1);
            token = sharedPreferences.getString(getString(R.string.preference_token), null);

            if (hostname == null || port < 0 || token == null) {
                throw new GetScriptsActivityException("Can not load preferences");
            }

        } catch (GetScriptsActivityException e) {
            switch (e.getMessage()) {
                case "Can not load preferences":
                    e.printStackTrace();
                    Toast.makeText(this, R.string.generic_error, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    e.printStackTrace();
            }
        }

        super.onPostCreate(savedInstanceState);
    }

    public void updateListOnClick(MenuItem item) {
        updateList();
    }

    private void updateList() {

        Log.d("updateList", "updateList called");

        updateListTask = new AsyncTask<Void, Void, ListScripts.ListScriptsScripts[]>() {
            @Override
            protected void onPostExecute(ListScripts.ListScriptsScripts[] listScriptsScripts) {

                scriptListDescriptionArray.clear();
                scriptListCodeArray.clear();

                for (ListScripts.ListScriptsScripts script : listScriptsScripts) {
                    scriptListDescriptionArray.add(script.getScript() + ": " + script.getDescription());
                    scriptListCodeArray.add(script.getScript());
                }
                scriptListAdapter.notifyDataSetChanged();
            }

            @Override
            protected ListScripts.ListScriptsScripts[] doInBackground(Void... params) {
                try {
                    return ListScripts.listScripts(hostname, port, token);

                } catch (Exception e) {
                    e.printStackTrace();
                    this.cancel(true);
                }
                return new ListScripts.ListScriptsScripts[]{};
            }
        }.execute();
    }
}
