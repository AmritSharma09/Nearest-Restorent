package com.example.golu.zombatomap;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ResturantFragment extends Fragment {
private RecyclerView recyclerView;
private MyTaks myTaks;
   private  MyAdapter adapter;
    private ArrayList<Restaurant> restaurants;
    public ResturantFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Initialize all variable
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_resturant, container, false);
        restaurants=new ArrayList<Restaurant>();
        myTaks=new MyTaks();

        recyclerView=(RecyclerView)view.findViewById(R.id.rec);
        adapter=new MyAdapter();
        recyclerView.setAdapter(adapter);
        LinearLayoutManager mgr=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(mgr);
        myTaks.execute("https://developers.zomato.com/api/v2.1/geocode?lat=12.8984&lon=77.6179");
        return view;
    }

    private class MyTaks extends AsyncTask<String,Void,String>{
        URL url;  //THIS VARIABLE FOR PREAPAREING URL OF THE WEBSITE
        HttpURLConnection connection;// THIS IS FRO OPENNIG SERVER CONNECTION
        InputStream inputStream;//THIS IOS TO OPEN INPUTSTREAM, SO THAT WE CAN READ SERVER DATA
        InputStreamReader inputStreamReader;//THIS IS TO CONVER VITS AND BYTE TO ACSII CHARS
        BufferedReader bufferedReader;// THIS IS TO BUFFER SERVER DATA IN LINE BY LINE FASHION
        String line;// THIS IS TO READ EACH LINE FROM BUFFERED-READER
        StringBuilder stringBuilder;//THIS IS TO PILE UP EACH LINE (ABOVE ) TO DISPLAY ON TEXTVIEW
        @Override
        protected void onPreExecute() {
            Toast.makeText(getActivity(),"Task is starting",Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                url=new URL(strings[0]);
                connection= (HttpURLConnection) url.openConnection();
                //PASS ZOMATO API KEY TO ZOMATO SERVER
                connection.setRequestProperty("user-key","af79b4795ea650e8061bf69206c0ab0d");
                //TELL TO ZOMATO SERVER THAT I WANT JSON DATA
                connection.setRequestProperty("Accept","application/json");
                //c. open input Stream -cruicial- this might take couple of seconds
                inputStream  =connection.getInputStream();
                //d. open input stream reader
                inputStreamReader =new InputStreamReader(inputStream);
                //e. open buffer reader
                bufferedReader=new BufferedReader(inputStreamReader);
                //f. using loop read line by line from buffered reader
                stringBuilder=new StringBuilder();
                do{
                    line=bufferedReader.readLine();
                    stringBuilder.append(line);

                }while(line!=null);

            } catch (MalformedURLException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();
                Log.d("B36","Io Exceptiom"+e.getCause() );
                //Toast.makeText(getActivity(),"Io Exception"+e.getCause(),Toast.LENGTH_SHORT).show();
               // return "UNABLE TO OPEN CONNECTION"+e.getMessage();
            }
            return stringBuilder.toString();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Toast.makeText(getActivity(),""+s,Toast.LENGTH_SHORT).show();
           // Log.d("B36","json:"+s);
            if(s==null){

            }
            else {
                try {
                    JSONObject j=new JSONObject(s);
                    JSONArray nearby_restaurant=j.getJSONArray("nearby_restaurants");
                    for(int i=0;i<nearby_restaurant.length();i++){
                        JSONObject rest=nearby_restaurant.getJSONObject(i);
                        JSONObject restaurant=rest.getJSONObject("restaurant");
                        String name=restaurant.getString("name");
                        JSONObject location=restaurant.getJSONObject("location");
                        String address=location.getString("address");
                        String locality=location.getString("locality");
                        String city=location.getString("city");
                        String latitude=location.getString("latitude");
                        String longitude=location.getString("longitude");
                        String cuisine=restaurant.getString("cuisines");
                        String thumb=restaurant.getString("thumb");
                        JSONObject user_rating=restaurant.getJSONObject("user_rating");
                        String rating=user_rating.getString("aggregate_rating");

                        //prepare restaurant object with parameter to constructor
                        Restaurant myRestaurant=new Restaurant( name,  address,  locality,  latitude,  longitude,  city,  rating,  cuisine,  thumb);

                        restaurants.add(myRestaurant);
                        adapter.notifyDataSetChanged();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=getActivity().getLayoutInflater().inflate(R.layout.row,parent,false);
            //pass row xml to view holder
            ViewHolder viewHolder=new ViewHolder(view);

            return viewHolder;

        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Restaurant r=restaurants.get(position);
           holder.restaurantName.setText(r.getName());
            holder.restaurantaddress.setText(r.getAddress());
            holder.restaurantlocation.setText(r.getLocality());
            holder.restaurantCuisine.setText(r.getCuisines());
            holder.restaurantCity.setText(r.getCity());
            Float rating=Float.parseFloat(r.getRating());
            holder.ratingBar.setRating(rating);
            Glide.with(getActivity()).load(r.getThumb()).into(holder.image);

        }

        @Override
        public int getItemCount() {
            return restaurants.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ImageView image;
            public TextView restaurantName,restaurantaddress,restaurantlocation,restaurantCuisine,restaurantCity;
           public Button map;
            RatingBar ratingBar;
            public ViewHolder(View itemView) {
                super(itemView);
                image= (ImageView) itemView.findViewById(R.id.image);
                restaurantName=(TextView)itemView.findViewById(R.id.restaurantName);
                restaurantaddress=(TextView)itemView.findViewById(R.id.address);
                restaurantlocation=(TextView)itemView.findViewById(R.id.location);
                restaurantCuisine=(TextView)itemView.findViewById(R.id.cuisine);
                restaurantCity=(TextView)itemView.findViewById(R.id.city);
                ratingBar=(RatingBar)itemView.findViewById(R.id.rate);
                map=(Button)itemView.findViewById(R.id.mapButton);
                map.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                      //  Toast.makeText(getActivity(),"Still working",Toast.LENGTH_SHORT).show();
                        int pos=getAdapterPosition();//get position where user is clicking
                        Restaurant r=restaurants.get(pos);//get restaurant position based on position
                        String name=r.getName();//get restaurant name
                        String latitude=r.getLatitude();//
                        String longitude=r.getLongitude();
                        //use Intent  and pass name latitude,longitude
                        Intent i=new Intent(getActivity(),MapsActivity.class);
                        i.putExtra("name",name);
                        i.putExtra("latitude",latitude);
                        i.putExtra("longitude",longitude);
                        startActivity(i);

                    }
                });

            }
        }
    }
}
