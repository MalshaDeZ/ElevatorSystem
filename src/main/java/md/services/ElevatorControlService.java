package md.services;

import md.constants.Direction;
import md.exceptions.impl.InvalidWeightException;
import md.models.Elevator;
import md.models.PassengerElevator;
import md.models.PassengerRequest;
import md.constants.ElevatorStatus;
import md.models.MovingDetails;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ElevatorControlService {


    private static final double TRANSITION_TIME = 1000; // in milliseconds

    Logger logger = Logger.getLogger(this.getClass().getName());
    private TreeSet<Integer> upQueue;
    private TreeSet<Integer> downQueue;

    private Elevator elevator;

    public ElevatorControlService() {

        this.upQueue = new TreeSet<>();
        this.downQueue = new TreeSet<>();
        this.elevator = new PassengerElevator();
    }

    public ElevatorControlService(Elevator elevator) {

        this.upQueue = new TreeSet<>();
        this.downQueue = new TreeSet<>();
        this.elevator = elevator;
    }

    /**
     * Add the new passenger request to corresponding queue to be processed
     *
     * @param passengerRequest contains targetFloor , requestDirection , currentWeight
     * @return whether the floor was added to the queue
     */
    public boolean addFloor(PassengerRequest passengerRequest) {

        // if the weight of the list exceeds the lift's capacity then throw an exception
        if (passengerRequest.getCurrentWeight() > elevator.getCapacity()) {
            throw new InvalidWeightException((elevator.getCapacity() - passengerRequest.getCurrentWeight()) + "");
        }

        // if the passenger requests a floor number higher than the current floor
        // then an item will be added to the up queue
        if (passengerRequest.getTargetFloor() >= this.elevator.getCurrentFloor()) {

            this.upQueue.add(passengerRequest.getTargetFloor());
            logger.log(Level.FINER, "Up queue size :" + upQueue.size());
            logger.info("Scheduling lift to move up to floor:" + passengerRequest.getTargetFloor());
            return true;
        }
        // if the passenger requests a floor number lower than the current floor
        // then an item will be added to the down queue
        this.downQueue.add(passengerRequest.getTargetFloor());

        logger.log(Level.FINER, "Down queue size : " + downQueue.size());

        logger.info("Scheduling lift to move down to floor:" + passengerRequest.getTargetFloor());

        return true;
    }

    /**
     * Contains the logic to move the lift
     *
     * @throws InterruptedException
     */
    public void moveLift() throws InterruptedException {

        this.elevator.setMovingDirection(this.getElevatorDirection());

        Direction currentDirection = this.elevator.getMovingDirection();

        if (this.elevator.getMotionStatus().equals(ElevatorStatus.IDLE)) {
            // if the elevator is idle then move in the direction of the nearest flow
            currentDirection = this.computeTheNearestLocation().getDirection();
        }
        if (currentDirection.equals(Direction.UP)) {
            this.moveLiftUp();
        } else if (currentDirection.equals(Direction.DOWN)) {
            this.moveLiftDown();
        } else {
            logger.info("Lift is moving no where");
        }
    }

    /**
     * Calculates the next in line destination
     *
     * @return the next target floor number
     */
    public int getNextFloor() {

        this.elevator.setMovingDirection(this.getElevatorDirection());

        if (!upQueue.isEmpty() || !downQueue.isEmpty()) {
            if (elevator.getMotionStatus().equals(ElevatorStatus.IDLE)) {
                // if the elevator is idle then move in the direction of the nearest flow
                return this.computeTheNearestLocation().getFloorNumber();

            } else if (elevator.getMotionStatus().equals(ElevatorStatus.MOVING)) {
                // if the lift is moving

                // if the current moving direction is up
                // then first check if there are any scheduled jobs for moving up
                // if so return the next in queue
                // if there are no scheduled jobs to move up then returns the next in queue

                // and vise versa if the lift is moving down
                if (this.elevator.getMovingDirection().equals(Direction.UP)) {

                    if (!upQueue.isEmpty()) {
                        return upQueue.first();
                    }
                    if (!downQueue.isEmpty()) {
                        return downQueue.last();
                    }

                } else if (this.elevator.getMovingDirection().equals(Direction.DOWN)) {

                    if (!downQueue.isEmpty()) {
                        return downQueue.last();
                    }
                    if (!upQueue.isEmpty()) {
                        return upQueue.first();
                    }
                } else {
                    logger.info("Lift is moving no where");
                }
            }
        }

        // if there are no scheduled jobs to move up or down
        // then the lift will go to IDLE mode

        elevator.setMotionStatus(ElevatorStatus.IDLE);

        return 0;
    }

    /**
     * Compute the next nearest location
     *
     * @return object containing the target floor number and target direction
     */
    private MovingDetails computeTheNearestLocation() {
        // if the distance from the current floor to the target flow when moving up is lower than that of moving down
        // then the lift will move up
        if (upQueue.first() - elevator.getCurrentFloor() < elevator.getCurrentFloor() - downQueue.last()) {
            return new MovingDetails(upQueue.first(), Direction.UP);
        } else if (upQueue.first() - elevator.getCurrentFloor() == elevator.getCurrentFloor() - downQueue.last()) {
            // if the distances are equal inorder to minimize the traffic and congestion
            // will check which direction has the most requests
            // and will select the biggest one
            // tradeoff - will be time consuming for the non selected direction

            logger.info("Finding the optimum way");

            if (upQueue.size() > downQueue.size()) {
                logger.info("Moving up");
                return new MovingDetails(upQueue.first(), Direction.UP);
            }

            logger.info("Moving down");
            return new MovingDetails(downQueue.last(), Direction.DOWN);

        }
        return new MovingDetails(downQueue.last(), Direction.DOWN);
    }

    /**
     * @return the current moving direction of the lift
     */
    private Direction getElevatorDirection() {
        if (!this.upQueue.isEmpty()) {
            return Direction.UP;
        } else if (!this.downQueue.isEmpty()) {
            return Direction.DOWN;
        }
        return Direction.NONE;
    }

    /**
     * Moves the lift up by emptying the up queue
     * Once there are no more jobs to move up then moves the lift dow if there are any jobs to move down
     *
     * @throws InterruptedException
     */
    private void moveLiftUp() throws InterruptedException {
        int prevFloor = 0;
        if (!upQueue.isEmpty()) {
            do {
                prevFloor = this.elevator.getCurrentFloor();
                this.elevator.setCurrentFloor(this.upQueue.pollFirst());
                this.elevator.setMovingDirection(Direction.UP);
                this.elevator.move();
                logger.info("Lift moving up to floor :" + this.elevator.getCurrentFloor());
                Thread.sleep(this.calculateTransitioningTime(prevFloor, this.elevator.getCurrentFloor()));// The time in which lift moves up
            } while (!this.upQueue.isEmpty());
        }
        if (!downQueue.isEmpty()) {
            moveLiftDown();
        } else {
            elevator.setMotionStatus(ElevatorStatus.IDLE);
            this.elevator.setMovingDirection(Direction.NONE);
        }
    }

    /**
     * Moves the lift down by emptying the down queue
     * Once there are no more jobs to move down then moves the lift up if there are any jobs to move up
     *
     * @throws InterruptedException
     */
    private void moveLiftDown() throws InterruptedException {
        int prevFloor = 0;
        if (!downQueue.isEmpty()) {
            do {
                prevFloor = this.elevator.getCurrentFloor();
                this.elevator.setCurrentFloor(this.downQueue.pollLast());
                this.elevator.setMovingDirection(Direction.DOWN);
                this.elevator.move();
                logger.info("Lift moving down to floor :" + this.elevator.getCurrentFloor());
                Thread.sleep(this.calculateTransitioningTime(prevFloor, this.elevator.getCurrentFloor()));// The time in which lift moves up
            } while (!this.downQueue.isEmpty());
        }
        if (!upQueue.isEmpty()) {
            moveLiftUp();
        } else {
            elevator.setMotionStatus(ElevatorStatus.IDLE);
            this.elevator.setMovingDirection(Direction.NONE);
        }
    }

    private long calculateTransitioningTime(int prevFloor, int currentFloor) {
        return (long) Math.abs(TRANSITION_TIME * (prevFloor - currentFloor));
    }
}
