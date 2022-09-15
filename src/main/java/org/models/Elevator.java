package org.models;

import org.constants.Direction;
import org.constants.ElevatorStatus;

public interface Elevator {


    void setCurrentFloor(int floor);

    void setMovingDirection(Direction direction);

    void setMotionStatus(ElevatorStatus motionStatus);

    boolean isEmergency();

    boolean openDoor();

    boolean closeDoor();

    boolean activateEmergency();

    boolean move();

    int getCurrentFloor();

    Direction getMovingDirection();
    ElevatorStatus getMotionStatus();

    double getWeight();

    double getCapacity();
}
