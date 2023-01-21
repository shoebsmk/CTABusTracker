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

public class StopsDownloader {
    private static final String BASE_URL_GET_STOPS = "https://www.ctabustracker.com/bustime/api/v3/getstops";
    private static final String TAG = "StopsDownloader";

    public static void getStopsForRoute(MainActivity mainActivity, String route, String dir){

        RequestQueue queue = Volley.newRequestQueue(mainActivity);

        Uri.Builder buildURL = Uri.parse(BASE_URL_GET_STOPS).buildUpon();
        buildURL.appendQueryParameter("format", "json");
        buildURL.appendQueryParameter("key", mainActivity.yourAPIKey);
        buildURL.appendQueryParameter("rt", route);
        buildURL.appendQueryParameter("dir", dir);
        String urlToUse = buildURL.build().toString();

        Response.Listener<JSONObject> listener =
                response -> handleResults(mainActivity,response, urlToUse);
        Response.ErrorListener error = error1 -> {
            try {
                JSONObject jsonObject = new JSONObject(new String(error1.networkResponse.data));
                //responseTV.setText(MessageFormat.format("Error: {0}", jsonObject.toString()));
                Log.e(TAG, "onErrorResponse: " + MessageFormat.format("Error: {0}", jsonObject.toString()) );

            } catch (JSONException e) {
                e.printStackTrace();
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
                JSONArray stopsJsonArray = ((JSONObject)response.get("bustime-response")).getJSONArray("stops");
                Log.e(TAG, "onResponse: "  +  stopsJsonArray);

                for(int i = 0; i< stopsJsonArray.length();i++ ){
                    //Log.e(TAG, "handleResults: " + stopsJsonArray.getJSONObject(i));
                    JSONObject jsonRoute = stopsJsonArray.getJSONObject(i);
                    //Log.e(TAG, "handleResults: " + jsonRoute);
                    String rt = jsonRoute.getString("stpid");
                    String rtnm = jsonRoute.getString("stpnm");
                    resultMap.put(rt,rtnm);
                }
            }
            mainActivity.updateStopsMap(resultMap);


        } catch (Exception e) {
            Log.e(TAG, "onResponse: " + MessageFormat.format("Response: {0}", e.getMessage()) );
        }
    }

}
