package com.example.earthquake;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONObject;

import java.util.ArrayList;

class newAdapter extends ArrayAdapter<String> {
    Context context;
    ArrayList<String>dataList;
    public newAdapter(Context context, ArrayList dataList) {
        super(context,R.layout.newsholder,dataList);
        this.context=context;
        this.dataList=dataList;


    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView= inflater.inflate(R.layout.newsholder,null,true);
        TextView tv1=(TextView)rowView.findViewById(R.id.title);

        TextView tv2=(TextView)rowView.findViewById(R.id.description);
        TextView time=(TextView)rowView.findViewById(R.id.time);
        TextView source=(TextView)rowView.findViewById(R.id.sours);
      //  TextView rm=(TextView)rowView.findViewById(R.id.rm);

        ImageView img = (ImageView)rowView.findViewById(R.id.image);


        String data= dataList.get(position);
        try {
            JSONObject object = new JSONObject(data);

            String firstName=object.getString("title");
            String urll=object.getString("url");
              String s = object.getString("source");
             JSONObject object1 = new JSONObject(s);
             String name = object1.getString("name");

            String lastName =object.getString("description");
            String userId=object.getString("content");
            String t=object.getString("publishedAt");
           String Url=object.getString("image");
           // int sr=t.length()-1;
            //String stt=Integer.toString(sr);
            //String emailAddress =object.getString("code");
            tv1.setText(firstName);

            time.setText(t);
            source.setText(name);
            // tv1.setText(tv1.getText()+"\n"+firstName);
            tv2.setText(lastName);
          Glide.with(getContext()).load(Url).into(img);
          //  tv3.setText(userId);
            //tv1.setText(tv1.getText()+"\n"+lastName);
            //tv1.setText(tv1.getText()+"\n"+userId);

        }
        catch (Exception e){

        }
        return rowView;
    }}
