package com.example.expcta;

import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.LinkedHashMap;

public class DirectionsDownloader {
    private static final String BASE_URL_GET_DIRECTIONS = "https://www.ctabustracker.com/bustime/api/v3/getdirections";
    private static final String TAG = "DirectionsDownloader";

    public static void getAllDirections(MainActivity mainActivity, String route){
        RequestQueue queue = Volley.newRequestQueue(mainActivity);

        Uri.Builder buildURL = Uri.parse(BASE_URL_GET_DIRECTIONS).buildUpon();
        buildURL.appendQueryParameter("format", "json");
        buildURL.appendQueryParameter("key", mainActivity.yourAPIKey);
        buildURL.appendQueryParameter("rt", route);
        buildURL.appendQueryParameter("rtpidatafeed", "bustime");
        String urlToUse = buildURL.build().toString();

        Response.Listener<JSONObject> listener =
                response -> handleResults(mainActivity,response, urlToUse);
        Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(error.networkResponse.data));
                    //responseTV.setText(MessageFormat.format("Error: {0}", jsonObject.toString()));
                    Log.e(TAG, "onErrorResponse: " + MessageFormat.format("Error: {0}", jsonObject.toString()) );

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, urlToUse,
                        null, listener, error);
        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    private static void handleResults(MainActivity mainActivity, JSONObject response, String urlToUse) {
        try {
            LinkedHashMap<String, String> resultMap = new LinkedHashMap<>();
            //responseTV.setText(MessageFormat.format("Response: {0}", response.toString()));
            Log.e(TAG, "onResponse: " + urlToUse);

            //JSONObject responseJSONObject = response;
            Log.e(TAG, "onResponse: " + response );
            if(response.has("bustime-response")){
                //Log.e(TAG, "onResponse: "  + response.getString("bustime-response"));
                JSONArray directionsJsonArray = ((JSONObject)response.get("bustime-response")).getJSONArray("directions");
                Log.e(TAG, "onResponse: "  +  directionsJsonArray);

                for(int i = 0; i< directionsJsonArray.length();i++ ){
                    //Log.e(TAG, "handleResults: " + routesArray.getJSONObject(i));
                    JSONObject jsonDir = directionsJsonArray.getJSONObject(i);
                    //Log.e(TAG, "handleResults: " + jsonRoute);
                    String id = jsonDir.getString("id");
                    String name = jsonDir.getString("name");
                    Log.e(TAG, "handleResults: " + id  + " " + name);
                    resultMap.put(id,name);
                }
                //Log.e(TAG, "handleResults: " + resultMap);
            }
            mainActivity.updateDirectionsMap(resultMap);


        } catch (Exception e) {
            //responseTV.setText(MessageFormat.format("Response: {0}", e.getMessage()));
            Log.e(TAG, "onResponse: " + MessageFormat.format("Response: {0}", e.getMessage()) );
        }
    }
}
