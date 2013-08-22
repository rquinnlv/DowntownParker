package com.zappos.downtown.parker.model;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * This model class represents a single floor of a parking garage
 */
public class Floor {

    @JsonProperty("name")
    private String name;

    @JsonProperty("spots_available")
    private int spotsAvailable;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSpotsAvailable() {
        return spotsAvailable;
    }

    public void setSpotsAvailable(int spotsAvailable) {
        this.spotsAvailable = spotsAvailable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Floor)) return false;

        Floor floor = (Floor) o;

        if (spotsAvailable != floor.spotsAvailable) return false;
        if (name != null ? !name.equals(floor.name) : floor.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + spotsAvailable;
        return result;
    }

    @Override
    public String toString() {
        return "Floor{" +
                "name='" + name + '\'' +
                ", spotsAvailable=" + spotsAvailable +
                '}';
    }
}
