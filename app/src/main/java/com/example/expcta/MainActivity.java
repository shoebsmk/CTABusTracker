package com.example.expcta;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Remember INTERNET permission in the manifest

    //https://developer.android.com/training/volley/simple#java
    //implementation 'com.android.volley:volley:1.2.1'
    LinkedHashMap<String, String> routesResultMap;
    LinkedHashMap<String, String> stopsResultMap;
    LinkedHashMap<String, String> directionsResultMap;
    RecyclerView recyclerView;
    PredictionAdapter predictionAdapter;

    public List<Prediction> predictionList = new ArrayList<>();

    public static String getYourAPIKey() {
        return yourAPIKey;
    }

    //////////////////////////////////////////////////////////////////////////////////
    // Sign up to get your API Key at:  https://home.openweathermap.org/users/sign_up
    public static String yourAPIKey = "ZBNiPc3LP3XNCeMZZcqrbrKUZ";
    //
    //////////////////////////////////////////////////////////////////////////////////

    private final String TAG = getClass().getSimpleName();
    public TextView responseTV;
    public TextView routeSelTV;
    public TextView stopSelTV;
    public TextView directionSelTV;

    public String getFinalRoute() {
        return finalRoute;
    }

    public String getFinalStop() {
        return finalStop;
    }

    public String getFinalDir() {
        return finalDir;
    }

    private String finalRoute;
    private String finalStop;
    private String finalDir;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        routeSelTV = findViewById(R.id.routeSelTV);
        stopSelTV = findViewById(R.id.stopSelTV);
        directionSelTV = findViewById(R.id.directionSelTV);

        recyclerView = findViewById(R.id.myRV);

        predictionAdapter = new PredictionAdapter(predictionList, this);

        recyclerView.setAdapter(predictionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        RoutesDownloader.getAllRoutes(this);
    }

    public void getPredData(View v) {

        //doDownload();
        PredictionDownloader.getPrediction(this);
    }



    public void updateRouteMap(LinkedHashMap<String, String> resultMap) {
        this.routesResultMap = resultMap;
        Log.e(TAG, "updateRoutesMap: " + resultMap );
    }

    public void updateStopsMap(LinkedHashMap<String, String> resultMap) {
        this.stopsResultMap = resultMap;
        Log.e(TAG, "updateStopsMap: " + resultMap );
    }
    public void updateDirectionsMap(LinkedHashMap<String, String> resultMap) {
        this.directionsResultMap = resultMap;
        Log.e(TAG, "updateDirectionsMap: " + resultMap );
    }

    public void onRouteSelClick(View v){
        final int size = routesResultMap.values().size();
        Log.d(TAG, "SelectFromResultMaps: " + size);

        // List selection dialog
        //make an array of strings

        final CharSequence[] sArray = new CharSequence[size];

        for (int i = 0; i <size; i++){
            sArray[i] = routesResultMap.keySet().toArray()[i].toString() + " " + routesResultMap.values().toArray()[i].toString();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select a bus");

        // Set the builder to display the string array as a selectable
        // list, and add the "onClick" for when a selection is made
        builder.setItems(sArray, (dialog, which) -> {
            String[] parts = (sArray[which]).toString().split(" ");
            String selectedRouteKey = parts[0];
            Log.d(TAG, "onRouteSelClick: " +routesResultMap.get(selectedRouteKey));
            Log.d(TAG, "onRouteSelClick: " + selectedRouteKey);

            finalRoute = selectedRouteKey;
            //DIRECTION SETUP THEN CALL THIS
            DirectionsDownloader.getAllDirections(this,finalRoute);
            routeSelTV.setText(sArray[which]);
            stopsResultMap = null;
            directionSelTV.setText("");
            stopSelTV.setText("");
            finalDir = "";
            finalStop = "";

        });

        builder.setNegativeButton("Nevermind", (dialog, id) -> {
        });
        AlertDialog dialog = builder.create();

        dialog.show();


    }

    public void onStopSelClick(View v){
        if(stopsResultMap == null){
            Toast.makeText(this, "Enter Bus/Route and Direction", Toast.LENGTH_SHORT).show();
            return;
        }
        final int size = stopsResultMap.values().size();
        Log.d(TAG, "SelectFromResultMaps: " + size);

        // List selection dialog
        //make an array of strings

        final CharSequence[] sArray = new CharSequence[size];

        for (int i = 0; i <size; i++){
            sArray[i] = stopsResultMap.keySet().toArray()[i].toString() + " " + stopsResultMap.values().toArray()[i].toString();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select a Stop");

        // Set the builder to display the string array as a selectable
        // list, and add the "onClick" for when a selection is made
        builder.setItems(sArray, (dialog, which) -> {
            String[] parts = (sArray[which]).toString().split(" ");
            String selectedStopKey = parts[0];

            Log.e(TAG, "onStopSelClick: " + selectedStopKey);
            finalStop = selectedStopKey;
            stopSelTV.setText(sArray[which]);
            PredictionDownloader.getPrediction(this);
        });

        builder.setNegativeButton("Nevermind", (dialog, id) -> {
        });
        AlertDialog dialog = builder.create();

        dialog.show();
    }

    public void onDirectionSelClick(View v){
        if(directionsResultMap == null){
            Toast.makeText(this, "Enter Bus/Route first.", Toast.LENGTH_SHORT).show();
            return;
        }
        int x = 6;
        //result map null check
        final int size = directionsResultMap.values().size();
        Log.d(TAG, "SelectFromResultMaps: " + size);

        // List selection dialog
        //make an array of strings

        final CharSequence[] sArray = new CharSequence[size];

        for (int i = 0; i <size; i++){
            sArray[i] = directionsResultMap.keySet().toArray()[i].toString() + " " + directionsResultMap.values().toArray()[i].toString();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select a direction");

        // Set the builder to display the string array as a selectable
        // list, and add the "onClick" for when a selection is made
        builder.setItems(sArray, (dialog, which) -> {
            String[] parts = (sArray[which]).toString().split(" ");
            String selectedDirKey = parts[0];

            Log.e(TAG, "onStopSelClick: " + selectedDirKey);
            finalDir = selectedDirKey;
            directionSelTV.setText(finalDir);
            stopSelTV.setText("");
            finalStop = "";
            StopsDownloader.getStopsForRoute(this,finalRoute, finalDir);
        });

        builder.setNegativeButton("Nevermind", (dialog, id) -> {
        });
        AlertDialog dialog = builder.create();

        dialog.show();
    }

    public void updateData(Prediction pred) {

        predictionList.add(pred);
        predictionAdapter.notifyItemRangeChanged(0, predictionList.size());
    }

    public void downloadFailed() {
        predictionList.clear();
        predictionAdapter.notifyItemRangeChanged(0, predictionList.size());
    }
}