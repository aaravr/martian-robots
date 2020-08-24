package com.aaravr.martian.robot.navigator.model;

import com.aaravr.martian.robot.navigator.exception.InvalidInputException;

import java.util.Arrays;


public enum DirectionType {
    NORTH("N", 0),
    EAST("E", 90),
    SOUTH("S", 180),
    WEST("W", 270);


    private String key;
    private int angle;

    public int getCurrentAngle() {
        return this.angle;
    }

    public String getKey() {
        return this.key;
    }

    DirectionType(String key, int angle) {
        this.key = key;
        this.angle = angle;
    }

    public static DirectionType getDirectionByAngle(final int angle) {

        if (angle < 0 || angle > 360) {
            throw new IllegalArgumentException("Invalid angle to retrive direction");
        }

        return  Arrays.stream(values())
                .filter(d -> d.getCurrentAngle() == angle).findFirst()
                .orElseThrow(() -> new InvalidInputException(String.format("Direction type %d is invalid. Please try again", angle)));
    }

    public static DirectionType getDirection(final String input) {
        if (input == null) {
            throw new IllegalArgumentException("Key for direction type cannot be null");
        }

        return  Arrays.stream(values())
                .filter(d -> d.key.equalsIgnoreCase(input.trim())).findFirst()
                .orElseThrow(() -> new InvalidInputException(String.format("Direction type %s is invalid. Please provide a valid direction", input)));
    }
}
