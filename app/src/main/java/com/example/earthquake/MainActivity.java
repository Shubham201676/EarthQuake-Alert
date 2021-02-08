package com.example.earthquake;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.Person;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends Fragment {
    View view;
    CustomAdpter adpter;
    ListView lv;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    RequestQueue mQueue;
    SwipeRefreshLayout swipe;
    TextView tv4;
    ArrayList<String> data=new ArrayList<>();
    String getEmploy;
    BottomNavigationView bottomNavigationView;
   // Dialog f3;
    ProgressBar progressBar;
    JSONArray array;
    String datafile;
    ArrayList<String>timesList = new ArrayList<>();
    String locationCollection;
    boolean isHistory = false;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.activity_main, container, false);
        progressBar=(ProgressBar)view.findViewById(R.id.loader);
        lv=(ListView)view.findViewById(R.id.lv);
       bottomNavigationView=(BottomNavigationView)view.findViewById(R.id.bottom_navigation);
        tv4=(TextView)view.findViewById(R.id.tv4);
        mQueue= Volley.newRequestQueue(getActivity());
        swipe=(SwipeRefreshLayout)view.findViewById(R.id.swipe);
        progressBar.setVisibility(View.VISIBLE);
      //  showLoading();
        adpter=new CustomAdpter(getActivity(),data);
        lv.setAdapter(adpter);
        preferences=getActivity().getSharedPreferences("myPref",0);
        editor=preferences.edit();


     //reset();
        String savedApi = preferences.getString("api","");
        if (!savedApi.isEmpty()){
            Toast.makeText(getActivity(),"history found",Toast.LENGTH_SHORT).show();
            // execute history
            getEmploy = savedApi;
            processApi();
            saveApi();

            //readtime();
            readData();
            collectLocation();
            Person person = new Person();
            person.execute();
        }
        else {
            parse();
        }


   swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
         @Override
         public void onRefresh() {

                 parse();
         }
     });
    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
   //           String jsonMapData = data.get(position);
//            processJSonMap(jsonMapData);
        }
    });

   /* bottomNavigationView.setSelectedItemId(R.id.home);
    bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.news:
                    startActivity(new Intent(getApplicationContext(),newsActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.home:

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



        return view;
    }
    public void parse(){
        String url ="https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_hour.geojson";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        getEmploy = response.toString();
                        processApi();
                        saveApi();

                        //readtime();
                        readData();
                        collectLocation();
                        Person person = new Person();
                        person.execute();
                      // f3.dismiss();
                        progressBar.setVisibility(View.INVISIBLE);
                      //  swipe.setVisibility(View.GONE);
                        swipe.setRefreshing(false);

                       // Toast.makeText(getApplicationContext(),"received",Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                readOfflineApi();
            }

        });
        mQueue.add(request);


    }
    public void readData(){
        data.clear();
        try {

            JSONObject object = new JSONObject(getEmploy);
            String EmployArray = object.getString("features");
            JSONArray array = new JSONArray(EmployArray);



            int length = array.length();
            for (int i=0; i<length; i++){
                data.add(array.getString(i));
            }


            adpter.notifyDataSetChanged();


        }
        catch (Exception e){

        }

    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        MenuItem menuItem=menu.findItem(R.id.setting);
        return true;
    }*/

   /* public void news(View view) {
        Intent intent=new Intent(this,newsActivity.class);
        startActivity(intent);
        finish();
    }*/






 /*   public void showLoading(){
        f3 = new Dialog(this);
        f3.requestWindowFeature(Window.FEATURE_NO_TITLE);
        f3.setContentView(R.layout.progressbar);
        f3.setCancelable(false);
        f3.show();
    }*/
//private void refresh(int milliseconds){
    public void processApi(){
        Calendar calendar= Calendar.getInstance();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("hh:mm:ss");
        String datetime=simpleDateFormat.format(calendar.getTime());

        try {
            JSONObject object1 = new JSONObject(getEmploy);
            String features = object1.getString("features");
            JSONArray array = new JSONArray(features);
            int length = array.length();
            for (int i = 0; i<length; i++){
                JSONObject jsonObject = new JSONObject(array.getString(i));
                String properties = jsonObject.getString("properties");
                JSONObject object11 = new JSONObject(properties);
                String time = object11.getString("time");

                // json parsing begin here
                object11.put("time",datetime);
                jsonObject.put("properties",object11);
                array.put(i,jsonObject);
                timesList.add(time);
            }
          //  Toast.makeText(this,array.toString(),Toast.LENGTH_SHORT).show();
          //  getEmploy = array.toString();
            object1.put("features",array.toString());
           // Toast.makeText(this,object1.toString(),Toast.LENGTH_SHORT).show();
            getEmploy =object1.toString();
            saveApi();
            readData();
         //  swipe.setRefreshing(false);


        }
        catch (Exception e){
                Toast.makeText(getActivity(),"Process Error",Toast.LENGTH_SHORT).show();
        }
       // Toast.makeText(this,timesList.get(0),Toast.LENGTH_SHORT).show();
    }

    public void  saveApi(){

        String db= preferences.getString("api","");
        if (db.length()>0){
            try {
               // existing data available
                array = new JSONArray(db);
                array.put(getEmploy);

                editor.putString("api",array.toString()).commit();
              // Toast.makeText(getApplicationContext(),"data saved",Toast.LENGTH_SHORT).show();
            }
            catch (Exception e){
                Toast.makeText(getActivity(),"data not saved",Toast.LENGTH_SHORT).show();
            }
        }
        else {
            try {
                // no data available
                 array = new JSONArray();
                array.put(getEmploy);
                editor.putString("api",array.toString()).commit();

            }
            catch (Exception e){

            }
        }
    }



public void reset(){
    editor.putString("api","").commit();

}
public void readOfflineApi(){
    String db= preferences.getString("api","");

    try {
        JSONArray array = new JSONArray(db);
        int length = array.length();
        getEmploy = array.getString(length-1);

        readData();
        swipe.setRefreshing(false);
    }
    catch (Exception e){
        Toast.makeText(getActivity(),"error",Toast.LENGTH_SHORT).show();
    }

}

  /*  public void map(View view) {
        collectLocation();


       Intent intent=new Intent(this,MapsActivity.class);
        intent.putExtra("title","");
        intent.putExtra("lat","");
        intent.putExtra("mag","");
        intent.putExtra("lon","");
        intent.putExtra("list",locationCollection);




        startActivity(intent);


    }*/
/*public void readtime(){
    Calendar calendar= Calendar.getInstance();
    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("hh:mm:ss");
    String datetime=simpleDateFormat.format(calendar.getTime());
        String df=preferences.getString("timedata","");
        try {

            JSONObject object1=new JSONObject(df);
                 String tt= object1.getString(datetime);


            editor.putString("timedata",array.toString()).commit();

            tv4.setText(tt);
            Toast.makeText(this,"time saved",Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(this,"time not saved",Toast.LENGTH_SHORT).show();
        }
}*/
  public  void processJSonMap(String json){
      try {
          JSONObject object = new JSONObject(json);
          String properties=object.getString("properties");
          JSONObject object2 = new JSONObject(properties);
          String title=object2.getString("title");

          String geometry=object.getString("geometry");
          JSONObject object1 = new JSONObject(geometry);
          String coordinate = object1.getString("coordinates");

          JSONArray array = new JSONArray(coordinate);
          String lat = array.getString(0);
          String logn=array.getString(1);
          Intent n = new Intent(getActivity(),MapsActivity.class);
          n.putExtra("lat",lat);

          n.putExtra("title",title);
          n.putExtra("lon",logn);
          startActivity(n);






      } catch (Exception e) {
          e.printStackTrace();
          Toast.makeText(getActivity(),"error",Toast.LENGTH_SHORT).show();
      }
  }

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
          }
          locationCollection = parentArray.toString();
          editor.putString("locationList",locationCollection).commit();
          editor.putString("api","").commit();
         //Toast.makeText(getActivity(),locationCollection,Toast.LENGTH_SHORT).show();
      }
      catch (Exception e){
          Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
      }



  }
    class Person extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            Task task = new Task();
            task.setName(getEmploy);
            //adding to database
            DatabaseClient.getInstance(getActivity()).getAppDatabase()
                    .taskDao()
                    .insert(task);
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
          //  Toast.makeText(getActivity(),"saved", Toast.LENGTH_SHORT).show();
        }
    }

   }
