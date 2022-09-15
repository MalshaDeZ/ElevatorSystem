package md.models;

import md.constants.Direction;
import md.constants.ElevatorStatus;

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
