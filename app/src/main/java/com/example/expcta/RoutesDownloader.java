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

public class RoutesDownloader {
    private static final String BASE_URL_GET_ROUTES = "https://www.ctabustracker.com/bustime/api/v3/getroutes";

    //JSONObject routesResponse;
    private static final String TAG = "routesDownloader";



    public static void getAllRoutes(MainActivity mainActivity){
        RequestQueue queue = Volley.newRequestQueue(mainActivity);

        Uri.Builder buildURL = Uri.parse(BASE_URL_GET_ROUTES).buildUpon();
        buildURL.appendQueryParameter("format", "json");
        buildURL.appendQueryParameter("key", mainActivity.yourAPIKey);
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
                JSONArray routesArray = ((JSONObject)response.get("bustime-response")).getJSONArray("routes");
                Log.e(TAG, "onResponse: "  +  routesArray);

                for(int i = 0; i< routesArray.length();i++ ){
                    //Log.e(TAG, "handleResults: " + routesArray.getJSONObject(i));
                    JSONObject jsonRoute = routesArray.getJSONObject(i);
                    //Log.e(TAG, "handleResults: " + jsonRoute);
                    String rt = jsonRoute.getString("rt");
                    String rtnm = jsonRoute.getString("rtnm");
                    //Log.e(TAG, "handleResults: " + rt  + " " + rtnm);
                    resultMap.put(rt,rtnm);
                }
                //Log.e(TAG, "handleResults: " + resultMap);
            }
            mainActivity.updateRouteMap(resultMap);


        } catch (Exception e) {
            //responseTV.setText(MessageFormat.format("Response: {0}", e.getMessage()));
            Log.e(TAG, "onResponse: " + MessageFormat.format("Response: {0}", e.getMessage()) );
        }
    }

}
