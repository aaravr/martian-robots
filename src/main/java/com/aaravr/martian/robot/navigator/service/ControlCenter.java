package com.aaravr.martian.robot.navigator.service;

import com.aaravr.martian.robot.navigator.exception.InvalidInputException;
import com.aaravr.martian.robot.navigator.exception.RobotDiedException;
import com.aaravr.martian.robot.navigator.model.*;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by raghu on 23/08/2020.
 */
@Component
@Slf4j
public class ControlCenter {
    private static final String SPACE = "\\s+";
    private static final int MIN_SURFACE_LENGTH = 0;
    private static final int MAX_SURFACE_LENGTH = 49;
    private static final int INPUT_LENGTH = 3;
    public static final int COORDINATES_LENGTH = INPUT_LENGTH;
    private static final int X_COORDINATE = 0;
    private static final int Y_COORDINATE = 1;
    private static final int DIRECTION_STRING_INDEX = 2;
    private static final String LOST = "LOST";

    private Map<Position, DirectionType> deathValleyPaths = new HashMap<>();

    private MarsSurface marsSurface;

    public void initialiseMarsSurface(String surfaceCoordinates) {
        try {
            String[] xAndyAxis = surfaceCoordinates.split(SPACE);

            if (xAndyAxis.length != 2) {
                throw new InvalidInputException("Invalid arguments for upper coordinates - Failed to initialise Mars surface");
            }

            if (StringUtils.isNumeric(xAndyAxis[X_COORDINATE]) && StringUtils.isNumeric(xAndyAxis[Y_COORDINATE])) {
                int xAxis = Integer.valueOf(xAndyAxis[X_COORDINATE]);
                int yAxis = Integer.valueOf(xAndyAxis[Y_COORDINATE]);

                validateAxisRange(xAxis, yAxis);

                int[][] coordinates = new int[xAxis][yAxis];

                this.marsSurface = MarsSurface.builder().coordinates(coordinates).build();

            } else {
                throw new InvalidInputException("Surface coordinates must be a valid integer value");
            }
        } catch (IllegalArgumentException validationException) {
            throw new InvalidInputException(validationException.getMessage());
        }
    }

    public Optional<Robot> validateAndProcessRobotMovementInput(String initialPosition, String moves) {

        Optional<Robot> robot = Optional.empty();
        if (Objects.isNull(moves) || moves.length() > 50) {
            throw new InvalidInputException("Movement instruction must be between 1 and 50 chars in length");
        }

        if (Objects.nonNull(initialPosition) && Objects.nonNull(moves)) {
            String[] robotInitialCoordinates = initialPosition.split(SPACE);
            if (robotInitialCoordinates.length == COORDINATES_LENGTH) {

                int startX = Integer.valueOf(robotInitialCoordinates[X_COORDINATE]);
                int startY = Integer.valueOf(robotInitialCoordinates[Y_COORDINATE]);

                Position robotInitialPosition = Position.builder().xaxis(startX).yaxis(startY).build();
                validateRobotInitialPosition(robotInitialPosition);

                DirectionType facingDirection = DirectionType.getDirection(robotInitialCoordinates[DIRECTION_STRING_INDEX]);
                Position nextMovementPosition = marsSurface.calculateNextPositionByDirection(robotInitialPosition, facingDirection);

                Robot createdRobot = createARobot(robotInitialPosition, facingDirection);
                createdRobot.setForwardMovementPosition(nextMovementPosition);

                List<MovementType> nextMoves = generateMoveTypesFromInput(moves);

                MovementInstruction instruction = MovementInstruction.builder().currentPosition(robotInitialPosition)
                        .nextMoves(nextMoves).build();

                moveAsPerInstruction(createdRobot, instruction);

                if (createdRobot.isLost()) {
                    log.info("{} {} {} {}", createdRobot.getForwardMovementPosition().getXaxis(),
                            createdRobot.getForwardMovementPosition().getYaxis(), createdRobot.getCurrentFacingDirection().getKey(), LOST);
                } else {
                    log.info("{} {} {}", createdRobot.getCurrentPosition().getXaxis(),
                            createdRobot.getCurrentPosition().getYaxis(), createdRobot.getCurrentFacingDirection().getKey());
                }
                robot = Optional.of(createdRobot);
            }
        }
        return robot;
    }

    private List<MovementType> generateMoveTypesFromInput(String moves) {
        if (Objects.nonNull(moves)) {
            return Arrays.stream(ArrayUtils.toObject(moves.toCharArray()))
                    .map(c -> MovementType.getMovementFromKey(String.valueOf(c))).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private void validateRobotInitialPosition(Position position) {
        if (!this.marsSurface.doesPositionExistInSurface(position)) {
            throw new InvalidInputException("Robot starting position cannot be off the grid");
        }
    }


    private void moveAsPerInstruction(Robot robot, MovementInstruction instruction) {
        try {
            for (MovementType movement : instruction.getNextMoves()) {
                DirectionType previousOffEdgeDirection = this.deathValleyPaths.get(robot.getCurrentPosition());
                robot.move(movement, marsSurface, previousOffEdgeDirection);
            }
        } catch (RobotDiedException exception) {
            robot.setLost(true);
            log.error("Robot died", exception);
            this.deathValleyPaths.put(exception.getLastOccupiedPosition(), exception.getLastOccupiedPosition().getDirectionType());
        }
    }

    private void validateAxisRange(int xAxis, int yAxis) {
        Validate.inclusiveBetween(MIN_SURFACE_LENGTH, MAX_SURFACE_LENGTH, xAxis);
        Validate.inclusiveBetween(MIN_SURFACE_LENGTH, MAX_SURFACE_LENGTH, yAxis);
    }

    //TODO: All robots are created with the same scent type for now - Should be created with random scent
    private Robot createARobot(Position robotInitialPosition, DirectionType facingDirection) {
        Robot robot = Robot.builder().currentPosition(robotInitialPosition).currentFacingDirection(facingDirection)
                .scentType(ScentType.ARMANI).build();
        robot.setName(UUID.randomUUID().toString());
        return robot;
    }
}
