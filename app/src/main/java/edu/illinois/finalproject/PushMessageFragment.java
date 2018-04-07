package edu.illinois.finalproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;


import static edu.illinois.finalproject.MainActivity.TAG;

/**
 * Created by haonanwang on 12/5/17.
 */

/**
 * In the PushMessageFragment, the recent plan name,
 * punch message and location information will be sent to fireBase together.
 */
public class PushMessageFragment extends Fragment {
    public static final String BUNDLE_TITLE = "title";
    private String mTitle = "DefaultValue";
    private View thisview;
    private PushMessageFragment main;
    private Context context;
    private Plan plan;
    private LocationManager locationManager;
    private String provider;
    private String currentPosition;
    private String punchRefKey;
    private String punchRecordKey;
    private String planname = "Please create plan first";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        thisview = inflater.inflate(R.layout.messagefragment_layout, container, false);
        main = this;
        context = this.context;
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference(getCurrentUser()+"/plan");
        final TextView planName = (TextView) thisview.findViewById(R.id.planname);

        myRef.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: plan name test");
                long num = dataSnapshot.getChildrenCount();

                //Because the latest plan will be put at the end
                //this for loop is used to get the current plan.
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    plan = data.getValue(Plan.class);
                    int count = 0;
                    if(count < num){
                        count += 1;
                        if(plan.getPlanName() == null){
                            planname = "N/A";
                        }else{
                            planname = plan.getPlanName();
                        }
                    }
                }
                planName.setText(planname);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        locationManager = (LocationManager) getActivity().getSystemService(Context.
                LOCATION_SERVICE);

        final Button addLocationButton = (Button) thisview.findViewById(R.id.addlocation);
        addLocationButton.setOnClickListener(new View.OnClickListener() {
            /**
             * This method choose an available GPS provider
             * @param view
             */
            @Override
            public void onClick(View view) {
                List<String> providerList = locationManager.getProviders(true);
                if (providerList.contains(LocationManager.GPS_PROVIDER)) {
                    provider = LocationManager.GPS_PROVIDER;
                } else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
                    provider = LocationManager.NETWORK_PROVIDER;
                } else {
                    Snackbar.make(view, "No location provider to use", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }

                if (ActivityCompat.checkSelfPermission(context,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(context,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                Location location = locationManager.getLastKnownLocation(provider);
                if (location != null) {
                    //show the location on screen
                    showLocation(location);
                }
                locationManager.requestLocationUpdates(provider, 5000, 1, locationListener);
            }
        });


        FloatingActionButton fab =
                (FloatingActionButton) thisview.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Read the input field and push a new instance
                // of ChatMessage to the Firebase database
                final EditText input = (EditText) thisview.findViewById(R.id.input);
                final DatabaseReference punchRef = database.getReference("newMessage");
                final DatabaseReference punchRecord = database.getReference(getCurrentUser()+"/record");

                //Data in the Firebase real-time database is always stored as key-value pairs.
                // However, if you observe the code above,
                // you'll see that we're calling setValue() without specifying any key.
                // That's allowed only because the call to the setValue() method is preceded
                // by a call to the push() method, which automatically generates a new key.
                // PunchMessage will be pushed to two different directories.
                // This is for the convenience of getting data back
                punchRecord.push()
                        .setValue(new PunchMessage(input.getText().toString(),
                                getCurrentUser(), plan.getPlanName(), plan.getStart(), plan.getEnd(), currentPosition)
                        );
                punchRecord.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        punchRecordKey = "record";
                        for(DataSnapshot data : dataSnapshot.getChildren()){
                            punchRecordKey = data.getKey();
                        }
                        Log.d(TAG, "punchRefKey: " + punchRecordKey);
                        DatabaseReference addKeyToRef= FirebaseDatabase.getInstance().getReference()
                                .child(getCurrentUser()+"/record")
                                .child(punchRecordKey).child("messageKey");
                        addKeyToRef.setValue(punchRecordKey);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                punchRef.push()
                        .setValue(new PunchMessage(input.getText().toString(),
                                getCurrentUser(), plan.getPlanName(), plan.getStart(), plan.getEnd(), currentPosition)
                        );

                punchRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        punchRefKey = "Ref";
                        for(DataSnapshot data : dataSnapshot.getChildren()) {
                            punchRefKey = data.getKey();
                        }
                        Log.d(TAG, "punchRecord: " + punchRefKey);
                        DatabaseReference addKey= FirebaseDatabase.getInstance().getReference().child("newMessage")
                                .child(punchRefKey).child("messageKey");
                        addKey.setValue(punchRecordKey);
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                input.setText("");
            }
        });

        return thisview;
    }

    /**
     * this method will destroy listener when quit
     */
    public void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    }

    LocationListener locationListener = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle
                extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onLocationChanged(Location location) {
            // update location infomation
            showLocation(location);
        }
    };

    public static final int SHOW_LOCATION = 0;

    /**
     * a new thread will be opened to get location information
     * the latitude and longitude information will be sent to google mapDecoder to decode
     * for convenience, I use Json to get result.
     * @param location
     */
    private void showLocation(final Location location) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //make a url with current latitude and longitude
                    StringBuilder url = new StringBuilder();
                    url.append("http://maps.googleapis.com/maps/api/geocode/json?latlng=");
                    url.append(location.getLatitude()).append(",");
                    url.append(location.getLongitude());
                    url.append("&sensor=false");

                    //use HttpClient to open the url
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpGet = new HttpGet(url.toString());
                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        HttpEntity entity = httpResponse.getEntity();
                        String response = EntityUtils.toString(entity, "utf-8");

                        JSONObject jsonObject = new JSONObject(response);

                        // get results's location information
                        JSONArray resultArray = jsonObject.getJSONArray("results");

                        //if json cannot parse correctly, handler will not show it.
                        if (resultArray.length() > 0) {
                            JSONObject subObject = resultArray.getJSONObject(0);
                            String address = subObject.getString("formatted_address");

                            Message message = new Message();
                            message.what = SHOW_LOCATION;
                            message.obj = address;
                            handler.sendMessage(message);
                        }
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_LOCATION:
                    currentPosition = (String) msg.obj;
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    /**
     * This method is to help MainActivity pass information to TextFragment.
     * @return a new fragment
     */
    public static PushMessageFragment newInstance() {
        PushMessageFragment fragment = new PushMessageFragment();
        return fragment;
    }

    /**
     * this is a helper method to get current user name
     * @return current user name
     */
    private String getCurrentUser() {
        String userName;
        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            userName = "NoCurrentUser";
        }
        else{
            userName= FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        }
        return userName;
    }
}
