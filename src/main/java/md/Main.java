package md;

import md.constants.Direction;
import md.models.PassengerElevator;
import md.models.PassengerRequest;
import md.services.ElevatorControlService;

public class Main {
    public static void main(String[] args) {

        PassengerElevator passengerElevator = new PassengerElevator();
        ElevatorControlService elevatorControlService = new ElevatorControlService(passengerElevator);
        elevatorControlService.addFloor(new PassengerRequest(6, Direction.UP, 300));
        elevatorControlService.addFloor(new PassengerRequest(-1, Direction.DOWN, 350));
        elevatorControlService.addFloor(new PassengerRequest(4, Direction.UP, 350));

        try {
            elevatorControlService.moveLift();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);

        }
    }


}