package org;

import org.constants.Direction;
import org.models.PassengerElevator;
import org.models.PassengerRequest;
import org.services.ElevatorController;

public class Main {
    public static void main(String[] args) {

        PassengerElevator passengerElevator = new PassengerElevator();
        ElevatorController elevatorController = new ElevatorController(passengerElevator);
        elevatorController.addFloor(new PassengerRequest(6, Direction.UP, 300));
        elevatorController.addFloor(new PassengerRequest(-1, Direction.DOWN, 350));
        elevatorController.addFloor(new PassengerRequest(4, Direction.UP, 350));

        try {
            elevatorController.moveLift();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);

        }
    }


}