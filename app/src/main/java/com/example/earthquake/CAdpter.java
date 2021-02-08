package com.example.earthquake;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

class CAdpter extends ArrayAdapter {
    Context context;
    ArrayList<String>itemList;
    public CAdpter(Context context, ArrayList itemList) {
        super(context,R.layout.historylist,itemList);

        this.context=context;
        this.itemList=itemList;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view= inflater.inflate(R.layout.historylist,null,true);

       TextView tv1=(TextView)view.findViewById(R.id.tv3);
//        TextView tv2=(TextView)view.findViewById(R.id.tv2);
//        TextView tv3=(TextView)view.findViewById(R.id.tv3);
        TextView tv4=(TextView)view.findViewById(R.id.tv4);
        Calendar calendar= Calendar.getInstance();

        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("hh:mm:ss");
        String datetime=simpleDateFormat.format(calendar.getTime());




        String datalist= itemList.get(position);
        try {
            JSONObject object = new JSONObject(datalist);
            String features = object.getString("features");
            JSONArray array = new JSONArray(features);
            JSONObject object1=new JSONObject(array.getString(0));
            String properties = object1.getString("properties");
            JSONObject jsonObject = new JSONObject(properties);
            String time  = jsonObject.getString("time");
            String tittle=jsonObject.getString("title");
            tv1.setText(tittle);
            tv4.setText(time);


        }
        catch (Exception e){

        }



        return view;
    }}
