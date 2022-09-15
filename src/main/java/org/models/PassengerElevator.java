package org.models;


import org.constants.Direction;
import org.constants.ElevatorStatus;
import org.models.Elevator;

public class PassengerElevator implements Elevator {

    private static final double MAX_ELEVATOR_CAPACITY = 400; // total capacity in Kg
    private int currentFloor;

    private Direction movingDirection;
    private ElevatorStatus motionStatus;
    private boolean emergency;

    private double currentWeight = 0; // should ber in Kg

    public PassengerElevator() {

        this.currentFloor = 0;
        this.motionStatus = ElevatorStatus.IDLE;

    }


    @Override
    public void setCurrentFloor(int floor) {
        this.currentFloor = floor;
    }

    @Override
    public void setMovingDirection(Direction direction) {
        this.movingDirection = direction;
    }

    @Override
    public void setMotionStatus(ElevatorStatus motionStatus) {
        this.motionStatus = motionStatus;
    }

    @Override
    public boolean isEmergency() {
        return this.emergency;
    }

    @Override
    public boolean closeDoor() {

        if (currentWeight < MAX_ELEVATOR_CAPACITY) {
            //sends the command to close the elevator door
            return true;
        }
        //sends warning
        return false;
    }

    @Override
    public boolean activateEmergency() {
        this.emergency = true;
        // activate emergency protocol
        return true;
    }

    @Override
    public boolean move() {
        //sends signal to move the elevator to move to the floor
        return true;
    }

    public boolean openDoor() {
        // open the door
        this.motionStatus = ElevatorStatus.LOADING;
        // sends signal to open the door
        return true;
    }


    public int getCurrentFloor() {
        return currentFloor;
    }

    public Direction getMovingDirection() {
        return movingDirection;
    }

    @Override
    public double getWeight() {
        return this.currentWeight;
    }

    @Override
    public double getCapacity() {
        return MAX_ELEVATOR_CAPACITY;
    }

    public ElevatorStatus getMotionStatus() {
        return motionStatus;
    }

    public void setEmergency(boolean emergency) {
        this.emergency = emergency;
    }

    public double getCurrentWeight() {
        return currentWeight;
    }

    public void setCurrentWeight(double currentWeight) {
        this.currentWeight = currentWeight;
    }
}
