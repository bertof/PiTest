package com.bertof.pitestremote;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.bertof.pitestremote.API_client.ActionsLoader;
import com.bertof.pitestremote.Object.CommandItem;
import com.bertof.pitestremote.R;

import java.util.ArrayList;

public class SelectActionActivity extends AppCompatActivity {

    String token;
    String host;
    Integer port;


    ArrayList<String> listItems = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_action_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.item_list);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItems);

        listView.setAdapter(adapter);

//        SharedPreferences sharedPref = getSharedPreferences("pitest_host", Context.MODE_PRIVATE);

//        token = sharedPref.getString("token", null);
//        host = sharedPref.getString("hostname", null);
//        port = sharedPref.getInt("port", -1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.select_action_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.action_refresh:
                Toast.makeText(this, "Now should refresh", Toast.LENGTH_SHORT).show();
//                refreshList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

/*
    public void refreshList() {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected void onPostExecute(String jsonDefinition) {
                ArrayList<CommandItem> commandItems = CommandItem.fromJSON(jsonDefinition);

                for (CommandItem command :
                        commandItems) {
                    Log.d("COMMAND", command.getCommand());
                    listItems.add(command.getCommand());
                    adapter.notifyDataSetChanged();
                }

                super.onPostExecute(jsonDefinition);
            }

            @Override
            protected String doInBackground(Void... params) {
                return ActionsLoader.getCommands(host, port, token);
            }
        }.execute();
    }
*/

}
