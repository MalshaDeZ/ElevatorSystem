# Elevator System

The system is designed to function an elevator

The system supports only a single elevator

when a passenger presses the button, the floor number and the direction to be moved is stored inside treeSets depending on the direction of the movement

All the passenger requests will be stored in these sets

In order to move the elevator need to call the moveLift method in the ElevatorController class

Once it is executing the elevator will decide which direction move

To decide the direction
* if requests has been sent to only one direction, elevator will move in that direction
* if both directions have requests, then algorithm will check the closest floor and move in that direction
* if both directions have requests and next requests are equidistant from the current floor, then will select the direction with the maximum requests
