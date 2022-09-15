package md.models;


import md.constants.Direction;

public class PassengerRequest {

    private int targetFloor;
    private Direction requestDirection;

    private double currentWeight;

    public PassengerRequest() {
        // nothing to do here
    }

    public PassengerRequest(int targetFloor, Direction requestDirection, double currentWeight) {
        this.targetFloor = targetFloor;
        this.requestDirection = requestDirection;
        this.currentWeight = currentWeight;
    }

    public int getTargetFloor() {
        return this.targetFloor;
    }

    public void setTargetFloor(int targetFloor) {
        this.targetFloor = targetFloor;
    }

    public Direction getRequestDirection() {
        return requestDirection;
    }

    public void setRequestDirection(Direction requestDirection) {
        this.requestDirection = requestDirection;
    }

    public double getCurrentWeight() {
        return currentWeight;
    }

    public void setCurrentWeight(double currentWeight) {

        this.currentWeight = currentWeight;
    }
}
