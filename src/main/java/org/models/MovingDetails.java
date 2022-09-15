package org.models;

import org.constants.Direction;

public class MovingDetails {
    private int floorNumber;
    private Direction direction;

    public MovingDetails() {
    }

    public MovingDetails(int floorNumber, Direction direction) {
        this.floorNumber = floorNumber;
        this.direction = direction;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public Direction getDirection() {
        return direction;
    }
}
