package com.zappos.downtown.parker.model;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

/**
 * This model class represents a single parking garage structure
 */
public class Garage {

    @JsonProperty("open_spots")
    private int openSpots;

    @JsonProperty("floors")
    private List<Floor> floors;

    public int getOpenSpots() {
        return openSpots;
    }

    public void setOpenSpots(int openSpots) {
        this.openSpots = openSpots;
    }

    public List<Floor> getFloors() {
        return floors;
    }

    public void setFloors(List<Floor> floors) {
        this.floors = floors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Garage)) return false;

        Garage garage = (Garage) o;

        if (openSpots != garage.openSpots) return false;
        if (floors != null ? !floors.equals(garage.floors) : garage.floors != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = openSpots;
        result = 31 * result + (floors != null ? floors.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Garage{" +
                "openSpots=" + openSpots +
                ", floors=" + floors +
                '}';
    }
}
