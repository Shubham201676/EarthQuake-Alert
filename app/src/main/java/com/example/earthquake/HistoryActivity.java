package com.example.earthquake;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    List<Task>taskList;
    ListView lv3;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    CAdpter cAdpter;
    ArrayList<String>itemList = new ArrayList<>();
    String currentApi = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getAll all = new getAll();
        all.execute();
        lv3=(ListView)findViewById(R.id.lv3);

        cAdpter=new CAdpter(getApplicationContext(),itemList);
        lv3.setAdapter(cAdpter);
        lv3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                saveCurrentApi(itemList.get(position));

            }


        });
        preferences=getApplicationContext().getSharedPreferences("myPref",0);
        editor=preferences.edit();

    }
    class getAll extends AsyncTask<String,Void, List<Task>>{
        @Override
        protected void onPostExecute(List<Task> tasks) {

            try {

                int s = tasks.size();
                JSONArray array = new JSONArray();
                itemList.clear();
                for (int i = 0; i < s; i++) {
                    Task task = tasks.get(i);
                      // String st = array.getString(i);
//                    JSONObject object = new JSONObject(st);

                    itemList.add(task.getName());
                    Collections.reverse(itemList);
                    cAdpter.notifyDataSetChanged();

                }
            }catch (Exception e){

            }

            //Toast.makeText(getApplicationContext(),itemList.get(0),Toast.LENGTH_LONG).show();
        }

        @Override
        protected List<Task> doInBackground(String... strings) {
            List<Task>taskList = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().taskDao().getAll();

            return taskList;
        }
    }
    public void saveCurrentApi(String api){
        currentApi = api;
        editor.putString("api",currentApi).commit();
        finish();
    }

    class apicheck extends AsyncTask<String,Void, List<Task>>{
        @Override
        protected List<Task> doInBackground(String... strings) {
          //  editor.putString("api",currentApi).commit();
            return null;
        }

        @Override
        protected void onPostExecute(List<Task> tasks) {

            //Toast.makeText(getApplicationContext(),itemList.get(0),Toast.LENGTH_LONG).show();
        }

    }}
