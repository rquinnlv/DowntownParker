package com.zappos.downtown.parker.model;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * This model class represents a collection of parking garage structure
 */
public class GarageData {

    @JsonProperty("garage north")
    private Garage northGarage;

    @JsonProperty("garage south")
    private Garage southGarage;

    @JsonProperty("parking lot")
    private Garage parkingLot;

    public Garage getNorthGarage() {
        return northGarage;
    }

    public void setNorthGarage(Garage northGarage) {
        this.northGarage = northGarage;
    }

    public Garage getSouthGarage() {
        return southGarage;
    }

    public void setSouthGarage(Garage southGarage) {
        this.southGarage = southGarage;
    }

    public Garage getParkingLot() {
        return parkingLot;
    }

    public void setParkingLot(Garage parkingLot) {
        this.parkingLot = parkingLot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GarageData)) return false;

        GarageData that = (GarageData) o;

        if (northGarage != null ? !northGarage.equals(that.northGarage) : that.northGarage != null)
            return false;
        if (parkingLot != null ? !parkingLot.equals(that.parkingLot) : that.parkingLot != null)
            return false;
        if (southGarage != null ? !southGarage.equals(that.southGarage) : that.southGarage != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = northGarage != null ? northGarage.hashCode() : 0;
        result = 31 * result + (southGarage != null ? southGarage.hashCode() : 0);
        result = 31 * result + (parkingLot != null ? parkingLot.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "GarageData{" +
                "northGarage=" + northGarage +
                ", southGarage=" + southGarage +
                ", parkingLot=" + parkingLot +
                '}';
    }
}
