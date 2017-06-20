package com.bertof.pitestremote;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class MainMenuActivity extends AppCompatActivity {

    private class MainMenuActivityException extends Exception {
        MainMenuActivityException(String message) {
            super(message);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        ListView listView = (ListView) findViewById(R.id.mainMenuViewListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {
                    Intent newActivity;

                    switch (position) {
                        case 0:
                            newActivity = new Intent(MainMenuActivity.this, ExecuteCommandActivity.class);
                            break;
                        case 1:
                            newActivity = new Intent(MainMenuActivity.this, GetScriptsActivity.class);
                            break;
                        default:
                            throw new MainMenuActivityException("Invalid position");
                    }

                    startActivity(newActivity);

                } catch (MainMenuActivityException e) {
                    Toast.makeText(MainMenuActivity.this, R.string.generic_error, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        });
    }

}
