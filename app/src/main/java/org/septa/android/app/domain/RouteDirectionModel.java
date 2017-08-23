package org.septa.android.app.domain;

import java.io.Serializable;

/**
 * Created by jkampf on 8/15/17.
 */

public class RouteDirectionModel implements Serializable {
    private static final String TAG = RouteDirectionModel.class.getName();

    private String routeId;
    private String routeShortName;
    private String routeLongName;
    private String directionDescription;
    private String directionCode;
    private Number routeType;

    public RouteDirectionModel(String routeId, String routeShortName, String routeLongName, String directionDescription, String directionCode, Number routeType) {
        this.routeId = routeId;
        this.routeShortName = routeShortName;
        this.routeLongName = routeLongName;
        this.directionDescription = directionDescription;
        this.directionCode = directionCode;
        this.routeType = routeType;
    }

    public String getRouteId() {
        return routeId;
    }

    public String getRouteShortName() {
        return routeShortName;
    }

    public String getRouteLongName() {
        return routeLongName;
    }

    public Number getRouteType() {
        return routeType;
    }

    public String getDirectionDescription() {
        return directionDescription;
    }

    public String getDirectionCode() {
        return directionCode;
    }
}