package com.ucm.informatica.spread.Model;


import com.mapbox.geojson.Point;

import java.util.List;

public class Region {
    private int countContainedPoints;
    private List<Point> polygonCoordinatesList;

    public Region(List<Point> dataList){
        this.countContainedPoints = 0;
        this.polygonCoordinatesList = dataList;
    }

    public int getCountContainedPoints() {
        return countContainedPoints;
    }

    public List<Point> getPolygonCoordinatesList() {
        return polygonCoordinatesList;
    }

    public void increaseContainedPoints(){
        countContainedPoints++;
    }
}
