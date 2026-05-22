package com.app.peach.userLocation.dto;

public class FeedRequestDTO {

    private Double xCoordinate;
    private Double yCoordinate;
    private Double range;

    public Double getXCoordinate() {
        return xCoordinate;
    }

    public FeedRequestDTO setxCoordinate(Double xCoordinate) {
        this.xCoordinate = xCoordinate;
        return this;
    }

    public Double getYCoordinate() {
        return yCoordinate;
    }

    public FeedRequestDTO setyCoordinate(Double yCoordinate) {
        this.yCoordinate = yCoordinate;
        return this;
    }

    public Double getRange() {
        return range;
    }

    public FeedRequestDTO setRange(Double range) {
        this.range = range;
        return this;
    }

    @Override
    public String toString() {
        return "FeedRequestDTO{" +
                "xCoordinate=" + xCoordinate +
                ", yCoordinate=" + yCoordinate +
                ", range=" + range +
                '}';
    }
}
