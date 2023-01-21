package com.example.expcta;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.MessageFormat;
import java.util.List;

public class PredictionAdapter extends RecyclerView.Adapter<PredictionViewHolder> {

    private static final String TAG = "PredictionAdapter";
    private final List<Prediction> predictionList;
    private final MainActivity mainAct;

    public PredictionAdapter(List<Prediction> predictionList, MainActivity mainAct) {
        this.predictionList = predictionList;
        this.mainAct = mainAct;
    }

    @NonNull
    @Override
    public PredictionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pred_entry, parent, false);
        return new PredictionViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull PredictionViewHolder holder, int position) {
        Prediction prediction = predictionList.get(position);
        holder.routeTV.setText(prediction.getRoute());
        holder.stopNameTV.setText(prediction.getStopName());
        holder.descriptionTV.setText(prediction.getDescription());
        String[] parts = prediction.getPredictionTime().split(" ");
        String predTime = parts[parts.length - 1];
        holder.predictionTimeTV.setText( predTime);
        holder.directionTV.setText(prediction.getDirection());
        holder.moreInfoTV.setText(MessageFormat.format("Vehicle #{0} is {1}ft Away from stop {2}",
                prediction.getVehicleNumber(),
                prediction.getLinearDistance(),
                prediction.getStopId()));
    }

    @Override
    public int getItemCount() {
        return predictionList.size();
    }
}
