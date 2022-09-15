package services;

import org.constants.Direction;
import org.constants.ElevatorStatus;
import org.exceptions.impl.InvalidWeightException;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Tag;
import org.services.ElevatorController;
import org.models.PassengerElevator;
import org.models.PassengerRequest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Tag("unit-test")
public class ElevatorControllerTest {

    PassengerElevator passengerElevator;

    @Before
    public void setup() {
    }


    @Test
    public void addOneFloor_validWeightArgument_success() {

        passengerElevator = new PassengerElevator();
        ElevatorController elevatorController = new ElevatorController(passengerElevator);

        boolean flag = elevatorController.addFloor(new PassengerRequest(1, Direction.UP, 200));
        assertTrue(flag);

    }

    @Test(expected = InvalidWeightException.class)
    public void addOneFloor_invalidWeightArgument_throwInvalidWeightException() {

        passengerElevator = new PassengerElevator();
        ElevatorController elevatorController = new ElevatorController(passengerElevator);

        elevatorController.addFloor(new PassengerRequest(1, Direction.UP, 900));

    }

    @Test
    public void addMultipleFloors_validArguments_success() {
        passengerElevator = new PassengerElevator();
        ElevatorController elevatorController = new ElevatorController(passengerElevator);

        boolean firstAdd = elevatorController.addFloor(new PassengerRequest(1, Direction.UP, 300));
        boolean sectAdd = elevatorController.addFloor(new PassengerRequest(4, Direction.UP, 350));
        boolean thirdAdd = elevatorController.addFloor(new PassengerRequest(3, Direction.UP, 400));

        assertTrue(firstAdd && sectAdd && thirdAdd);

    }

    @Test
    public void addMultipleFloorsMultipleDirections_validArguments_success() {
        passengerElevator = new PassengerElevator();
        ElevatorController elevatorController = new ElevatorController(passengerElevator);

        boolean firstAdd = elevatorController.addFloor(new PassengerRequest(1, Direction.UP, 300));
        boolean sectAdd = elevatorController.addFloor(new PassengerRequest(4, Direction.UP, 350));
        boolean thirdAdd = elevatorController.addFloor(new PassengerRequest(2, Direction.DOWN, 350));
        boolean fourthAdd = elevatorController.addFloor(new PassengerRequest(3, Direction.UP, 400));
        boolean fifthAdd = elevatorController.addFloor(new PassengerRequest(2, Direction.DOWN, 350));


        assertTrue(firstAdd && sectAdd && thirdAdd && fourthAdd && fifthAdd);

    }

    @Test
    public void addMultipleFloorsAndGetNextFloorWhenLiftIsIdle_validArguments_success() {
        passengerElevator = new PassengerElevator();
        ElevatorController elevatorController = new ElevatorController(passengerElevator);

        elevatorController.addFloor(new PassengerRequest(6, Direction.UP, 300));
        elevatorController.addFloor(new PassengerRequest(-1, Direction.DOWN, 350));
        elevatorController.addFloor(new PassengerRequest(4, Direction.UP, 350));
        elevatorController.addFloor(new PassengerRequest(2, Direction.DOWN, 350));
        elevatorController.addFloor(new PassengerRequest(3, Direction.UP, 400));

        int nextFloor = elevatorController.getNextFloor();
        // should expect -1, since the algorithm chooses the nearest floor to move if the lift is idle
        assertEquals(-1, nextFloor);

    }

    @Test
    public void addMultipleFloorsAndGetNextFloorWhenLiftIsMoving_validArguments_success() {
        passengerElevator = new PassengerElevator();
        passengerElevator.setCurrentFloor(1);
        passengerElevator.setMotionStatus(ElevatorStatus.MOVING);
        passengerElevator.setMovingDirection(Direction.UP);
        ElevatorController elevatorController = new ElevatorController(passengerElevator);

        elevatorController.addFloor(new PassengerRequest(6, Direction.UP, 300));
        elevatorController.addFloor(new PassengerRequest(-1, Direction.DOWN, 350));
        elevatorController.addFloor(new PassengerRequest(4, Direction.UP, 350));
        elevatorController.addFloor(new PassengerRequest(2, Direction.DOWN, 350));
        elevatorController.addFloor(new PassengerRequest(3, Direction.UP, 400));

        int nextFloor = elevatorController.getNextFloor();
        // should expect 2, since the algorithm chooses the nearest floor in the moving direction to move if the lift is on moving mode
        assertEquals(2, nextFloor);

    }

    @Test
    public void startFromMiddleFloorAddMultipleFloorsAndGetNextFloor_validArguments_success() {
        passengerElevator = new PassengerElevator();
        passengerElevator.setCurrentFloor(2);
        ElevatorController elevatorController = new ElevatorController(passengerElevator);

        elevatorController.addFloor(new PassengerRequest(6, Direction.UP, 300));
        elevatorController.addFloor(new PassengerRequest(-1, Direction.DOWN, 350));
        elevatorController.addFloor(new PassengerRequest(4, Direction.UP, 350));
        elevatorController.addFloor(new PassengerRequest(1, Direction.DOWN, 350));
        elevatorController.addFloor(new PassengerRequest(3, Direction.UP, 400));

        int nextFloor = elevatorController.getNextFloor();
        assertEquals(1, nextFloor);

    }

    @Test
    public void startFromGroundEquidistantFromStartingFloorAddMultipleFloorsAndMove_validArguments_success() {
        passengerElevator = new PassengerElevator();
        ElevatorController elevatorController = new ElevatorController(passengerElevator);

        elevatorController.addFloor(new PassengerRequest(6, Direction.UP, 300));
        elevatorController.addFloor(new PassengerRequest(-1, Direction.DOWN, 350));
        elevatorController.addFloor(new PassengerRequest(4, Direction.UP, 350));
        elevatorController.addFloor(new PassengerRequest(1, Direction.DOWN, 350));
        elevatorController.addFloor(new PassengerRequest(3, Direction.UP, 400));
        elevatorController.addFloor(new PassengerRequest(2, Direction.DOWN, 350));

        try {
            elevatorController.moveLift();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void startFromGroundMultiDistantFromStartingFloorAddMultipleFloorsAndMove_validArguments_success() {
        passengerElevator = new PassengerElevator();
        ElevatorController elevatorController = new ElevatorController(passengerElevator);

        elevatorController.addFloor(new PassengerRequest(6, Direction.UP, 300));
        elevatorController.addFloor(new PassengerRequest(-1, Direction.DOWN, 350));
        elevatorController.addFloor(new PassengerRequest(4, Direction.UP, 350));
        elevatorController.addFloor(new PassengerRequest(8, Direction.DOWN, 350));
        elevatorController.addFloor(new PassengerRequest(3, Direction.UP, 400));
        elevatorController.addFloor(new PassengerRequest(2, Direction.DOWN, 350));

        try {
            elevatorController.moveLift();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void startFromMiddleFloorAddMultipleFloorsAndMove_validArguments_success() {
        passengerElevator = new PassengerElevator();
        passengerElevator.setCurrentFloor(3);
        ElevatorController elevatorController = new ElevatorController(passengerElevator);

        elevatorController.addFloor(new PassengerRequest(6, Direction.UP, 300));
        elevatorController.addFloor(new PassengerRequest(-1, Direction.DOWN, 350));
        elevatorController.addFloor(new PassengerRequest(4, Direction.UP, 350));
        elevatorController.addFloor(new PassengerRequest(1, Direction.DOWN, 350));
        elevatorController.addFloor(new PassengerRequest(3, Direction.UP, 400));
        elevatorController.addFloor(new PassengerRequest(2, Direction.DOWN, 350));

        try {
            elevatorController.moveLift();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void startFromMiddleEquidistantFloorAddMultipleFloorsAndMove_validArguments_success() {
        passengerElevator = new PassengerElevator();
        passengerElevator.setCurrentFloor(3);
        ElevatorController elevatorController = new ElevatorController(passengerElevator);

        elevatorController.addFloor(new PassengerRequest(6, Direction.UP, 300));
        elevatorController.addFloor(new PassengerRequest(-1, Direction.DOWN, 350));
        elevatorController.addFloor(new PassengerRequest(4, Direction.UP, 350));
        elevatorController.addFloor(new PassengerRequest(1, Direction.DOWN, 350));
        elevatorController.addFloor(new PassengerRequest(3, Direction.UP, 400));
        elevatorController.addFloor(new PassengerRequest(2, Direction.DOWN, 350));

        try {
            elevatorController.moveLift();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
