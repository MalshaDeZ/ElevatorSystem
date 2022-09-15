package md.services;

import md.constants.Direction;
import md.constants.ElevatorStatus;
import md.exceptions.impl.InvalidWeightException;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Tag;
import md.models.PassengerElevator;
import md.models.PassengerRequest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Tag("unit-test")
public class ElevatorControlServiceTest {

    PassengerElevator passengerElevator;

    @Before
    public void setup() {
    }


    @Test
    public void addOneFloor_validWeightArgument_success() {

        passengerElevator = new PassengerElevator();
        ElevatorControlService elevatorControlService = new ElevatorControlService(passengerElevator);

        boolean flag = elevatorControlService.addFloor(new PassengerRequest(1, Direction.UP, 200));
        assertTrue(flag);

    }

    @Test(expected = InvalidWeightException.class)
    public void addOneFloor_invalidWeightArgument_throwInvalidWeightException() {

        passengerElevator = new PassengerElevator();
        ElevatorControlService elevatorControlService = new ElevatorControlService(passengerElevator);

        elevatorControlService.addFloor(new PassengerRequest(1, Direction.UP, 900));

    }

    @Test
    public void addMultipleFloors_validArguments_success() {
        passengerElevator = new PassengerElevator();
        ElevatorControlService elevatorControlService = new ElevatorControlService(passengerElevator);

        boolean firstAdd = elevatorControlService.addFloor(new PassengerRequest(1, Direction.UP, 300));
        boolean sectAdd = elevatorControlService.addFloor(new PassengerRequest(4, Direction.UP, 350));
        boolean thirdAdd = elevatorControlService.addFloor(new PassengerRequest(3, Direction.UP, 400));

        assertTrue(firstAdd && sectAdd && thirdAdd);

    }

    @Test
    public void addMultipleFloorsMultipleDirections_validArguments_success() {
        passengerElevator = new PassengerElevator();
        ElevatorControlService elevatorControlService = new ElevatorControlService(passengerElevator);

        boolean firstAdd = elevatorControlService.addFloor(new PassengerRequest(1, Direction.UP, 300));
        boolean sectAdd = elevatorControlService.addFloor(new PassengerRequest(4, Direction.UP, 350));
        boolean thirdAdd = elevatorControlService.addFloor(new PassengerRequest(2, Direction.DOWN, 350));
        boolean fourthAdd = elevatorControlService.addFloor(new PassengerRequest(3, Direction.UP, 400));
        boolean fifthAdd = elevatorControlService.addFloor(new PassengerRequest(2, Direction.DOWN, 350));


        assertTrue(firstAdd && sectAdd && thirdAdd && fourthAdd && fifthAdd);

    }

    @Test
    public void addMultipleFloorsAndGetNextFloorWhenLiftIsIdle_validArguments_success() {
        passengerElevator = new PassengerElevator();
        ElevatorControlService elevatorControlService = new ElevatorControlService(passengerElevator);

        elevatorControlService.addFloor(new PassengerRequest(6, Direction.UP, 300));
        elevatorControlService.addFloor(new PassengerRequest(-1, Direction.DOWN, 350));
        elevatorControlService.addFloor(new PassengerRequest(4, Direction.UP, 350));
        elevatorControlService.addFloor(new PassengerRequest(2, Direction.DOWN, 350));
        elevatorControlService.addFloor(new PassengerRequest(3, Direction.UP, 400));

        int nextFloor = elevatorControlService.getNextFloor();
        // should expect -1, since the algorithm chooses the nearest floor to move if the lift is idle
        assertEquals(-1, nextFloor);

    }

    @Test
    public void addMultipleFloorsAndGetNextFloorWhenLiftIsMoving_validArguments_success() {
        passengerElevator = new PassengerElevator();
        passengerElevator.setCurrentFloor(1);
        passengerElevator.setMotionStatus(ElevatorStatus.MOVING);
        passengerElevator.setMovingDirection(Direction.UP);
        ElevatorControlService elevatorControlService = new ElevatorControlService(passengerElevator);

        elevatorControlService.addFloor(new PassengerRequest(6, Direction.UP, 300));
        elevatorControlService.addFloor(new PassengerRequest(-1, Direction.DOWN, 350));
        elevatorControlService.addFloor(new PassengerRequest(4, Direction.UP, 350));
        elevatorControlService.addFloor(new PassengerRequest(2, Direction.DOWN, 350));
        elevatorControlService.addFloor(new PassengerRequest(3, Direction.UP, 400));

        int nextFloor = elevatorControlService.getNextFloor();
        // should expect 2, since the algorithm chooses the nearest floor in the moving direction to move if the lift is on moving mode
        assertEquals(2, nextFloor);

    }

    @Test
    public void startFromMiddleFloorAddMultipleFloorsAndGetNextFloor_validArguments_success() {
        passengerElevator = new PassengerElevator();
        passengerElevator.setCurrentFloor(2);
        ElevatorControlService elevatorControlService = new ElevatorControlService(passengerElevator);

        elevatorControlService.addFloor(new PassengerRequest(6, Direction.UP, 300));
        elevatorControlService.addFloor(new PassengerRequest(-1, Direction.DOWN, 350));
        elevatorControlService.addFloor(new PassengerRequest(4, Direction.UP, 350));
        elevatorControlService.addFloor(new PassengerRequest(1, Direction.DOWN, 350));
        elevatorControlService.addFloor(new PassengerRequest(3, Direction.UP, 400));

        int nextFloor = elevatorControlService.getNextFloor();
        assertEquals(3, nextFloor);

    }

    @Test
    public void startFromGroundEquidistantFromStartingFloorAddMultipleFloorsAndMove_validArguments_success() {
        passengerElevator = new PassengerElevator();
        ElevatorControlService elevatorControlService = new ElevatorControlService(passengerElevator);

        elevatorControlService.addFloor(new PassengerRequest(6, Direction.UP, 300));
        elevatorControlService.addFloor(new PassengerRequest(-1, Direction.DOWN, 350));
        elevatorControlService.addFloor(new PassengerRequest(4, Direction.UP, 350));
        elevatorControlService.addFloor(new PassengerRequest(1, Direction.DOWN, 350));
        elevatorControlService.addFloor(new PassengerRequest(3, Direction.UP, 400));
        elevatorControlService.addFloor(new PassengerRequest(2, Direction.DOWN, 350));

        try {
            elevatorControlService.moveLift();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void startFromGroundMultiDistantFromStartingFloorAddMultipleFloorsAndMove_validArguments_success() {
        passengerElevator = new PassengerElevator();
        ElevatorControlService elevatorControlService = new ElevatorControlService(passengerElevator);

        elevatorControlService.addFloor(new PassengerRequest(6, Direction.UP, 300));
        elevatorControlService.addFloor(new PassengerRequest(-1, Direction.DOWN, 350));
        elevatorControlService.addFloor(new PassengerRequest(4, Direction.UP, 350));
        elevatorControlService.addFloor(new PassengerRequest(8, Direction.DOWN, 350));
        elevatorControlService.addFloor(new PassengerRequest(3, Direction.UP, 400));
        elevatorControlService.addFloor(new PassengerRequest(2, Direction.DOWN, 350));

        try {
            elevatorControlService.moveLift();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void startFromMiddleFloorAddMultipleFloorsAndMove_validArguments_success() {
        passengerElevator = new PassengerElevator();
        passengerElevator.setCurrentFloor(3);
        ElevatorControlService elevatorControlService = new ElevatorControlService(passengerElevator);

        elevatorControlService.addFloor(new PassengerRequest(6, Direction.UP, 300));
        elevatorControlService.addFloor(new PassengerRequest(-1, Direction.DOWN, 350));
        elevatorControlService.addFloor(new PassengerRequest(4, Direction.UP, 350));
        elevatorControlService.addFloor(new PassengerRequest(1, Direction.DOWN, 350));
        elevatorControlService.addFloor(new PassengerRequest(3, Direction.UP, 400));
        elevatorControlService.addFloor(new PassengerRequest(2, Direction.DOWN, 350));

        try {
            elevatorControlService.moveLift();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void startFromMiddleEquidistantFloorAddMultipleFloorsAndMove_validArguments_success() {
        passengerElevator = new PassengerElevator();
        passengerElevator.setCurrentFloor(3);
        ElevatorControlService elevatorControlService = new ElevatorControlService(passengerElevator);

        elevatorControlService.addFloor(new PassengerRequest(6, Direction.UP, 300));
        elevatorControlService.addFloor(new PassengerRequest(-1, Direction.DOWN, 350));
        elevatorControlService.addFloor(new PassengerRequest(4, Direction.UP, 350));
        elevatorControlService.addFloor(new PassengerRequest(1, Direction.DOWN, 350));
        elevatorControlService.addFloor(new PassengerRequest(3, Direction.UP, 400));
        elevatorControlService.addFloor(new PassengerRequest(2, Direction.DOWN, 350));

        try {
            elevatorControlService.moveLift();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
