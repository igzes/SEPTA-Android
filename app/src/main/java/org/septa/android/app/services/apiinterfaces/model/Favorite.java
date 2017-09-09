package org.septa.android.app.services.apiinterfaces.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import org.septa.android.app.TransitType;
import org.septa.android.app.domain.RouteDirectionModel;
import org.septa.android.app.domain.StopModel;

import java.io.Serializable;

/**
 * Created by jkampf on 9/6/17.
 */

public class Favorite implements Serializable {
    @SerializedName("name")
    private String name;

    @SerializedName("start")
    private StopModel start;
    @SerializedName("destination")
    private StopModel destination;
    @SerializedName("route")
    RouteDirectionModel routeDirectionModel;
    @SerializedName("transit_type")
    private TransitType transitType;


    public Favorite(@NonNull StopModel start, @NonNull StopModel destination, @NonNull TransitType transitType, @Nullable RouteDirectionModel routeDirectionModel) {
        this.start = start;
        this.destination = destination;
        this.transitType = transitType;
        this.routeDirectionModel = routeDirectionModel;

        this.transitType = transitType;
        if (routeDirectionModel != null) {
            name = routeDirectionModel.getRouteShortName() + " to " + destination.getStopName();
        } else {
            name = "To " + destination.getStopName();
        }

    }

    public String getKey() {
        return generateKey(start, destination, transitType, routeDirectionModel);
    }

    public static String generateKey(TransitType transitType, String startId, String destinationId, String lineId, String directionCode) {
        return transitType.name() + "_" + startId + "_" + destinationId + "_" + lineId + "_" + directionCode;
    }

    public static String generateKey(@NonNull StopModel start, @NonNull StopModel destination, @NonNull TransitType transitType, @Nullable RouteDirectionModel routeDirectionModel) {
        String startId = start.getStopId();
        String destinationId = destination.getStopId();
        String lineId = null;
        String directionCode = null;
        if (routeDirectionModel != null) {
            lineId = routeDirectionModel.getRouteId();
            directionCode = routeDirectionModel.getDirectionCode();
        }

        return generateKey(transitType, startId, destinationId, lineId, directionCode);
    }

    public String getName() {
        return name;
    }

    public StopModel getStart() {
        return start;
    }

    public StopModel getDestination() {
        return destination;
    }

    public TransitType getTransitType() {
        return transitType;
    }

    public RouteDirectionModel getRouteDirectionModel() {
        return routeDirectionModel;
    }
}

