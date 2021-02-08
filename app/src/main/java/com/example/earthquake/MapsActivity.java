package com.example.earthquake;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class MapsActivity extends Fragment {
    SharedPreferences pref;
    MapView mMap;
    View view;
    SharedPreferences.Editor editor;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view=inflater.inflate(R.layout.activity_maps,container,false);
        mMap= view.findViewById(R.id.map);
        mMap.onCreate(savedInstanceState);
        mMap.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
            // Toast.makeText(getActivity(),"abcd",Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
        }mMap.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                double latitude = 0,longitude=0,magi=0;

                pref = getActivity().getSharedPreferences("myPref",0);
                editor = pref.edit();
                String mapData = pref.getString("locationList","");


                try {
                    JSONArray array = new JSONArray(mapData);
                    for (int i=0; i<array.length(); i++){
                        JSONObject object = new JSONObject(array.getString(i));
                        String lat = object.getString("lat");
                        String mag=object.getString("mag");
                        String lon = object.getString("lon");
                        String title = object.getString("title");
                        // Toast.makeText(getApplicationContext(),lon,Toast.LENGTH_SHORT).show();
                        // process map
                        if (title.length()>0){
                            latitude = Double.parseDouble(lat);
                            longitude = Double.parseDouble(lon);
                            magi=Double.parseDouble(mag);

                        }
                        LatLng India = new LatLng(latitude, longitude);
                     //   googleMap.addMarker(new MarkerOptions().position(India).title(title));
//                       Toast.makeText(getContext(),"if",Toast.LENGTH_SHORT).show();
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(India,8F));



                        if (magi>0 && magi<3){
                            googleMap.addMarker(new MarkerOptions().position(India).title(title).icon(bitmapDescriptorFromVector(getActivity(), R.drawable.earthquakeredmarkerpgreen)));
                           // Toast.makeText(getActivity(),"if",Toast.LENGTH_SHORT).show();
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(India,8F));
                        }else if (magi>2 && magi<5){

                            googleMap.addMarker(new MarkerOptions().position(India).title(title).icon(bitmapDescriptorFromVector(getActivity(), R.drawable.earthquakeredmarkerp)));
                           // Toast.makeText(getActivity(),"else part",Toast.LENGTH_SHORT).show();
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(India,8F));

                        }
                    }

                }
                catch (Exception e){
                    Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
                }

               // Toast.makeText(getActivity(),mapData,Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

}