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


public class PredictionDownloader {
    private static final String TAG = "predictionDownloader";
    private static long start;
    private static final String BASE_URL_PRD = "https://www.ctabustracker.com/bustime/api/v3/getpredictions";

    public static void getPrediction(MainActivity mainActivity) {

        RequestQueue queue;
        queue = Volley.newRequestQueue(mainActivity.createDeviceProtectedStorageContext());

        Uri.Builder buildURL = Uri.parse(BASE_URL_PRD).buildUpon();

        buildURL.appendQueryParameter("format", "json");
        buildURL.appendQueryParameter("key", mainActivity.getYourAPIKey());
        buildURL.appendQueryParameter("rt", mainActivity.getFinalRoute());
        //buildURL.appendQueryParameter("rtdir", stpidET.getText().toString());
        buildURL.appendQueryParameter("stpid", mainActivity.getFinalStop());
        buildURL.appendQueryParameter("rtpidatafeed", "bustime");
        String urlToUse = buildURL.build().toString();

        start = System.currentTimeMillis();

        Response.Listener<JSONObject> listener =
                response -> {
                    handleResults(mainActivity, urlToUse, response);
                };
        Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(error.networkResponse.data));
                    Log.d(TAG, "onErrorResponse:" + MessageFormat.format("Error: {0}", jsonObject.toString()));
                    mainActivity.setTitle("Duration: " + (System.currentTimeMillis() - start));
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

    private static void handleResults(MainActivity mainActivity, String urlToUse, JSONObject response) {
        try {
            //mainActivity.responseTV.setText(MessageFormat.format("Response: {0}", response.toString()));
            Log.d(TAG, "onResponse: " + urlToUse);
            Log.d(TAG, "getPrediction: " + response);
            Log.d(TAG, MessageFormat.format(
                    "Duration: {0} ms", System.currentTimeMillis() - start));
            parseJSON(response, mainActivity);
        } catch (Exception e) {
            //mainActivity.responseTV.setText(MessageFormat.format("Response: {0}", e.getMessage()));
            Log.d(TAG, "handleResults: " + MessageFormat.format("Response: {0}", e.getMessage()));
        }
    }

    private static void parseJSON(JSONObject response, MainActivity mainActivity) throws JSONException {
        JSONObject bustimeResponse = response.getJSONObject("bustime-response");
        if(bustimeResponse.has("error")){
            JSONObject errorJSON = bustimeResponse.getJSONArray("error").getJSONObject(0);
            Log.d(TAG, "parseJSON: " + errorJSON.getString("msg"));
            return;
        }

        mainActivity.predictionList.clear();
        JSONArray predictions = bustimeResponse.getJSONArray("prd");
        int responseCount = predictions.length();
        for (int i = 0; i<predictions.length(); i++) {
            Log.d(TAG, "parseJSON: PRED "+ predictions.get(i));
            JSONObject predictionJSON = predictions.getJSONObject(i);
            Log.d(TAG, "parseJSON: PRED Route "+ predictionJSON.getString("rt"));
            Log.d(TAG, "parseJSON: PRED STOP NAME "+ predictionJSON.getString("stpnm"));
            Log.d(TAG, "parseJSON: PRED DES "+ predictionJSON.getString("des"));
            Log.d(TAG, "parseJSON: PRED STOP ID "+ predictionJSON.getString("stpid"));
            Log.d(TAG, "parseJSON: PRED TIME "+ predictionJSON.getString("prdtm"));
            Log.d(TAG, "parseJSON: PRED VEHICLE NO "+ predictionJSON.getString("vid"));
            Log.d(TAG, "parseJSON: PRED LINEAR DIST "+ predictionJSON.getString("dstp"));
            Log.d(TAG, "parseJSON: PRED DIRECTION "+ predictionJSON.getString("rtdir"));

            String rt = predictionJSON.getString("rt");
            String stpnm = predictionJSON.getString("stpnm");
            String des = predictionJSON.getString("des");
            String stpid = predictionJSON.getString("stpid");
            String prdtm = predictionJSON.getString("prdtm");
            String vid = predictionJSON.getString("vid");
            String dstp = predictionJSON.getString("dstp");
            String rtdir =  predictionJSON.getString("rtdir");

            Prediction pred = new Prediction(rt, stpnm, des, stpid, prdtm, vid, dstp, rtdir);

            mainActivity.updateData(pred);

            Log.e(TAG, "parseJSON: "+ pred);
        }
        Log.d(TAG, "parseJSON: "+ predictions);

        Log.d(TAG, "parseJSON: "+ responseCount);
    }

}
