package org.exceptions.impl;

import org.exceptions.ForbiddenException;

public class InvalidWeightException extends ForbiddenException {

    public InvalidWeightException(String weight) {
        super("Maximum weight limit exceeded by : " + weight +"Kg");
    }
}
