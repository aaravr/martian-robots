package com.aaravr.martian.robot.navigator.model;

import com.aaravr.martian.robot.navigator.exception.InvalidInputException;
import java.util.Arrays;


public enum MovementType {
    LEFT("L"),
    RIGHT("R"),
    FORWARD("F");

    private String key;

    MovementType(String identifier) {
        this.key = identifier;
    }

    public static MovementType getMovementFromKey(final String input) {
        if (input == null) {
            throw new IllegalArgumentException("Key for movement type cannot be null");
        }

       return  Arrays.stream(values()).filter(m -> m.key.equalsIgnoreCase(input)).findFirst()
                .orElseThrow(() -> new InvalidInputException(String.format("Movement type %s is invalid. Please provide a valid input", input)));
    }
}
