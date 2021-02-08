package com.example.earthquake;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.Person;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    RequestQueue mQueue;
    boolean isActionBarHide=false;
    ArrayList<String>addItem=new ArrayList<>();
    String getEmploy;
    String locationCollection;
    //MyAdapter myAdapter;
    ViewPager viewPager;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        // viewPager=(ViewPager)findViewById(R.id.viewpager);
      /*   myAdapter=new MyAdapter(getSupportFragmentManager());
         viewPager.setAdapter(myAdapter);*/
        mQueue = Volley.newRequestQueue(this);
        // bottomNavigationView =(BottomNavigationView)findViewById(R.id.b_view);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
       
        setSupportActionBar(toolbar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.home:
                        if(isActionBarHide){
                            isActionBarHide=false;
                            getSupportActionBar().show();
                        }

                        toolbar.setTitle("Earthquake");
                        //   Toast.makeText(getApplicationContext(), "Home clicked", Toast.LENGTH_SHORT).show();
                        loadFragment(new MainActivity());
                        break;
                    case R.id.news:
                        if(isActionBarHide){
                            isActionBarHide=false;
                            getSupportActionBar().show();
                        }
                        toolbar.setTitle("Earthquake News");
                        loadFragment(new newsActivity());
                        break;
                    case R.id.map:
                       // toolbar.setTitle("Map");
                        getSupportActionBar().hide();
                        isActionBarHide=true;
                        loadFragment(new MapsActivity());
                        break;

                    case R.id.info:
                        if(isActionBarHide){
                            isActionBarHide=false;
                            getSupportActionBar().show();
                        }
                        toolbar.setTitle("Developer Info");
                        loadFragment(new infoActivity());
                        break;
                }
                return true;
            }
        });
        // end

    }

    private void loadFragment(Fragment fragment) {
// create a FragmentManager
        FragmentManager fm = getFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
// replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.history:
                Intent i=new Intent(this,HistoryActivity.class);
                startActivity(i);

        }
        return super.onOptionsItemSelected(item);
    }
    public void onResume(){
        super.onResume();
        loadFragment(new MainActivity());
    }
}



