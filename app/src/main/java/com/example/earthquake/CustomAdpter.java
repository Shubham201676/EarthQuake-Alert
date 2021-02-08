package com.example.earthquake;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.format.DateUtils;
import android.util.TimeFormatException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.airbnb.lottie.L;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

class CustomAdpter extends ArrayAdapter<String> {
    Context context;
    ArrayList<String>data=new ArrayList<>();

    public CustomAdpter(Context context, ArrayList data) {
        super(context,R.layout.holder,data);
        this.context=context;
        this.data=data;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.holder,null,true);
        TextView tv1=(TextView)view.findViewById(R.id.tv1);
        TextView tv2=(TextView)view.findViewById(R.id.tv2);
        TextView tv3=(TextView)view.findViewById(R.id.tv3);
        TextView tv4=(TextView)view.findViewById(R.id.tv4);
       Calendar calendar= Calendar.getInstance();
       

       SimpleDateFormat simpleDateFormat=new SimpleDateFormat("hh:mm:ss");
         String datetime=simpleDateFormat.format(calendar.getTime());
       // String t=time+"L";
//        Instant i=null;
//        String tym;
////
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            i=Instant.ofEpochMilli(1612268466620L);
//            i.atZone(ZoneId.of("india"));
//            tym=i.toString();
//            Toast.makeText(getContext(),tym,Toast.LENGTH_SHORT).show();
//        }




        String datalist= data.get(position);
        try {
            JSONObject object = new JSONObject(datalist);

            String type=object.getString("type");
            String features = object.getString("properties");
            String geometry = object.getString("geometry");
            JSONObject object2 = new JSONObject(geometry);

            JSONObject object1 = new JSONObject(features);
            String mag = object1.getString("mag");
            String place = object1.getString("place");
           String time = object1.getString("time");
           String coordinates=object2.getString("coordinates");
            JSONArray array = new JSONArray(coordinates);
            String lat = array.getString(0);
            String logn=array.getString(2);




//            Intent n = new Intent(getContext(),MapsActivity.class);
//            n.putExtra("lat",lat);
//            n.putExtra("lon",logn);
//            view.startActivity(n);
           // Toast.makeText(getContext(),lat,Toast.LENGTH_SHORT).show();


           /* TimeZone tz = TimeZone.getDefault();
            Date currentDate = new java.util.Date();
            Boolean dstValue = tz.inDaylightTime(currentDate);
           String tzName = tz.getDisplayName(dstValue, 0);

            Long finalTime = Long.parseLong(time);
            Integer gmtOffset = tz.getOffset(finalTime);
            finalTime = finalTime*1000;
          String  convertedTime = new java.text.SimpleDateFormat("EE/MM/dd/yyyy hh:mm:ss:SSS").format(new java.util.Date(finalTime));

            Long gmtTime = finalTime - gmtOffset;
          String  gmtString = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date(gmtTime));


            convertedTime = convertedTime + " [" + tzName + "]";
            gmtString = gmtString + " [GMT]";



//unix time convert
        /*  String  unixSeconds = time;

            SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
            String converteddate=sdf.format( new Date(Long.parseLong(unixSeconds) * 1000L));*/
          // datetime=converteddate;
// convert seconds to milliseconds
         /*  Date date = new java.util.Date(Long.parseLong(unixSeconds)*1000L);
           // long date =System.currentTimeMillis()/1000;

// the format of your date
            SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm:ss z ");
// give a timezone reference for formatting (see comment at the bottom)
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            String formattedDate = sdf.format(date);*/
            //System.out.println(formattedDate);

            //   String lastName =object.getString("mag");
            // String userId=object.getString("time");
            //String emailAddress =object.getString("code");

            // tv1.setText(firstName);

            tv1.setText(type);
            tv2.setText(mag);
            tv3.setText(place);
            tv4.setText(datetime);
            //tv1.setText(tv1.getText()+"\n"+lastName);
            //tv1.setText(tv1.getText()+"\n"+userId);
            ImageView im1=(ImageView)view.findViewById(R.id.alertimg1);
            AnimationSet animationSet;
            ImageView im2=(ImageView)view.findViewById(R.id.alertimg2);
           
//              Animation animation= AnimationUtils.loadAnimation(getContext(),R.anim.fade);
//              im2.setAnimation(animation);
//              im1.setAnimation(animation);



            double magi=0;
            magi=Double.parseDouble(mag);
                 if (magi>0 && magi<3){
                     Animation animation1= AnimationUtils.loadAnimation(getContext(),R.anim.fade);
                    im1.setAnimation(animation1);
                     Animation animation4= AnimationUtils.loadAnimation(getContext(),R.anim.fadeout);
                     im1.setAnimation(animation4);
    im1.setVisibility(View.VISIBLE);
    im2.setVisibility(View.INVISIBLE);
}else if (magi>2 && magi<6){
                     Animation animation2= AnimationUtils.loadAnimation(getContext(),R.anim.fade);
                     im2.setAnimation(animation2);
                     Animation animation3= AnimationUtils.loadAnimation(getContext(),R.anim.fadeout);
                     im2.setAnimation(animation3);
    im2.setVisibility(View.VISIBLE);
    im1.setVisibility(View.INVISIBLE);
}

        }
        catch (Exception e){

        }


        return view;
    }
}
