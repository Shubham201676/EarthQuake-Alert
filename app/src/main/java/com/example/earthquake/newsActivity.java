package com.example.earthquake;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Fragment;

import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class newsActivity extends Fragment {
    ArrayList<String>dataList=new ArrayList<>();
    ListView lv;
    newAdapter adapter;
    private Button btn;
    BottomNavigationView bottomNavigationView;
    SwipeRefreshLayout swipe;
    ProgressBar pBar;
    private RequestQueue mQueue;
    String getEmploy;
    String locationCollection;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.activity_news, container, false);

        bottomNavigationView=(BottomNavigationView)view.findViewById(R.id.bottom_navigation);
        lv=(ListView)view.findViewById(R.id.lv2);
        pBar=(ProgressBar)view.findViewById(R.id.load);
        mQueue =Volley.newRequestQueue(getActivity());
        swipe=(SwipeRefreshLayout)view.findViewById(R.id.swipe);
        pBar.setVisibility(View.VISIBLE);
        adapter=new newAdapter(getActivity(),dataList);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getActivity(),webViewActivity.class);
                intent.putExtra("data",dataList.get(position));
                startActivity(intent);
            }
        });
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);


      /*  bottomNavigationView.setSelectedItemId(R.id.news);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.news:

                        return true;
                    case R.id.map:
                      // startActivity(new Intent(getApplicationContext(),MapsActivity.class));
                        collectLocation();
                       Intent intent=new Intent(getApplicationContext(),MapsActivity.class);
                        intent.putExtra("title","");
                        intent.putExtra("lat","");
                        intent.putExtra("mag","");
                        intent.putExtra("lon","");
                        intent.putExtra("list",locationCollection);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });*/
        jsonParse();

       swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                jsonParse();
            }
        });
        return view;
    }
    private void jsonParse() {
        String url = "https://gnews.io/api/v4/search?q=earthquake&token=685026fe9446853f0482b9d4b0c5e395&lang=en";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        getEmploy = response.toString();
                        readData();
                        pBar.setVisibility(View.INVISIBLE);
                        swipe.setRefreshing(false);
                      //  Toast.makeText(getApplicationContext(),"received",Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }

        });
        mQueue.add(request);


    }
    public void readData(){
        dataList.clear();
        try {

            JSONObject object = new JSONObject(getEmploy);
            String EmployArray = object.getString("articles");
            JSONArray array = new JSONArray(EmployArray);



            int length = array.length();
            for (int i=0; i<length; i++){
                dataList.add(array.getString(i));
            }


            adapter.notifyDataSetChanged();


        }
        catch (Exception e){

        }


    }

  /*  public void home(View view) {
        Intent a =new Intent(this,MainActivity.class);
        startActivity(a);
        finish();
    }

    public void map(View view) {
        Intent intent=new Intent(this,MapsActivity.class);
        startActivity(intent);
    }*/
  public void collectLocation(){
      JSONArray parentArray = new JSONArray();
      try {
          JSONObject objectX = new JSONObject(getEmploy);
          String EmployArray = objectX.getString("features");
          JSONArray array = new JSONArray(EmployArray);
          for (int i=0; i<array.length(); i++){
              JSONObject object = new JSONObject(array.getString(i));
              String properties=object.getString("properties");
              JSONObject object2 = new JSONObject(properties);
              String title=object2.getString("title");
              String mag=object2.getString("mag");
              //  Toast.makeText(this,mag,Toast.LENGTH_SHORT).show();
              String geometry=object.getString("geometry");
              JSONObject object1 = new JSONObject(geometry);
              String coordinate = object1.getString("coordinates");

              JSONArray array2 = new JSONArray(coordinate);
              String lat = array2.getString(0);
              String logn=array2.getString(1);

              // parsing array
              JSONObject object3 = new JSONObject();
              object3.put("lat",lat);
              object3.put("lon",logn);
              object3.put("mag",mag);
              object3.put("title",title);
              parentArray.put(object3);
//            Intent intent=new Intent(this,MapsActivity.class);
//            startActivity(intent);
              // Toast.makeText(this,timesList.toString(),Toast.LENGTH_SHORT).show();
              locationCollection = parentArray.toString();
          }
           Toast.makeText(getActivity(),parentArray.toString(),Toast.LENGTH_SHORT).show();
      }
      catch (Exception e){
          Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
      }
  }
}