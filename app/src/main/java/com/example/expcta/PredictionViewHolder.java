package com.example.expcta;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PredictionViewHolder extends RecyclerView.ViewHolder {
    TextView routeTV;
    TextView stopNameTV;
    TextView descriptionTV;
    TextView predictionTimeTV;
    TextView directionTV;
    TextView moreInfoTV;
    public PredictionViewHolder(@NonNull View itemView) {
        super(itemView);
        routeTV = itemView.findViewById(R.id.routeNoTV);
        stopNameTV = itemView.findViewById(R.id.stopNameTV);
        descriptionTV = itemView.findViewById(R.id.descriptionTV);
        predictionTimeTV = itemView.findViewById(R.id.predictionTimeTV);
        directionTV = itemView.findViewById(R.id.directionTV);
        moreInfoTV = itemView.findViewById(R.id.moreInfoTV);
    }
}
