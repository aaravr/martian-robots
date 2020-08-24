package com.aaravr.martian.robot.navigator.exception;

import com.aaravr.martian.robot.navigator.model.Position;
import lombok.Data;


@Data
public class RobotDiedException extends RuntimeException {

    private Position lastOccupiedPosition;
    private Position offGridPosition;


    public RobotDiedException(String message, Position lastOccupiedPosition, Position offGridPosition) {
        super(message);
        this.lastOccupiedPosition = lastOccupiedPosition;
        this.offGridPosition = offGridPosition;
    }
}
