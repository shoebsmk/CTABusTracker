package com.example.expcta;

public class Prediction {
    private String route; //rt
    private String stopName; //stpnm
    private String description; //des
    private String stopId; //stpid --
    private String predictionTime;  //prdtm
    private String vehicleNumber;  //vid --
    private String linearDistance;  //dstp --
    private String direction;  //rtdir

    public Prediction(String route, String stopName, String description, String stopId, String predictionTime, String vehicleNumber, String linearDistance, String direction) {
        this.route = route;
        this.stopName = stopName;
        this.description = description;
        this.stopId = stopId;
        this.predictionTime = predictionTime;
        this.vehicleNumber = vehicleNumber;
        this.linearDistance = linearDistance;
        this.direction = direction;
    }

    public String getRoute() {
        return route;
    }

    public String getStopName() {
        return stopName;
    }

    public String getDescription() {
        return description;
    }

    public String getStopId() {
        return stopId;
    }

    public String getPredictionTime() {
        return predictionTime;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public String getLinearDistance() {
        return linearDistance;
    }

    public String getDirection() {
        return direction;
    }

    @Override
    public String toString() {
        return "Prediction{" +
                "route='" + route + '\'' +
                ", stopName='" + stopName + '\'' +
                ", description='" + description + '\'' +
                ", stopId='" + stopId + '\'' +
                ", predictionTime='" + predictionTime + '\'' +
                ", vehicleNumber='" + vehicleNumber + '\'' +
                ", linearDistance='" + linearDistance + '\'' +
                ", direction='" + direction + '\'' +
                '}';
    }
}
